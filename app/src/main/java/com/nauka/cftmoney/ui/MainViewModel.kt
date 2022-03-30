package com.nauka.cftmoney.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nauka.cftmoney.model.Repository
import com.nauka.cftmoney.model.dto.Main
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var commonData = MutableLiveData<Main?>()

    private  var timer: Timer? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkInternetConnection(200)) {
                apiLoadInfo()
            } else {
                roomLoadInfo()
            }
        }
    }


    //Load data with retrofit
    fun apiLoadInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAPI()?.awaitResponse().let {
                val db = repository.getDatabase().getAll()
                if (it != null) {
                    if (it.isSuccessful && it.code() == 200) {
                        val main = it.body()
                        commonData.postValue(main)

                        if (db?.date != null) {
                            repository.getDatabase().update(it.body())
                        } else {
                            repository.getDatabase().insertAll(it.body())
                        }
                    }
                }
            }
        }
    }

    //Load data with Room
    fun roomLoadInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDatabase().let {
                CoroutineScope(Dispatchers.IO).launch {
                    commonData.postValue(it.getAll())
                }
            }
        }
    }


    //Check internet connection
    fun checkInternetConnection(timeoutMs: Int): Boolean {
        try {
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            return true
        } catch (ex: IOException) {
            return false
        }
    }

    fun startAutoUpdate() {
        val periodTime:Long = 60*1000 // 1 minute
        timer = Timer()
        timer?.schedule(CustomTask(), 1000, periodTime)
    }

    fun stopAutoUpdate() {
        timer?.let {
            it.cancel()
            timer = null
        }
    }

    private inner class CustomTask: TimerTask() {
        override fun run() {
            viewModelScope.launch(Dispatchers.IO) {
                if (checkInternetConnection(200)) {
                    apiLoadInfo()
                } else {
                    roomLoadInfo()
                }
            }
        }
    }

}

