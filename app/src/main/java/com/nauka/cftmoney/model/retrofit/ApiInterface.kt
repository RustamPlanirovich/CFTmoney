package com.nauka.cftmoney

import com.nauka.cftmoney.model.dto.Main
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("daily_json.js")
    fun getValuteData(): Call<Main>?
}