package com.riyazuddin.shoppinglist.data.remote

data class ImagesResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)