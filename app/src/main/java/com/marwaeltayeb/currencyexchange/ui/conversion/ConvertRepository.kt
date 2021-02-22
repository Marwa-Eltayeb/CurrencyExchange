package com.marwaeltayeb.currencyexchange.ui.conversion

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.currencyexchange.data.model.HistoricApiResponse
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConvertRepository {

    private val mutableLiveData: MutableLiveData<List<Pair<String, Double>>> = MutableLiveData<List<Pair<String,Double>>>()
    private val historicalRatesLiveData: MutableLiveData<HistoricApiResponse> = MutableLiveData<HistoricApiResponse>()

    fun getMutableLiveData(base: String, symbol: String): MutableLiveData<List<Pair<String,Double>>> {
        RetrofitClient.getRateService().getSpecificExchangeRate(base, symbol)
            .enqueue(object : Callback<RateApiResponse> {
                override fun onFailure(call: Call<RateApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<RateApiResponse>, response: Response<RateApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            mutableLiveData.setValue(response.body()!!.rates.toList())
                        }
                    }
                }
            })
        return mutableLiveData
    }

    fun getMutableLiveData(startDate: String, endDate: String ,base: String, symbol: String): MutableLiveData<HistoricApiResponse> {
        RetrofitClient.getRateService().getHistoricalRates(startDate, endDate, base, symbol)
            .enqueue(object : Callback<HistoricApiResponse> {
                override fun onFailure(call: Call<HistoricApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<HistoricApiResponse>, response: Response<HistoricApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            historicalRatesLiveData.setValue(response.body())
                        }
                    }
                }
            })
        return historicalRatesLiveData
    }
}