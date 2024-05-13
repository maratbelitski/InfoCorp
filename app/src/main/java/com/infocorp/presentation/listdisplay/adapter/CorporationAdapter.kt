package com.infocorp.presentation.listdisplay.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.infocorp.domain.model.Corporation
import com.infocorp.presentation.egrdisplay.adapter.ResponseEgrHolder

class CorporationAdapter : ListAdapter<Corporation, ViewHolder>(CorporationDiffUtils()) {
    companion object {
        private const val USER_CORPORATION = "USER_CORPORATION"
        const val USER_CORP = 3
        const val NEW_CORP = 2
        const val FAVOURITE = 1
        const val NORMAL = 0
    }

    var onLongClick: ((Corporation) -> Unit)? = null
    var onClick: ((Corporation) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item.idFirebase == USER_CORPORATION) {
            true -> USER_CORP
            false -> {
                if (item.isFavourite) {
                    FAVOURITE
                } else if (item.isNew){
                    NEW_CORP
                } else {
                    NORMAL
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            USER_CORP -> UserCorporationHolder.from(parent)
            NEW_CORP -> CorporationNewHolder.from(parent)
            NORMAL -> CorporationHolder.from(parent)
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
        } else if (corporation is Corporation && holder is CorporationNewHolder) {
            holder.bind(corporation, onLongClick, onClick)
        }else if (corporation is Corporation && holder is UserCorporationHolder) {
            holder.bind(corporation, onLongClick, onClick)
        }
    }
}