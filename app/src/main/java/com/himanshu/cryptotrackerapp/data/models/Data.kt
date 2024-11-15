package com.himanshu.cryptotrackerapp.data.models

import com.himanshu.cryptoapp.models.CryptoCurrencyListItem

data class Data(
    val cryptoCurrencyList: List<CryptoCurrencyListItem>?,
    val totalCount: String = ""
)