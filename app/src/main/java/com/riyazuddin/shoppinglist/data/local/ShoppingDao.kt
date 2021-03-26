package com.riyazuddin.shoppinglist.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shoppingItems")
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    @Query("SELECT SUM(price*quantity) FROM shoppingItems")
    fun observeTotalPrice(): LiveData<Float>
}