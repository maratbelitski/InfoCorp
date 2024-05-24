package com.infocorp.presentation.settingsdisplay

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infocorp.R
import com.infocorp.databinding.FragmentSettingsBinding
import com.infocorp.presentation.MainActivity


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding:FragmentSettingsBinding
        get() = _binding ?: throw NullPointerException()


   lateinit var updateStateBottomMenu:(()->Unit)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) updateStateBottomMenu = { context.enableBottomMenu() }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        updateStateBottomMenu.invoke()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}