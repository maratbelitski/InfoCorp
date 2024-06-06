package com.infocorp.presentation.settingsdisplay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.infocorp.databinding.FragmentSettingsBinding
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: throw NullPointerException()

    private val fragmentViewModel: SettingsFragmentViewModel by viewModels()

    private var updateStateBottomMenu: (() -> Unit)? = null
    private var initLanguageParams: (() -> Unit)? = null
    private var initThemeParams: (() -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).enableBottomMenu() }
        initLanguageParams = { (activity as MainActivity).onInitLanguage() }
        initThemeParams = { (activity as MainActivity).onInitThemeParams()}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        onCheckRadioButtonsTheme()
        onCheckRadioButtonsLang()
        onListeners()
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
    }

    private fun onCheckRadioButtonsTheme() {
        when (resources.configuration.uiMode) {
            Constants.LIGHT_MODE.value.toInt() -> binding.themeCard.rbtnLight.isChecked = true
            Constants.NIGHT_MODE.value.toInt() -> binding.themeCard.rbtnDark.isChecked = true
        }
    }

    private fun onCheckRadioButtonsLang() {
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
                initThemeParams?.invoke()
            }

            themeCard.rbtnLight.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                fragmentViewModel.setThemeParams(Constants.LIGHT_MODE.value)
                initThemeParams?.invoke()
            }

            languageCard.rbtnEng.setOnClickListener {
                fragmentViewModel.setLanguageParams(Constants.LANG_EN.value)
                initLanguageParams?.invoke()
            }

            languageCard.rbtnRus.setOnClickListener {
                fragmentViewModel.setLanguageParams(Constants.LANG_RU.value)
                initLanguageParams?.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}