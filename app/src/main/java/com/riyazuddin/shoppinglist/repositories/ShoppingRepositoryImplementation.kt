package com.riyazuddin.shoppinglist.repositories

import androidx.lifecycle.LiveData
import com.riyazuddin.shoppinglist.data.local.ShoppingDao
import com.riyazuddin.shoppinglist.data.local.ShoppingItem
import com.riyazuddin.shoppinglist.data.remote.ImagesResponse
import com.riyazuddin.shoppinglist.data.remote.PixabayApi
import com.riyazuddin.shoppinglist.others.Resource
import javax.inject.Inject

class ShoppingRepositoryImplementation @Inject constructor(
    private val dao: ShoppingDao,
    private val pixabayApi: PixabayApi
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        dao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        dao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return dao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return dao.observeTotalPrice()
    }

    override suspend fun searchImages(query: String): Resource<ImagesResponse> {
        return try {
            val response = pixabayApi.searchImage(query)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("error ${response.errorBody()}", null)
            }else{
                Resource.error("error ${response.errorBody()}", null)
            }
        } catch (e: Exception) {
            Resource.error("error: ${e.message}",null)
        }
    }
}