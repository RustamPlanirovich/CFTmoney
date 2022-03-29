package com.nauka.cftmoney

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {


    lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerAdapter = RecyclerAdapter(this)

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recyclerAdapter
        }
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "main"
        ).build()


        val callbackList = ArrayList<CurrencyModel>()
        val mainList = HashMap<String, CurrencyModel>()



        val apiInterface = ApiInterface.create().getValuteData()

        apiInterface?.enqueue(object : Callback<Main?> {
            override fun onResponse(call: Call<Main?>, response: Response<Main?>) {

                Log.d("MyTag", response.body()?.valute.toString())
                val currencyResponse = response.body()

                Log.d("Hel", mainList.toString())

                for (value in currencyResponse!!.valute!!.entries) {
                    val model = value.value
                    callbackList.add(model)

                }
                response.body().apply {
                    binding.currentDateTV.text = this?.date?.parseDate()
                    binding.previousDateTV.text = this?.previousDate?.parseDate()
                    binding.timestampTV.text = this?.timestamp?.parseDate()

                }

                CoroutineScope(Dispatchers.IO).launch {
                    db.mainDao().insertAll(callbackList)
                }




                recyclerAdapter.setValuteListItems(callbackList)

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