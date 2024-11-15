package com.himanshu.cryptotrackerapp.data.network

import com.himanshu.cryptotrackerapp.data.models.MarketModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=20")
    suspend fun getCryptoCurrencies(): Response<MarketModel>


}