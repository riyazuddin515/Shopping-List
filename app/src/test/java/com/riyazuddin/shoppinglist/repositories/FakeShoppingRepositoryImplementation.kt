package com.riyazuddin.shoppinglist.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.riyazuddin.shoppinglist.data.local.ShoppingItem
import com.riyazuddin.shoppinglist.data.remote.ImagesResponse
import com.riyazuddin.shoppinglist.others.Resource

class FakeShoppingRepositoryImplementation: ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observeAllShoppingItem = MutableLiveData<List<ShoppingItem>>()
    private val observeTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observeAllShoppingItem.postValue(shoppingItems)
        observeTotalPrice.postValue(shoppingItems.sumByDouble { it.price.toDouble() }.toFloat())
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observeAllShoppingItem
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observeTotalPrice
    }

    override suspend fun searchImages(query: String): Resource<ImagesResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("network error", null)
        }else{
            Resource.success(ImagesResponse(listOf(), 0, 0))
        }
    }
}