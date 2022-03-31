package com.nauka.cftmoney.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.nauka.cftmoney.databinding.ValuteItemBinding
import com.nauka.cftmoney.model.dto.CurrencyModel

class RecyclerAdapter(
    private val listener: MainActivity
) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    //Переменная в которую полчаем данные для отображение
    private var changedData: MutableList<CurrencyModel> = mutableListOf()


    class MyViewHolder(val binding: ValuteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(snapshot: CurrencyModel, listener: MainActivity) {
            binding.itemCV.setOnClickListener {
                //Передаем в метод onItemsSelected нашего listener
                listener.onItemsSelected(
                    snapshot, //Данные из выбранного пункта
                    binding.sumToConvertET, //Экземпляр sumToConvertET из данного пункта
                    binding.resultTV, //Экземпляр resultTV из данного пункта
                    binding.convertLayout) //Экземпляр convertLayout из данного пункта
            }
            //Устанавливаем данные в view представления valute_item
            binding.nameTV.text = snapshot.name
            binding.charcodeTV.text = snapshot.charCode
            binding.nominalTV.text = "Nominal " + snapshot.nominal.toString()
            binding.previousTV.text = snapshot.previous.toString()
            binding.valueTV.text = snapshot.value.toString() + " ₽"

        }
    }

    //Интерфейс listener
    interface AdapterListener {
        fun onItemsSelected(
            snapshot1: CurrencyModel, //Данные из выбранного пункта
            snapshot: EditText, //Экземпляр sumToConvertET из данного пункта
            text: TextView, //Экземпляр resultTV из данного пункта
            convertLayout: ConstraintLayout //Экземпляр convertLayout из данного пункта
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //Создаем binding valute_item
        val binding =
            ValuteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        changedData[position]?.let { snapshot ->
            holder.bind(
                changedData[position],
                listener
            )
        }
    }


    override fun getItemCount(): Int {
        return changedData.size
    }


    @SuppressLint("NotifyDataSetChanged")
    //Получаем данный для отображения
    fun setValuteListItems(valuteList: MutableList<CurrencyModel>) {
        //Предварительно очищаем changedData
        changedData.clear()
        //Помещаем полученные данные в changedData
        changedData.addAll(valuteList)
        //Обновляем представление recyclerView
        notifyDataSetChanged()
    }


}