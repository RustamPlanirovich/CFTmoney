package com.nauka.cftmoney

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.nauka.cftmoney.Room.AppDatabase
import com.nauka.cftmoney.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {


    lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerAdapter = RecyclerAdapter(this)
        val apiInterface = ApiInterface.create().getValuteData()
        val callbackList = ArrayList<CurrencyModel>()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "main"
        ).build()

        CoroutineScope(Dispatchers.IO).launch {
            if (isReallyOnline()) {
                apiLoadInfo(apiInterface, callbackList, db)
            } else {
                roomLoadData(db, callbackList)
            }
        }



        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recyclerAdapter
        }






        binding.swipeUpdate.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (isReallyOnline()) {
                    apiLoadInfo(apiInterface, callbackList, db)
                } else {
                    binding.swipeUpdate.isRefreshing = false
                }
            }

        }

        binding.swipeUpdate.setColorSchemeColors(
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark
        )


    }

    fun roomLoadData(
        db: AppDatabase,
        callbackList: ArrayList<CurrencyModel>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val local = db.mainDao().getAll()

            withContext(Dispatchers.Main) {
                if (local != null) {
                    for (vals in local.valute.entries) {
                        val mod = vals.value
                        Log.d("Hel", callbackList.toString())
                        callbackList.add(mod)


                        local.apply {
                            binding.currentDateTV.text = this.date?.parseDate()
                            binding.previousDateTV.text = this.previousDate?.parseDate()
                            binding.timestampTV.text = this.timestamp?.parseDate()
                        }


                        recyclerAdapter.setValuteListItems(callbackList)
                    }
                }
            }
        }
    }

    fun apiLoadInfo(
        apiInterface: Call<Main>?,
        callbackList: ArrayList<CurrencyModel>,
        db: AppDatabase
    ) {
        apiInterface?.enqueue(object : Callback<Main?> {
            override fun onResponse(call: Call<Main?>, response: Response<Main?>) {

                Log.d("MyTag", response.body()?.valute.toString())
                val currencyResponse = response.body()


                for (value in currencyResponse!!.valute.entries) {
                    val model = value.value
                    Log.d("Hel", callbackList.toString())
                    callbackList.add(model)

                }
                response.body().apply {
                    binding.currentDateTV.text = this?.date?.parseDate()
                    binding.previousDateTV.text = this?.previousDate?.parseDate()
                    binding.timestampTV.text = this?.timestamp?.parseDate()

                }

                CoroutineScope(Dispatchers.IO).launch {

                    if (db.mainDao().getAll()?.date != null) {
                        db.mainDao().update(response.body())
                        Log.d("Data", "Update")
                    } else {
                        db.mainDao().insertAll(response.body())
                        Log.d("Data", "Insert")
                    }

                }

                recyclerAdapter.setValuteListItems(callbackList)
                binding.swipeUpdate.isRefreshing = false

            }

            override fun onFailure(call: Call<Main?>, t: Throwable) {
                Log.d("MyTag", "Failure " + t.toString())
            }
        })
    }
}

fun String.parseDate(): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val formatter = SimpleDateFormat("HH:mm:ss MMM dd yyyy", Locale.getDefault())
        formatter.format(parser.parse(this))
    } catch (e: ParseException) {
        LocalDateTime.now().toString()
    }
}


suspend fun isReallyOnline(): Boolean {

    val runtime = Runtime.getRuntime()
    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
        val exitValue = ipProcess.waitFor()
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    return false
}