package com.infocorp.presentation.listdisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.databinding.ItemListCorporationBinding
import com.infocorp.databinding.NewItemListCorporationBinding
import com.infocorp.databinding.UserItemListCorporationBinding
import com.infocorp.domain.model.Corporation

class UserCorporationHolder(private val binding: UserItemListCorporationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): UserCorporationHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val corporationItemBinding = UserItemListCorporationBinding.inflate(layoutInflater)

            return UserCorporationHolder(corporationItemBinding)
        }
    }

    fun bind(corporation: Corporation, longClick: ((Corporation)-> Unit)?, onClick:((Corporation)->Unit)?) {
        binding.tvName.text = corporation.name

        itemView.setOnLongClickListener {
            longClick?.invoke(corporation)
            true
        }

        itemView.setOnClickListener {
            onClick?.invoke(corporation)
        }
    }
}