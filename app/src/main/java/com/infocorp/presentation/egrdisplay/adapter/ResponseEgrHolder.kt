package com.infocorp.presentation.egrdisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.R
import com.infocorp.databinding.ItemResponseEgrFormaLayoutBinding
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
            val defaultColor = ContextCompat.getColor(itemView.context, R.color.unknown_text_color)
            val normalColor = ContextCompat.getColor(itemView.context, R.color.text_color_normal)

            val name = responseEgr.titleCorp
            val fio = responseEgr.fioPerson
            val type = responseEgr.type
            val direction = responseEgr.direction
            val unp = responseEgr.unp
            val address = responseEgr.address
            val status = responseEgr.status


            if (name.isEmpty()) {
                tvEgrName.text = defaultValue
                tvEgrName.setTextColor(defaultColor)
            } else {
                tvEgrName.text = name
                tvEgrName.setTextColor(normalColor)
            }
            if (address.isEmpty()) {
                tvEgrAddress.text = defaultValue
                tvEgrAddress.setTextColor(defaultColor)
            } else {
                tvEgrAddress.text = address
                tvEgrAddress.setTextColor(normalColor)
            }
            if (fio.isEmpty()) {
                tvEgrFio.text = defaultValue
                tvEgrFio.setTextColor(defaultColor)
            } else {
                tvEgrFio.text = fio
                tvEgrFio.setTextColor(normalColor)
            }
            if (type.isEmpty()) {
                tvEgrType.text = defaultValue
                tvEgrType.setTextColor(defaultColor)
            } else {
                tvEgrType.text = type
                tvEgrType.setTextColor(normalColor)
            }
            if (direction.isEmpty()) {
                tvEgrDirection.text = defaultValue
                tvEgrDirection.setTextColor(defaultColor)
            } else {
                tvEgrDirection.text = direction
                tvEgrDirection.setTextColor(normalColor)
            }
            if (unp.isEmpty()) {
                tvEgrUnp.text = defaultValue
                tvEgrUnp.setTextColor(defaultColor)
            } else {
                tvEgrUnp.text = unp
                tvEgrUnp.setTextColor(normalColor)
            }
            if (status.isEmpty()) {
                tvEgrStatus.text = defaultValue
                tvEgrStatus.setTextColor(defaultColor)
            } else {
                tvEgrStatus.text = status
                tvEgrStatus.setTextColor(normalColor)
            }
        }
    }
}