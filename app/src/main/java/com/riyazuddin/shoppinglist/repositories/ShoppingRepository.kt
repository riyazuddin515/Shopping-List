package com.riyazuddin.shoppinglist.repositories

import androidx.lifecycle.LiveData
import com.riyazuddin.shoppinglist.data.local.ShoppingItem
import com.riyazuddin.shoppinglist.data.remote.ImagesResponse
import com.riyazuddin.shoppinglist.others.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchImages(query: String): Resource<ImagesResponse>
}