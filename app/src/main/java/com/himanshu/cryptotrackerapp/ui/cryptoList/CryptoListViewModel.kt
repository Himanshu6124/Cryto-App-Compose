package com.himanshu.cryptotrackerapp.ui.cryptoList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu.cryptoapp.models.CryptoCurrencyListItem
import com.himanshu.cryptotrackerapp.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val TAG = "CryptoListViewModel"

    private var _list :  MutableStateFlow<List<CryptoCurrencyListItem>> = MutableStateFlow(emptyList())
    val list = _list.asStateFlow()

    private var _loading = MutableStateFlow(false)
    var loading = _loading.asStateFlow()

    private var currentPage = 0


    fun getCryptocurrencies(){

        currentPage++
        viewModelScope.launch {
            try {
                _loading.emit(true)
                val res = apiService.getCryptoCurrencies(currentPage)
                _loading.emit(false)
                Log.i(TAG,"Response is ${res.body()!!.data}")
                val newData = res.body()!!.data?.cryptoCurrencyList
                val updatedList = _list.value + newData as List<CryptoCurrencyListItem>
                _list.emit(updatedList)
            }
            catch (e:Exception){
                _loading.emit(false)
                Log.e(TAG,"Exception occurred ${e.message}")
            }
        }
    }
}