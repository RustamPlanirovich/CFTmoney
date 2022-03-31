package com.nauka.cftmoney.util

import androidx.recyclerview.widget.DiffUtil
import com.nauka.cftmoney.model.dto.CurrencyModel

class ActorDiffCallback(
    private val oldList: List<CurrencyModel>,
    private val newList: List<CurrencyModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (value, nominal, previous, charCode, id, name, numCode) = oldList[oldItemPosition]
        val (valueN, nominalN, previousN, charCodeN, idN, nameN, numCodeN) = newList[newItemPosition]
        return value == valueN
                && nominal == nominalN
                && previous == previousN
                && charCode == charCodeN
                && id == idN
                && name == nameN
                && numCode == numCodeN
    }

}
