package com.arslan.taskmaster.view_models

import Item
import ItemRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ItemListViewModel : ViewModel() {
    private val itemRepository = ItemRepository()
    private val _itemList = MutableLiveData<List<Item>>()
    val itemList: LiveData<List<Item>> = _itemList
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun loadItemsForCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.getItemsForCurrentUser { items, exception ->
                if (items != null) {
                    _itemList.postValue(items)
                } else {
                    _error.postValue(exception)
                }
            }
        }

    }

    fun deleteItem(itemId: String, callback: (Boolean, String?) -> Unit) {
        // Show a loading indicator if you have one (e.g., using LiveData)
        viewModelScope.launch(Dispatchers.IO) {

            itemRepository.deleteItem(itemId) { success, errorMessage ->
                if (success) {
                    // Item deleted successfully
                    callback(true, null) // Indicate success
                } else {
                    // errorMessage will contain the reason for deletion failure
                    callback(false, errorMessage)
                }
        }

        }
    }

    fun updateItem(item: Item, callback: (Boolean, String?) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.updateItem(item) { success, errorMessage ->
                if (success) {
                    callback(true, null) // Indicate success
                } else {
                    // errorMessage will contain the reason for deletion failure
                    callback(false, errorMessage)
                }

            }
        }

    }
}
