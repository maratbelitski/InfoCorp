package com.infocorp.presentation.egrdisplay.adapter

import androidx.recyclerview.widget.DiffUtil
import com.infocorp.domain.model.Data

class ResponseEgrDiffUtils : DiffUtil.ItemCallback<Data>() {
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem.unp == newItem.unp
    }

    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }
}