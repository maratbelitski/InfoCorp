package com.infocorp.presentation.egrdisplay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import com.infocorp.R
import com.infocorp.databinding.ItemListCorporationBinding
import com.infocorp.databinding.ItemResponseEgrFormaLayoutBinding
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data

class ResponseEgrHolder(private val binding: ItemResponseEgrFormaLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ResponseEgrHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val corporationItemBinding = ItemResponseEgrFormaLayoutBinding.inflate(layoutInflater)

            return ResponseEgrHolder(corporationItemBinding)
        }
    }

    fun bind(responseEgr: Data) {
        with(binding) {

            val defaultValue = itemView.resources.getString(R.string.not_specified)
            val defaultColor = ContextCompat.getColor(itemView.context,R.color.unknown_text_color)

            var name = responseEgr.titleCorp
            var fio = responseEgr.fioPerson
            var type = responseEgr.type
            var direction = responseEgr.direction
            var unp = responseEgr.unp
            var address = responseEgr.address
            var status = responseEgr.status


            if (name.isEmpty()) {
                name = defaultValue
                tvEgrName.setTextColor(defaultColor)
            }
            if (address.isEmpty()) {
                address = defaultValue
                tvEgrAddress.setTextColor(defaultColor)
            }
            if (fio.isEmpty()) {
                fio = defaultValue
                tvEgrFio.setTextColor(defaultColor)
            }
            if (type.isEmpty()) {
                type = defaultValue
                tvEgrType.setTextColor(defaultColor)
            }
            if (direction.isEmpty()) {
                direction = defaultValue
                tvEgrDirection.setTextColor(defaultColor)
            }
            if (unp.isEmpty()) {
               unp = defaultValue
                tvEgrUnp.setTextColor(defaultColor)
            }
            if (status.isEmpty()) {
                status = defaultValue
                tvEgrStatus.setTextColor(defaultColor)
            }

            tvEgrName.text = name
            tvEgrFio.text = fio
            tvEgrAddress.text = address
            tvEgrType.text = type
            tvEgrDirection.text = direction
            tvEgrUnp.text = unp
            tvEgrStatus.text = status
        }
    }
}