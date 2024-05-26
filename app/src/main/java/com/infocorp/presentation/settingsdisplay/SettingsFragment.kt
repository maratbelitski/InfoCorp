package com.infocorp.presentation.settingsdisplay

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.infocorp.databinding.FragmentSettingsBinding
import com.infocorp.utils.Constants
import com.infocorp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: throw NullPointerException()

    private val fragmentViewModel: SettingsFragmentViewModel by viewModels()

     private lateinit var updateStateBottomMenu: (() -> Unit)
     private lateinit var initThemParams: (() -> Unit)
     private lateinit var initLanguageParams: (() -> Unit)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) updateStateBottomMenu = { context.enableBottomMenu() }
        if (context is MainActivity) initThemParams = { context.onInitThemeParams() }
        if (context is MainActivity) initLanguageParams = {context.onInitLanguage()}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        updateStateBottomMenu.invoke()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCheckRadioButtons()
        onListeners()
    }

    private fun onCheckRadioButtons() {
        when (resources.configuration.uiMode) {
            Constants.LIGHT_MODE.value.toInt() -> binding.themeCard.rbtnLight.isChecked = true
            Constants.NIGHT_MODE.value.toInt() -> binding.themeCard.rbtnDark.isChecked = true
        }

        when (Locale.getDefault().language.toString()) {
            Constants.LANG_RU.value -> binding.languageCard.rbtnRus.isChecked = true
            Constants.LANG_EN.value -> binding.languageCard.rbtnEng.isChecked = true
        }
    }

    private fun onListeners() {
        with(binding) {
            themeCard.rbtnDark.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                fragmentViewModel.setThemeParams(Constants.NIGHT_MODE.value)
            }

            themeCard.rbtnLight.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                fragmentViewModel.setThemeParams(Constants.LIGHT_MODE.value)
            }

            languageCard.rbtnEng.setOnClickListener {
                fragmentViewModel.setLanguageParams(Constants.LANG_EN.value)
                initLanguageParams.invoke()
            }

            languageCard.rbtnRus.setOnClickListener {
                fragmentViewModel.setLanguageParams(Constants.LANG_RU.value)
                initLanguageParams.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}