package com.infocorp.presentation.egrdisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
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
        binding.tvEgrName.text = responseEgr.titleCorp
         binding.tvEgrFio.text = responseEgr.fioPerson
         binding.tvEgrType.text = responseEgr.type
         binding.tvEgrDirection.text = responseEgr.direction
         binding.tvEgrUnp.text = responseEgr.unp
         binding.tvEgrAddress.text = responseEgr.address
         binding.tvEgrStatus.text = responseEgr.status
    }
}