package com.riyazuddin.shoppinglist.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.riyazuddin.shoppinglist.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    private lateinit var db: ShoppingDatabase
    private lateinit var dao: ShoppingDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            ShoppingDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.shoppingDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem(1, "Banana", 5.0f, 10, "")
        dao.insertShoppingItem(shoppingItem)
        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem.contains(shoppingItem)).isTrue()
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem(1, "Banana", 5.0f, 10, "")
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem.contains(shoppingItem)).isFalse()
    }

    @Test
    fun observeTotalPrice() = runBlockingTest {
        val shoppingItem1 = ShoppingItem(1, "Banana", 5.0f, 10, "")
        val shoppingItem2 = ShoppingItem(2, "Banana", 5.0f, 10, "")
        val shoppingItem3 = ShoppingItem(3, "Banana", 5.0f, 10, "")
        val shoppingItem4 = ShoppingItem(4, "Banana", 5.0f, 10, "")
        val shoppingItem5 = ShoppingItem(5, "Banana", 5.0f, 10, "")
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)
        dao.insertShoppingItem(shoppingItem4)
        dao.insertShoppingItem(shoppingItem5)
        val result = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(result).isEqualTo(5.0f*10 + 5.0f*10 + 5.0f*10 + 5.0f*10 + 5.0f*10)
    }
}