package com.himanshu.cryptotrackerapp.data.repository

import com.himanshu.cryptotrackerapp.data.network.ApiService
import javax.inject.Inject

class CryptoRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getCryptoCurrencies(page :Int) = apiService.getCryptoCurrencies(page)
}