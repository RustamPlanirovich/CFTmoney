package com.nauka.cftmoney.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.nauka.cftmoney.databinding.ValuteItemBinding
import com.nauka.cftmoney.model.dto.CurrencyModel

class RecyclerAdapter(
    val context: Context,
    private val listener: MainActivity
) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private var changedData: MutableList<CurrencyModel> = mutableListOf()


    class MyViewHolder(val binding: ValuteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(snapshot: CurrencyModel, listener: MainActivity) {
            binding.itemCV.setOnClickListener {
                listener.onItemsSelected(snapshot,binding.sumToConvertET, binding.resultTV, binding.convertLayout)
            }


            binding.nameTV.text = snapshot.name
            binding.charcodeTV.text = snapshot.charCode
            binding.nominalTV.text = "Nominal " + snapshot.nominal.toString()
            binding.previousTV.text = snapshot.previous.toString()
            binding.valueTV.text = snapshot.value.toString() + " ₽"

        }
    }

    interface AdapterListener {
        fun onItemsSelected(
            snapshot1: CurrencyModel,
            snapshot: EditText,
            text: TextView,
            convertLayout: ConstraintLayout
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
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
    fun setValuteListItems(valuteList: MutableList<CurrencyModel>) {
        changedData.clear()
        changedData.addAll(valuteList)
        notifyDataSetChanged()
    }


}