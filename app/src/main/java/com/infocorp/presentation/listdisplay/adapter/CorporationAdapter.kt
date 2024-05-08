package com.infocorp.presentation.listdisplay.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.infocorp.domain.model.Corporation

class CorporationAdapter : ListAdapter<Corporation, ViewHolder>(CorporationDiffUtils()) {
    companion object {
        const val FAVOURITE = 1
        const val NO_FAVOURITE = 0
    }

    var onLongClick: ((Corporation) -> Unit)? = null
    var onClick: ((Corporation) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item.isFavourite) {
            true -> FAVOURITE
            false -> NO_FAVOURITE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NO_FAVOURITE -> CorporationHolder.from(parent)
            FAVOURITE -> CorporationFavoriteHolder.from(parent)
            else -> throw IllegalStateException("Error in Corporation adapter")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corporation = getItem(position)

        if (corporation is Corporation && holder is CorporationHolder) {
            holder.bind(corporation, onLongClick, onClick)
        } else if (corporation is Corporation && holder is CorporationFavoriteHolder) {
            holder.bind(corporation, onLongClick, onClick)
        }
    }
}