package com.nauka.cftmoney.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nauka.cftmoney.model.Repository
import com.nauka.cftmoney.model.dto.Main
import dagger.hilt.android.lifecycle.HiltViewModel
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

    /*Объявляем переменную commonData типа MutableLiveData<Main?>
    в которую после чтения из базы или из сети попадают полученные данные*/
    var commonData = MutableLiveData<Main?>()

    //Объявляем переменную timer для периодического обновления данных в recyclerView
    private var timer: Timer? = null

    //При инициализации
    init {
        //В другом потоке (не в основном)
        viewModelScope.launch(Dispatchers.IO) {
            //Проверяем подключение к интернету
            if (checkInternetConnection(200)) {
                //Если соединение есть - загружаем данные из сети
                apiLoadInfo()
            } else {
                //Если соединения нет - загружаем данные из базы данных
                roomLoadInfo()
            }
        }
    }

    //Загружаем данные из сети с помощью retrofit
    fun apiLoadInfo() {
        //Не в оновном потоке
        viewModelScope.launch(Dispatchers.IO) {
            //Запрашиваем данные из сети
            repository.getAPI()?.awaitResponse().let {
                val db = repository.getDatabase().getAll()
                //Если полученные данные не равны null
                if (it != null) {
                    //Если ответ пришел успешный и полученный код равен - 200
                    if (it.isSuccessful && it.code() == 200) {
                        //Записываем полученные данные в переменную commonData
                        commonData.postValue(it.body())
                        //Если в базе данных есть поле date и оно не равно null
                        if (db?.date != null) {
                            //Обновляем данные в базе данных теми данными что получили при запросу
                            repository.getDatabase().update(it.body())
                        } else {
                            //Иначе добавлеяем эти данные в базу данных (кэшируем)
                            repository.getDatabase().insertAll(it.body())
                        }
                    }
                }
            }
        }
    }

    //Загружаем данные из локальной базы данных room
    fun roomLoadInfo() {
        //Выполняем запрос не в основном потоке
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDatabase().let {
                //Полученные данные записываем в переменную commonData
                commonData.postValue(it.getAll())
            }
        }
    }

    /*Проверяем подлючение к интернету
    Только проверка не просто наличия интернета, а именно подключения
    Взято с просторов интернета - принцип работы пока не очень ясен
    Понимаю то что проверка происходит путем пинга определенного хоста
    по определенному порту и возвращает
    true -> если ответ от хоста получен
    false -> если ответа нет*/
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

    //Включение автообновления
    fun startAutoUpdate() {
        //Переменная с временем для интервала
        val periodTime: Long = 60 * 1000 // 1 minute
        //Создаем экземпляр Timer
        timer = Timer()
        //Запускаем задачу на выполнение
        timer?.schedule(CustomTask(), 1000, periodTime)
    }

    //Остановка автообновления
    fun stopAutoUpdate() {
        //Удостоверимся что timer не null
        timer?.let {
            //Останавливаем timer
            it.cancel()
            //Обнуляем переменную
            timer = null
        }
    }

    //Задача на обновление данных в recyclerView
    private inner class CustomTask : TimerTask() {
        override fun run() {
            //Не в основном потоке
            viewModelScope.launch(Dispatchers.IO) {
                //Проверяем подлючение к сети
                if (checkInternetConnection(200)) {
                    //Если да - то обновляем данные из сети
                    apiLoadInfo()
                } else {
                    //Если нет - обновляем данные из базы данных
                    roomLoadInfo()
                }
            }
        }
    }

}

