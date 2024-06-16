package com.infocorp.presentation.resumestatedisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infocorp.R
import com.infocorp.databinding.ItemResumeBinding
import com.infocorp.domain.model.ResumeState

class ResumeStateHolder(private val binding: ItemResumeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ResumeStateHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val resumeItemBinding = ItemResumeBinding.inflate(layoutInflater)

            return ResumeStateHolder(resumeItemBinding)
        }
    }

    fun bind(resumeState: ResumeState) {

        with(binding) {
            Glide.with(ivPosterResume)
                .load(resumeState.poster)
                .placeholder(R.drawable.corp_list_logo)
                .into(ivPosterResume)

            tvTitleText.text = resumeState.title
            tvSentData.text = resumeState.dateSent
            tvResponseData.text = resumeState.dateResponse
            etNotesInput.setText(resumeState.notes)

            when(resumeState.result){
                0 -> radioButton3.isChecked = true
                1 -> radioButton2.isChecked = true
                2 -> radioButton.isChecked = true
            }
        }
    }
}