package com.riyazuddin.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.riyazuddin.shoppinglist.MainCoroutineRule
import com.riyazuddin.shoppinglist.others.Constants
import com.riyazuddin.shoppinglist.others.Status
import com.riyazuddin.shoppinglist.repositories.FakeShoppingRepositoryImplementation
import com.riyazuddin.shoppinglist.repositories.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepositoryImplementation())
    }

    @Test
    fun insert_shopping_item_with_empty_field_ReturnError(){
        viewModel.insertShoppingItem("banana","","5")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insert_shopping_item_with_too_long_name_ReturnError(){
        val name = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH+1){
                append(i)
            }
        }
        viewModel.insertShoppingItem(name,"10","5")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insert_shopping_item_with_too_long_price_ReturnError(){
        val price = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH+1){
                append(i)
            }
        }
        viewModel.insertShoppingItem("name","10",price)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insert_shopping_item_with_invalid_quantity_price_ReturnError(){
        viewModel.insertShoppingItem("name","99999999999","5")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insert_shopping_item_with_valid_fields_ReturnError(){
        viewModel.insertShoppingItem("name","10","5")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}