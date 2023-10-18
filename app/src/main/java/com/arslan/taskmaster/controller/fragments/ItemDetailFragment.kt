package com.arslan.taskmaster.controller.fragments

import Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arslan.taskmaster.databinding.FragmentItemDetailBinding
import com.google.gson.Gson

class ItemDetailFragment : Fragment() {
    private var binding: FragmentItemDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        // Return the root view of the binding
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Access views through the binding
        getIntentValue()
    }

    private fun getIntentValue() {
        // Access the arguments that were passed from the previous fragment
        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())
        val jsonData = args.itemObject
        val item = Gson().fromJson(jsonData, Item::class.java)

        if (item != null){
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

}