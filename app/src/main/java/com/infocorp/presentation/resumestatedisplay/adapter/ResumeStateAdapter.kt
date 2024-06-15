package com.infocorp.presentation.resumestatedisplay.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.infocorp.domain.model.ResumeState

class ResumeStateAdapter : ListAdapter<ResumeState, ViewHolder>(ResumeStateDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ResumeStateHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resumeState = getItem(position)

        if (resumeState is ResumeState && holder is ResumeStateHolder) {
            holder.bind(resumeState)
        }
    }
}