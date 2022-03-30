package com.nauka.cftmoney

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nauka.cftmoney.databinding.ValuteItemBinding

class RecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private var changedData: MutableList<CurrencyModel> = mutableListOf()

    inner class MyViewHolder(val binding: ValuteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ValuteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(changedData[position]) {
                binding.nameTV.text = this.name
                binding.charcodeTV.text = this.charCode
                binding.nominalTV.text = this.nominal.toString()
                binding.previousTV.text = this.previous.toString()
                binding.valueTV.text = this.value.toString() + " ₽"
            }
            binding.itemCV.setOnClickListener {
                when (binding.convertLayout.visibility) {
                    View.VISIBLE -> binding.convertLayout.visibility = View.GONE
                    View.GONE -> binding.convertLayout.visibility = View.VISIBLE
                }
            }
            binding.convertBtn.setOnClickListener {

                val result = binding.sumToConvertET.text.toString()
                    .toInt() * changedData[position].nominal / changedData[position].value
                binding.resultTV.text = result.toString()
            }
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