package com.riyazuddin.shoppinglist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoppingItems")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name:String,
    val price: Float,
    val quantity: Int,
    val imageUrl: String,
)