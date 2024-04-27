package com.infocorp.presentation.listdisplay.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.infocorp.domain.Corporation

class CorporationAdapter : ListAdapter<Corporation, ViewHolder>(CorporationDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CorporationHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val corporation = getItem(position)

        if (corporation is Corporation && holder is CorporationHolder) {
            holder.bind(corporation)
        }
    }
}