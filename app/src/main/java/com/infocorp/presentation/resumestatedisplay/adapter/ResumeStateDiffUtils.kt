package com.infocorp.presentation.resumestatedisplay.adapter

import androidx.recyclerview.widget.DiffUtil
import com.infocorp.domain.model.ResumeState

class ResumeStateDiffUtils : DiffUtil.ItemCallback<ResumeState>() {
    override fun areItemsTheSame(oldItem: ResumeState, newItem: ResumeState): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResumeState, newItem: ResumeState): Boolean {
        return oldItem == newItem
    }
}