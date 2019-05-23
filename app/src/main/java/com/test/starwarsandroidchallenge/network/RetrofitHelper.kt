package com.test.starwarsandroidchallenge.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    private constructor() {}

    companion object {
        private var retrofit: Retrofit? = null
        fun getAPIService(): APIService? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createAsync()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://swapi.co/api/")
                    .build()
            }
            return retrofit?.create(APIService::class.java)
        }
    }

}