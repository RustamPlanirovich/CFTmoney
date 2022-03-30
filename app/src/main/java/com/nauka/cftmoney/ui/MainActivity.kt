package com.nauka.cftmoney.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.nauka.cftmoney.R
import com.nauka.cftmoney.databinding.ActivityMainBinding
import com.nauka.cftmoney.databinding.ValuteItemBinding
import com.nauka.cftmoney.model.dto.CurrencyModel
import com.nauka.cftmoney.util.parseDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RecyclerAdapter.AdapterListener {


    lateinit var recyclerAdapter: RecyclerAdapter
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var _itemBinding: ValuteItemBinding? = null
    private val itemBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        _itemBinding = ValuteItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemBinding.root

        recyclerAdapter = RecyclerAdapter(this, this)


        val callbackList = ArrayList<CurrencyModel>()
        Log.d("Hel", callbackList.toString())

        val isChecked = this.getPreferences(Context.MODE_PRIVATE).getBoolean(
            getString(R.string.checkbox_pref), false
        )
        binding.autoUpdateCB.isChecked = isChecked

        binding.autoUpdateCB.setOnCheckedChangeListener { _, isCheckedL ->
            if (isCheckedL) {
                viewModel.startAutoUpdate()
            } else {
                viewModel.stopAutoUpdate()
            }
        }



        viewModel.commonData.observe(this) { body ->
            if (body != null) {
                callbackList.clear()
                for (value in body.valute.entries) {
                    val model = value.value
                    Log.d("Hel", callbackList.toString())
                    callbackList.add(model)
                }
                body.apply {
                    binding.currentDateTV.text = this.date.parseDate()
                    binding.previousDateTV.text = this.previousDate?.parseDate()
                    binding.timestampTV.text = this.timestamp?.parseDate()
                }
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = recyclerAdapter
                }
                recyclerAdapter.setValuteListItems(callbackList)
            }
        }


        binding.swipeUpdate.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (viewModel.checkInternetConnection(200)) {
                    withContext(Dispatchers.Main) {
                        viewModel.apiLoadInfo()
                        binding.swipeUpdate.isRefreshing = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        viewModel.roomLoadInfo()
                        binding.swipeUpdate.isRefreshing = false
                    }

                }
            }

        }
    }

    override fun onStart() {
        super.onStart() //Проверка при старте приложения
        if (binding.autoUpdateCB.isChecked) //Если установлен чек-бокс то включается автообновление
            viewModel.startAutoUpdate()
    }

    override fun onStop() {
        super.onStop() //Проверка при остановке приложения
        viewModel.stopAutoUpdate() //Выключение автообновления
    }

    override fun onPause() {
        super.onPause()
        val pref = this.getPreferences(Context.MODE_PRIVATE)
        with(pref.edit()) {
            putBoolean(getString(R.string.checkbox_pref), binding.autoUpdateCB.isChecked)
            apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _itemBinding = null
    }

    override fun onItemsSelected(
        snapshot1: CurrencyModel,
        snapshot: EditText,
        text: TextView,
        convertLayout: ConstraintLayout
    ) {

        when (convertLayout.visibility) {
            View.VISIBLE -> {
                convertLayout.visibility = View.GONE
            }
            View.GONE -> {
                convertLayout.visibility = View.VISIBLE
                snapshot.text.clear()
                text.text = "0"
            }
        }
        snapshot.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                val result = snapshot.text.toString()
                    .toInt() * snapshot1.nominal / snapshot1.value

                text.text = result.toString()

                true
            } else
                false
        }


    }


}


