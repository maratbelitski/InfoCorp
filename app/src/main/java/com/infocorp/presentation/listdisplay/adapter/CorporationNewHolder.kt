package com.infocorp.presentation.listdisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.R
import com.infocorp.databinding.NewItemListCorporationBinding
import com.infocorp.domain.model.Corporation

class CorporationNewHolder(private val binding: NewItemListCorporationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): CorporationNewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val corporationItemBinding = NewItemListCorporationBinding.inflate(layoutInflater)

            return CorporationNewHolder(corporationItemBinding)
        }
    }

    fun bind(corporation: Corporation, longClick: ((Corporation)-> Unit)?, onClick:((Corporation)->Unit)?) {
        binding.tvName.text = corporation.name

        when(corporation.resumeState){
            0 -> binding.imageResumeState.setImageResource(R.drawable.resume_grey)
            1 -> binding.imageResumeState.setImageResource(R.drawable.resume_red)
            2 -> binding.imageResumeState.setImageResource(R.drawable.resume_green)
            3 -> binding.imageResumeState.setImageResource(R.drawable.resume_yellow)
        }

        itemView.setOnLongClickListener {
            longClick?.invoke(corporation)
            true
        }

        itemView.setOnClickListener {
           onClick?.invoke(corporation)
        }
    }
}