package com.infocorp.presentation.listdisplay.adapter

import androidx.recyclerview.widget.DiffUtil
import com.infocorp.domain.entity.Corporation

class CorporationDiffUtils : DiffUtil.ItemCallback<Corporation>() {
    override fun areItemsTheSame(oldItem: Corporation, newItem: Corporation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Corporation, newItem: Corporation): Boolean {
        return oldItem == newItem
    }
}