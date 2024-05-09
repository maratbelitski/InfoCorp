package com.infocorp.presentation.egrdisplay.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.infocorp.domain.model.Data

class ResponseEgrAdapter : ListAdapter<Data, ViewHolder>(ResponseEgrDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ResponseEgrHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = getItem(position)

        if (response is Data && holder is ResponseEgrHolder) {
            holder.bind(response)
        }
    }
}