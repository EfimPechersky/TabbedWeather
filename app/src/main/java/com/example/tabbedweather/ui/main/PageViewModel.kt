package com.example.tabbedweather.ui.main

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.example.tabbedweather.WeatherApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.util.Scanner

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    lateinit var weatherirk:WeatherApi
    lateinit var weathermos:WeatherApi
    var loading = 0;
    var temp: LiveData<String> = _index.map {
        when (it){
            1->if (::weatherirk.isInitialized){weatherirk.main.temp.toString()}else{"Loading"}
            else->if (::weathermos.isInitialized){weathermos.main.temp.toString()}else{"Loading"}
        }
    }

    var wind: LiveData<String> = _index.map {
        when (it){
            1->if (::weatherirk.isInitialized){weatherirk.wind.speed.toString()}else{"Loading"}
            else->if (::weathermos.isInitialized){weathermos.wind.speed.toString()}else{"Loading"}
        }
        }

    var weather: LiveData<String> = _index.map {
        when (it) {
            1 -> if (::weatherirk.isInitialized) {
                weatherirk.weather[0].main
            } else {
                "Loading"
            }

            else -> if (::weathermos.isInitialized) {
                weathermos.weather[0].main
            } else {
                "Loading"
            }
        }
    }



    fun setIndex(index: Int) {
        _index.value = index
        GlobalScope.launch (Dispatchers.IO) {
            loadWeather()
        }
    }
    suspend fun loadWeather() {
        val API_KEY = "78e056c652997738cf55e5a6f0b7a40b" // TODO: ключ загрузить из строковых ресурсов
        // TODO: в строку подставлять API_KEY и город (выбирается из списка или вводится в поле)
        var weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=Irkutsk&appid="+API_KEY+"&units=metric";
        var stream = URL(weatherURL).getContent() as InputStream
        val gson = Gson()
        weatherirk=gson.fromJson(InputStreamReader(stream), WeatherApi::class.java)
        weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=Moscow&appid="+API_KEY+"&units=metric";
        stream = URL(weatherURL).getContent() as InputStream
        weathermos=gson.fromJson(InputStreamReader(stream), WeatherApi::class.java)
        Log.d("test", weathermos.weather[0].main)
        loading=1;
    }



}