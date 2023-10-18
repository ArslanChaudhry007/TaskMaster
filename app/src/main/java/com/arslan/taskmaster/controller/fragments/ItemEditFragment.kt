package com.arslan.taskmaster.controller.fragments

import AddItemViewModel
import Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.arslan.taskmaster.R
import com.arslan.taskmaster.constants.*
import com.arslan.taskmaster.databinding.FragmentItemDetailBinding
import com.arslan.taskmaster.databinding.FragmentItemEditBinding
import com.arslan.taskmaster.view_models.ItemListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch


class ItemEditFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentItemEditBinding? = null
    private lateinit var viewModel: ItemListViewModel
    private var productId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding
        binding = FragmentItemEditBinding.inflate(inflater, container, false)
        // Return the root view of the binding
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Access views through the binding
        viewModel = ViewModelProvider(this)[ItemListViewModel::class.java]
        initListeners()
        getIntentValue()
    }

    private fun initListeners() {
        binding?.btnEditItem?.setOnClickListener(this)
    }

    private fun getIntentValue() {
        // Access the arguments that were passed from the previous fragment
        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())
        val jsonData = args.itemObject
        val item = Gson().fromJson(jsonData, Item::class.java)

        if (item != null) {
            productId = item.id
            binding?.prodctName?.setText(item.productName)
            binding?.prodctQuantity?.setText(item.productQuantity)
            binding?.prodctPrice?.setText(item.productPrice)
            binding?.prodctDes?.setText(item.productDescription)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        binding = null
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnEditItem -> {

                if (checkNetworkConnectivity(requireContext())) {
                    requireActivity().hideKeyboard()
                    if (validateInput()) {
                        val updatedItem = Item()
                        updatedItem.id = productId // Set the product ID
                        updatedItem.productName = binding?.prodctName?.text.toString()
                        updatedItem.productQuantity = binding?.prodctQuantity?.text.toString()
                        updatedItem.productPrice = binding?.prodctPrice?.text.toString()
                        updatedItem.productDescription = binding?.prodctDes?.text.toString()
                        ShowProgressDialog(requireActivity(), "Updating", "Please wait...")
                        lifecycleScope.launch {
                            viewModel.updateItem(updatedItem) { success, errorMessage ->
                                HideProgressDialog()
                                if (success) {
                                    showToast(requireContext(), "Item updated successfully")
                                    // Optionally, you can navigate back to the item list or perform other actions
                                } else {
                                    showToast(requireContext(), "Update failed: $errorMessage")
                                }
                            }
                        }
                    }

                } else {
                    showToast(requireContext(), CHECK_INTERNET)
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (binding?.prodctName?.text.toString().trim().isEmpty()) {
            binding?.productNameLayout?.error = "Enter Product Name"
            binding?.prodctName?.disableError(binding!!.productNameLayout)
            isValid = false
        }

        if (binding?.prodctQuantity?.text.toString().trim().isEmpty()) {
            binding?.productQuantityLayout?.error = "Enter Product Quantity"
            binding?.prodctQuantity?.disableError(binding!!.productQuantityLayout)
            isValid = false
        }

        if (binding?.prodctPrice?.text.toString().trim().isEmpty()) {
            binding?.productPriceLayout?.error = "Enter Product Price"
            binding?.prodctPrice?.disableError(binding!!.productPriceLayout)
            isValid = false
        }

        if (binding?.prodctDes?.text.toString().trim().isEmpty()) {
            binding?.productDesLayout?.error = "Enter Product Description"
            binding?.prodctDes?.disableError(binding!!.productDesLayout)
            isValid = false
        }
        return isValid
    }
}
