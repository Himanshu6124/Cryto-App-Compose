package com.himanshu.cryptotrackerapp.ui.cryptoList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu.cryptotrackerapp.data.models.MarketModel
import com.himanshu.cryptotrackerapp.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val TAG = "CryptoListViewModel"

    private var _list = MutableStateFlow(MarketModel())
    val list = _list.asStateFlow()


    init {
        Log.i(TAG,"init block called")
        getCryptocurrencies()
    }


    private fun getCryptocurrencies(){

        viewModelScope.launch {

            try {
                val res = apiService.getCryptoCurrencies()
                Log.i(TAG,"Response is ${res.body()}")
                _list.emit(res.body()!!)

            }
            catch (e:Exception){
                Log.e(TAG,"Exception occurred ${e.message}")
            }
        }
    }
}