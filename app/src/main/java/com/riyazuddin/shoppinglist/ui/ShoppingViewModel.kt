package com.riyazuddin.shoppinglist.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riyazuddin.shoppinglist.data.local.ShoppingItem
import com.riyazuddin.shoppinglist.data.remote.ImagesResponse
import com.riyazuddin.shoppinglist.others.Constants.MAX_NAME_LENGTH
import com.riyazuddin.shoppinglist.others.Constants.MAX_PRICE_LENGTH
import com.riyazuddin.shoppinglist.others.Event
import com.riyazuddin.shoppinglist.others.Resource
import com.riyazuddin.shoppinglist.repositories.ShoppingRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoppingViewModel @ViewModelInject constructor(
    private val repository: ShoppingRepository
): ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImagesResponse>>>()
    val images: LiveData<Event<Resource<ImagesResponse>>> = _images

    private val _currentImagesUrl = MutableLiveData<String>()
    val currentImagesUrl: LiveData<String> = _currentImagesUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImagesUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDB(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, quantity: String, price: String) {
        if(name.isEmpty() || quantity.isEmpty() || price.isEmpty()){
            _insertShoppingItemStatus.postValue(Event(Resource.error("The field must not be empty", null)))
            return
        }
        if (name.length > MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Name must be less than $MAX_NAME_LENGTH", null)))
            return
        }
        if (price.length > MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Price must be less than $MAX_PRICE_LENGTH", null)))
            return
        }
        val quantityInt = try {
            quantity.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Enter valid quantity", null)))
            return
        }
        val shoppingItem = ShoppingItem(name = name,price = price.toFloat(), quantity = quantityInt, imageUrl = currentImagesUrl.value ?: "")
        insertShoppingItemIntoDB(shoppingItem)
        setCurrentImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchImage(query: String) {
        if (query.isEmpty())
            return
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchImages(query)
            _images.value = Event(response)
        }
    }
}