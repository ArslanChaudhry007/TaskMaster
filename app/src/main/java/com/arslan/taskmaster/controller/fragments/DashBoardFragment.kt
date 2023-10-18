package com.arslan.taskmaster.controller.fragments

import Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.arslan.taskmaster.R
import com.arslan.taskmaster.constants.*
import com.arslan.taskmaster.databinding.FragmentDashBoardBinding
import com.arslan.taskmaster.interfaces.GenericCallbackAdapter
import com.arslan.taskmaster.model.ItemListAdapter
import com.arslan.taskmaster.view_models.ItemListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch


class DashBoardFragment : Fragment(), View.OnClickListener, GenericCallbackAdapter {

    private var binding: FragmentDashBoardBinding? = null
    lateinit var navGraph: NavController
    private lateinit var viewModel: ItemListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)

        // Return the root view of the binding
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Access views through the binding
        navGraph = Navigation.findNavController(view)

        initListeners()
        initViewModel()
    }

    private fun initViewModel() {

        // Observe the list of items from the ViewModel
        viewModel.itemList.observe(viewLifecycleOwner) { items: List<Item> ->
            // Update the RecyclerView adapter with the new list of items
            showItemOnRecelerView(items)
            HideProgressDialog()
        }


        // Observe any errors that might occur during item retrieval
        viewModel.error.observe(viewLifecycleOwner) { error ->
            // Handle the error here, e.g., show a toast message
            showToast(requireContext(), "Error: ${error.message}")
            HideProgressDialog()
        }

    }


    private fun showItemOnRecelerView(items: List<Item>) {
        val adapter = ItemListAdapter(items, requireContext(), this)
        binding?.itemListRecyclerview?.adapter = adapter

    }

    private fun initListeners() {
        binding?.btnAddItem?.setOnClickListener(this)
        // Load the list of items for the current user
        viewModel = ViewModelProvider(this)[ItemListViewModel::class.java]
        if (checkNetworkConnectivity(requireContext())){
            ShowProgressDialog(requireActivity(),"Loading", "Please wait...")
            lifecycleScope.launch {
                viewModel.loadItemsForCurrentUser()
            }
        } else {
            showToast(requireContext(), CHECK_INTERNET)
        }

    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_addItem ->{
                navGraph.navigate(R.id.action_dashBoardFragment_to_addItemFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        binding = null
    }

    override fun <T> getClickedObject(clickedObj: T, position: T, callingID: String) {
        when (callingID) {
            "DELETE_ITEM" -> handleDeleteItem(clickedObj as String)
            "VIEW_ITEM" -> navigateToItemDetailFragment(clickedObj as Item)
            "Edit_ITEM" -> navigateToItemEditFragment(clickedObj as Item)
        }
    }

    private fun handleDeleteItem(itemId: String) {
        if (!checkNetworkConnectivity(requireContext())) {
            showToast(requireContext(), CHECK_INTERNET)
            return
        }

        ShowProgressDialog(requireActivity(), "Deleting", "Please wait")
        lifecycleScope.launch {
            viewModel.deleteItem(itemId) { success, errorMessage ->
                HideProgressDialog()
                if (success) {
                    showToast(requireContext(), "Item deleted successfully")
                    viewModel.loadItemsForCurrentUser()
                } else {
                    showToast(requireContext(), "Deletion failed: $errorMessage")
                }
            }
        }
    }

    private fun navigateToItemDetailFragment(item: Item) {
        val itemJson = Gson().toJson(item)
        val action = DashBoardFragmentDirections.actionDashBoardFragmentToItemDetailFragment(itemJson)
        navGraph.navigate(action)
    }

    private fun navigateToItemEditFragment(item: Item) {
        val itemJson = Gson().toJson(item)
        val action = DashBoardFragmentDirections.actionDashBoardFragmentToItemEditFragment(itemJson)
        navGraph.navigate(action)
    }

}
