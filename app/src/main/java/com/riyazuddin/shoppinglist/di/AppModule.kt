package com.riyazuddin.shoppinglist.di

import android.content.Context
import androidx.room.PrimaryKey
import androidx.room.Room
import com.riyazuddin.shoppinglist.data.local.ShoppingDao
import com.riyazuddin.shoppinglist.data.local.ShoppingDatabase
import com.riyazuddin.shoppinglist.data.remote.PixabayApi
import com.riyazuddin.shoppinglist.others.Constants.DATABASE_NAME
import com.riyazuddin.shoppinglist.others.Constants.PIXABAY_BASE_URL
import com.riyazuddin.shoppinglist.repositories.ShoppingRepository
import com.riyazuddin.shoppinglist.repositories.ShoppingRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShoppingDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideShoppingDao(
        database: ShoppingDatabase
    ) = database.shoppingDao()

    @Provides
    @Singleton
    fun provideShoppingRepository(
        dao: ShoppingDao,
        pixabayApi: PixabayApi
    ) = ShoppingRepositoryImplementation(dao, pixabayApi) as ShoppingRepository

    @Provides
    @Singleton
    fun providePixabayApi(): PixabayApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PIXABAY_BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }
}
