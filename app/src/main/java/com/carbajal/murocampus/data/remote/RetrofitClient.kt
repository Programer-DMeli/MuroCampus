package com.carbajal.murocampus.data.remote

//Hacemos uso del patrol singleton porque RetrofitClient existira solo un vez dentro de la aplicacion
//Evitas crear multiples conexiones innecesarias a internet

//un object es un patron singleton

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val json = Json {
        ignoreUnknownKeys = true
    }
//nos permite depurara errores

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()


    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}