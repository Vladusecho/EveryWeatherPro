package com.vladusecho.everyweatherpro.data.network.api

import com.vladusecho.everyweatherpro.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {

    private const val API_KEY = "key"
    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val oldRequest = chain.request()
            val newUrl = oldRequest
                .url
                .newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.WEATHER_API_KEY)
                .build()
            val newRequest = oldRequest.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create()
}