package com.infocorp.presentation.resumestatedisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infocorp.R
import com.infocorp.databinding.ItemResumeBinding
import com.infocorp.domain.model.ResumeState
import com.infocorp.utils.Constants

class ResumeStateHolder(private val binding: ItemResumeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ResumeStateHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val resumeItemBinding = ItemResumeBinding.inflate(layoutInflater)

            return ResumeStateHolder(resumeItemBinding)
        }
    }

    fun bind(
        resumeState: ResumeState,
        onClickButtonSave: ((ResumeState) -> Unit)?,
        onClick: ((ResumeState) -> Unit)?
    ) {

        with(binding) {
            Glide.with(ivPosterResume)
                .load(resumeState.poster)
                .placeholder(R.drawable.corp_list_logo)
                .into(ivPosterResume)

            tvTitleText.text = resumeState.title
            tvSentData.text = resumeState.dateSent
            etResponseData.setText(resumeState.dateResponse)
            etNotesInput.setText(resumeState.notes)

            when (resumeState.result) {
                Constants.NO_ANSWER.value.toInt() -> rbtnNoAnswer.isChecked = true
                Constants.REJECT.value.toInt() -> rbtnReject.isChecked = true
                Constants.INVITE.value.toInt() -> rbtnInvite.isChecked = true
            }

            var result = 0

            rbtnNoAnswer.setOnClickListener {
                result = Constants.NO_ANSWER.value.toInt()
            }
            rbtnReject.setOnClickListener {
                result = Constants.REJECT.value.toInt()
            }
            rbtnInvite.setOnClickListener {
                result = Constants.INVITE.value.toInt()
            }

            btnSaveNotes.setOnClickListener {
                val notes = etNotesInput.text.toString()
                val dateResponse = etResponseData.text.toString()

                val resume = resumeState.copy(
                    result = result,
                    notes = notes,
                    dateResponse = dateResponse)

                onClickButtonSave?.invoke(resume)


            }

            itemView.setOnClickListener {
                onClick?.invoke(resumeState)
            }
        }
    }
}