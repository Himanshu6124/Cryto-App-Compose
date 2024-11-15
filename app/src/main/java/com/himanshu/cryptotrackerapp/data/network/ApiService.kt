package com.himanshu.cryptotrackerapp.data.network

import com.himanshu.cryptotrackerapp.data.models.MarketModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=20")
    suspend fun getCryptoCurrencies(@Query("start") start: Int, @Query("limit") limit: Int = 20): Response<MarketModel>


}