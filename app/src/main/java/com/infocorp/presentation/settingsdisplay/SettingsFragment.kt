package com.infocorp.presentation.settingsdisplay

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import com.infocorp.R
import com.infocorp.databinding.FragmentSettingsBinding
import com.infocorp.presentation.Constants
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.detaildisplay.DetailCorporationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: throw NullPointerException()

    private val fragmentViewModel: SettingsFragmentViewModel by viewModels()
    lateinit var updateStateBottomMenu: (() -> Unit)
    lateinit var initThemParams: (() -> Unit)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) updateStateBottomMenu = { context.enableBottomMenu() }
        if (context is MainActivity) initThemParams = { context.onInitThemeParams() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        updateStateBottomMenu.invoke()
        initThemParams.invoke()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCheckThem()
        onListeners()
    }

    private fun onCheckThem() {
        when (resources.configuration.uiMode) {
            Constants.LIGHT_MODE.value.toInt() -> binding.themeCard.rbtnLight.isChecked = true
            Constants.NIGHT_MODE.value.toInt() -> binding.themeCard.rbtnDark.isChecked = true
        }
    }

    private fun onListeners() {
        binding.themeCard.rbtnDark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            fragmentViewModel.setThemeParams(Constants.NIGHT_MODE.value)
        }

        binding.themeCard.rbtnLight.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            fragmentViewModel.setThemeParams(Constants.LIGHT_MODE.value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}