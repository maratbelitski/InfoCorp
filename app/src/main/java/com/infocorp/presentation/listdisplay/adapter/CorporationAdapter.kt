package com.infocorp.presentation.listdisplay.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.infocorp.domain.model.Corporation

class CorporationAdapter : ListAdapter<Corporation, ViewHolder>(CorporationDiffUtils()) {
    companion object {
        const val NEW_CORP = 2
        const val FAVOURITE = 1
        const val NORMAL = 0
    }

    var onLongClick: ((Corporation) -> Unit)? = null
    var onClick: ((Corporation) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        Log.i("MyLog", "getItemViewType ${item.isNew}")
        return when (item.isFavourite) {
            true -> FAVOURITE
            false -> {
                if (item.isNew) {
                    NEW_CORP
                } else {
                    NORMAL
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("MyLog", "onCreateViewHolder $viewType")
        return when (viewType) {
            NEW_CORP -> CorporationNewHolder.from(parent)
            NORMAL -> CorporationHolder.from(parent)
            FAVOURITE -> CorporationFavoriteHolder.from(parent)
            else -> throw IllegalStateException("Error in Corporation adapter")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corporation = getItem(position)
        Log.i("MyLog", "onBindViewHolder $holder")
        if (corporation is Corporation && holder is CorporationHolder) {
            holder.bind(corporation, onLongClick, onClick)
        } else if (corporation is Corporation && holder is CorporationFavoriteHolder) {
            holder.bind(corporation, onLongClick, onClick)
        } else if (corporation is Corporation && holder is CorporationNewHolder) {
            holder.bind(corporation, onLongClick, onClick)
        }
    }
}