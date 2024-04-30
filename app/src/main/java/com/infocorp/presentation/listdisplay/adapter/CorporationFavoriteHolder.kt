package com.infocorp.presentation.listdisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.databinding.FavouriteItemListCorporationBinding
import com.infocorp.domain.entity.Corporation

class CorporationFavoriteHolder(private val binding: FavouriteItemListCorporationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): CorporationFavoriteHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val corporationItemBinding = FavouriteItemListCorporationBinding.inflate(layoutInflater)

            return CorporationFavoriteHolder(corporationItemBinding)
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