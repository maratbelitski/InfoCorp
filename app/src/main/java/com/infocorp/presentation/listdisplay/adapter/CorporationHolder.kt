package com.infocorp.presentation.listdisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.R
import com.infocorp.databinding.ItemListCorporationBinding
import com.infocorp.domain.model.Corporation

class CorporationHolder(private val binding: ItemListCorporationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): CorporationHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val corporationItemBinding = ItemListCorporationBinding.inflate(layoutInflater)

            return CorporationHolder(corporationItemBinding)
        }
    }

    fun bind(
        corporation: Corporation,
        longClick: ((Corporation) -> Unit)?,
        onClick: ((Corporation) -> Unit)?
    ) {
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