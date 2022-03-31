package com.nauka.cftmoney

import com.nauka.cftmoney.model.dto.Main
import com.nauka.cftmoney.util.Constants.TARGET
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    //Выполнение запроса в сеть
    @GET(TARGET)
    fun getValuteData(): Call<Main>?
}