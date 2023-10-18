package com.arslan.taskmaster.controller.fragments

import AddItemViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.arslan.taskmaster.R
import com.arslan.taskmaster.constants.*
import com.arslan.taskmaster.databinding.FragmentAddItemBinding
import com.arslan.taskmaster.interfaces.UploadResultCallback
import kotlinx.coroutines.launch

class AddItemFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentAddItemBinding? = null
    private lateinit var viewModel: AddItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddItemViewModel::class.java)
        initListeners()
    }

    private fun initListeners() {
        binding?.btnAddItem?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnAddItem -> {
                requireActivity().hideKeyboard()
                if (validateInput()) {
                    if (checkNetworkConnectivity(requireContext())) {
                        ShowProgressDialog(requireActivity(), "Loading", "Please wait...")

                        val productName = binding?.productNameId?.text.toString().trim()
                        val productQuantity = binding?.productQuanityId?.text.toString().trim()
                        val productPrice = binding?.productPriceId?.text.toString().trim()
                        val productDescription = binding?.productDesId?.text.toString().trim()

                        lifecycleScope.launch {
                            viewModel.uploadItem(
                                productName,
                                productQuantity,
                                productPrice,
                                productDescription,
                                object : UploadResultCallback {
                                    override fun onSuccess() {
                                        HideProgressDialog()
                                        showToast(requireContext(), "Item uploaded successfully")
                                        clearFields() // Clear input fields

                                    }

                                    override fun onFailure(error: Exception) {
                                        HideProgressDialog()
                                        showToast(requireContext(), "Item upload failed: ${error.message}")
                                    }
                                }
                            )
                        }

                    } else {
                        showToast(requireContext(), CHECK_INTERNET)
                    }
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (binding?.productNameId?.text.toString().trim().isEmpty()) {
            binding?.productNameLayout?.error = "Enter Product Name"
            binding?.productNameId?.disableError(binding!!.productNameLayout)
            isValid = false
        }

        if (binding?.productQuanityId?.text.toString().trim().isEmpty()) {
            binding?.productQuanityLayout?.error = "Enter Product Quantity"
            binding?.productQuanityId?.disableError(binding!!.productQuanityLayout)
            isValid = false
        }

        if (binding?.productPriceId?.text.toString().trim().isEmpty()) {
            binding?.productPriceLayout?.error = "Enter Product Price"
            binding?.productPriceId?.disableError(binding!!.productPriceLayout)
            isValid = false
        }

        if (binding?.productDesId?.text.toString().trim().isEmpty()) {
            binding?.productDesLayout?.error = "Enter Product Description"
            binding?.productDesId?.disableError(binding!!.productDesLayout)
            isValid = false
        }
        return isValid
    }

    private fun clearFields() {
        binding?.productNameId?.text?.clear()
        binding?.productQuanityId?.text?.clear()
        binding?.productPriceId?.text?.clear()
        binding?.productDesId?.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
