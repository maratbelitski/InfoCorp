package com.infocorp.presentation.settingsdisplay

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.infocorp.R
import com.infocorp.databinding.FragmentSettingsBinding
import com.infocorp.utils.Constants
import com.infocorp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        if (context is MainActivity) initLanguageParams = { context.onInitLanguage() }
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

        onObservers()
        onCheckRadioButtons()
        onListeners()
    }

    private fun onObservers() {
        lifecycleScope.launch {
            fragmentViewModel.headerText
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.cvCard.tvTitleCvText.text = it
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.contentText
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.cvCard.tvDescriptionCvText.text = it
                }
        }
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

            cvCard.btnCreate.setOnClickListener {
                val header = cvCard.etDescriptionInput.text.toString()
                val content = cvCard.etContentInput.text.toString()

                val emptiesFields = onCheckEmptyFields()
                if (!emptiesFields){
                    fragmentViewModel.createUserCv(header, content)
                    clearFields()
                }
            }

            cvCard.tvTitleCvText.setOnClickListener {
                cvCard.etDescriptionInput.setText(cvCard.tvTitleCvText.text.toString())
            }

            cvCard.tvDescriptionCvText.setOnClickListener {
                cvCard.etContentInput.setText(cvCard.tvDescriptionCvText.text.toString())
            }
        }
    }

    private fun onCheckError(header: String, content: String) {
        with(binding) {
            val headerLayout = cvCard.etCnangeHeaderCvText
            val contentLayout = cvCard.etCnangeContentCvText

            headerLayout.error = resources.getString(R.string.error_input_layout)
            contentLayout.error = resources.getString(R.string.error_input_layout)

            fragmentViewModel.validationError(header, headerLayout)
            fragmentViewModel.validationError(content, contentLayout)
        }
    }
    private fun onCheckEmptyFields(): Boolean {
        with(binding) {
            val header = cvCard.etDescriptionInput.text.toString()
            val content = cvCard.etContentInput.text.toString()

            onCheckError(header, content)

            return !(header.isNotEmpty() && content.isNotEmpty())
        }
    }
    private fun clearFields() {
        binding.cvCard.etDescriptionInput.text?.clear()
        binding.cvCard.etContentInput.text?.clear()
    }

    override fun onPause() {
        super.onPause()
        clearFields()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}