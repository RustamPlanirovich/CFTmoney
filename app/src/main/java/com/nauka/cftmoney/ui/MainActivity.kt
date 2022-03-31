package com.nauka.cftmoney.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import com.nauka.cftmoney.model.dto.CurrencyModel
import com.nauka.cftmoney.util.parseDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RecyclerAdapter.AdapterListener {

    // Ленивая инициализация RecyclerAdapter
    lateinit var recyclerAdapter: RecyclerAdapter

    /*Объявляем Binding layout'а MainActivity через вызов get
    * переменной _binding (чтобы его нельзя было изменить из вне)*/
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    /*Объявляем viewModel через делегат (пока плохо разобрался что такое делегат)
    Знаю что это нужно чтобы при повторном создании представления получал один и тот же
    экземпляр viewModel*/
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Инициализация _binding
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Инициализация recyclerAdapter
        recyclerAdapter = RecyclerAdapter(this)


        /*Объявляем массив куда будет загружать данные полученные из
        сетевого запроса или из базы данных*/
        val callbackList = ArrayList<CurrencyModel>()

        /*считываем сохраненное состояние autoUpdateCB который отвечает
        за автообновление данных в recyclerView*/
        val isChecked = this.getPreferences(Context.MODE_PRIVATE).getBoolean(
            getString(R.string.checkbox_pref), false
        )

        /*Устанавливаем считанное состояние autoUpdateCB*/
        binding.autoUpdateCB.isChecked = isChecked

        /*Проверяем, если занчение autoUpdateCB
        * true -> включаем автообновление
        * false -> выключаем автообновление*/
        binding.autoUpdateCB.setOnCheckedChangeListener { _, isCheckedL ->
            if (isCheckedL) {
                viewModel.startAutoUpdate()
            } else {
                viewModel.stopAutoUpdate()
            }
        }

        /*Получение данных из viewModel и присвоение их recyclerAdapter*/
        viewModel.commonData.observe(this) { body ->
            //Проверяем что полученное тело не пустое
            if (body != null) {
                //Предварительно очищаем callbackList для новой записи
                callbackList.clear()
                //Получаем данные конкретного поля valute который является HashMap
                for (value in body.valute.entries) {
                    //Записываем только значения
                    callbackList.add(value.value)
                }
                body.apply {
                    /*Устанавливаем даны и время проводя их конвертацию с помощью
                    функции расширения parseDate*/
                    binding.currentDateTV.text = this.date.parseDate()
                    binding.previousDateTV.text = this.previousDate?.parseDate()
                    binding.timestampTV.text = this.timestamp?.parseDate()
                }
                //Присваиваем recyclerView layoutManager и adapter
                binding.recyclerview.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = recyclerAdapter
                }
                //Отправляем полученные данные callbackList в адаптер
                recyclerAdapter.setValuteListItems(callbackList)
            }
        }

        /*При свайпе сверху -> вниз по recyclerView происходит обновление данных*/
        binding.swipeUpdate.setOnRefreshListener {
            //Производим проверку на наличие подключения интернета не в основном потоке
            CoroutineScope(Dispatchers.IO).launch {
                //Если подключение есть
                if (viewModel.checkInternetConnection(200)) {
                    //В основном потоке
                    withContext(Dispatchers.Main) {
                        //Выполняем загрузку данных из сети
                        viewModel.apiLoadInfo()
                        //После загрузки останавливаем анимацию обновления
                        binding.swipeUpdate.isRefreshing = false
                    }
                } else {
                    //Если подключения нет
                    withContext(Dispatchers.Main) {
                        /*Загружаем данные из базы данных
                        Не знаю зачем сделал - чтобы было*/
                        viewModel.roomLoadInfo()
                        //После загрузки останавливаем анимацию обновления
                        binding.swipeUpdate.isRefreshing = false
                    }
                }
            }
        }
    }

    //При старте активити
    override fun onStart() {
        super.onStart() //Проверка при старте приложения
        if (binding.autoUpdateCB.isChecked) //Если установлен чек-бокс то включается автообновление
            viewModel.startAutoUpdate()
    }

    //При остановке активити
    override fun onStop() {
        super.onStop() //Проверка при остановке приложения
        viewModel.stopAutoUpdate() //Выключение автообновления
    }

    //При переходе приложения в состояние паузы - то есть не на переднем плане
    override fun onPause() {
        super.onPause()
        /*Создаем переменную pref и сохраняем в нее состояние autoUpdateCB чекбокса
        * который отвечает за автообновление данных*/
        val pref = this.getPreferences(Context.MODE_PRIVATE)
        with(pref.edit()) {
            putBoolean(getString(R.string.checkbox_pref), binding.autoUpdateCB.isChecked)
            apply()
        }
    }

    //При уничтожении активити обнуляем _binding (представление активити)
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /*Функция отвечающая за клик по пунктам recyclerView*/
    override fun onItemsSelected(
        currency: CurrencyModel, // данные из пункта по которому кликнули
        editText: EditText, // экземпляр EditText
        textView: TextView, // экземпляр TextView
        convertLayout: ConstraintLayout // экземпляр ConstraintLayout
    ) {

        /*Если при клике по пункту слой который отвечает за конвертацию */
        when (convertLayout.visibility) {
            //видим -> то делаем его не видимым
            View.VISIBLE -> {
                convertLayout.visibility = View.GONE
            }
            //не видим -> делаем его видимым
            View.GONE -> {
                convertLayout.visibility = View.VISIBLE
                //Очищаем поле ввода
                editText.text.clear()
                //Обнуляем textView
                textView.text = "0"
            }
        }
        /*После ввода данных в editText и нажатии Enter либо клавиши назад
        * происходит конвертация валюты*/
        editText.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                //Получаем результат конвертации
                val result = editText.text.toString()
                    .toInt() * currency.nominal / currency.value
                //Присваеиваем полученный результат textView
                textView.text = result.toString()

                true
            } else
                false
        }


    }


}


