package com.example.hotel.di

import android.content.Context
import com.example.hotel.auth.Auth
import com.example.hotel.auth.AuthMananger
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import com.example.hotel.repository.HotelRepository
import com.example.hotel.details.HotelDetailsViewModel
import com.example.hotel.form.HotelFormViewModel
import com.example.hotel.list.HotelListViewModel
import com.example.hotel.repository.http.HotelHttp
import com.example.hotel.repository.http.HotelHttpApi
import com.example.hotel.repository.imagefiles.FindHotelPicture
import com.example.hotel.repository.imagefiles.ImageGalleryPictureFinder
import com.example.hotel.repository.room.HotelDatabase
import com.example.hotel.repository.room.RoomRepository
import org.koin.dsl.module

val androidModule = module {
    single { this }

    single {
        RoomRepository(HotelDatabase.getDatabase(context = get())) as HotelRepository
    }

    factory {
        HotelListViewModel(repository = get())
    }

    factory {
        HotelDetailsViewModel(repository = get())
    }

    factory {
        HotelFormViewModel(repository = get())
    }

    single {
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(HotelHttp.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()

        retrofit.create<HotelHttpApi>(HotelHttpApi::class.java)
    }

    factory {
        val context = get() as Context
        val resolver = context.contentResolver
        val uploadDir = context.externalCacheDir ?: context.cacheDir
        ImageGalleryPictureFinder(uploadDir, resolver) as FindHotelPicture
    }

    factory {
        HotelHttp(
            service = get(),
            repository = get(),
            pictureFinder = get(),
            currentUser = "nglauber"
        )
    }

    single {
        val manager: AuthMananger = get()
        manager as Auth
    }

    single {
        AuthMananger(context = get())
    }
}

