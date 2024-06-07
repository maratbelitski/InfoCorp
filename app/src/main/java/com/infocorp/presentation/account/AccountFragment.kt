package com.infocorp.presentation.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.infocorp.R
import com.infocorp.databinding.FragmentAccountBinding
import com.infocorp.presentation.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding ?: throw NullPointerException()

    private val fragmentViewModel: AccountViewModel by viewModels()

    private var updateStateBottomMenu: (() -> Unit)? = null

    private val firebase by lazy {
        fragmentViewModel.getFirebase()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).enableBottomMenu() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        onObservers()
        onListeners()
    }

    private fun onListeners() {
        with(binding) {
            userEmailTypeCard.btnCreate.setOnClickListener {
                val header = userEmailTypeCard.etDescriptionInput.text.toString()
                val content = userEmailTypeCard.etContentInput.text.toString()
                val link = userEmailTypeCard.etLinkCvInput.text.toString()

                val emptiesFields = onCheckEmptyFields()
                if (!emptiesFields) {
                    fragmentViewModel.createUserCv(header, content, link)
                    clearFields()
                }
            }

            userEmailTypeCard.tvTitleCvText.setOnClickListener {
                val title = userEmailTypeCard.tvTitleCvText.text.toString()
                userEmailTypeCard.etDescriptionInput.setText(title)
            }

            userEmailTypeCard.tvDescriptionCvText.setOnClickListener {
                val description = userEmailTypeCard.tvDescriptionCvText.text.toString()
                userEmailTypeCard.etContentInput.setText(description)
            }

            accountCard.btnLogOut.setOnClickListener {
                fragmentViewModel.logOut()

                val action = AccountFragmentDirections
                    .actionAccountFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
    }

    private fun onObservers() {
        lifecycleScope.launch {
            fragmentViewModel.headerText
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.userEmailTypeCard.tvTitleCvText.text = it
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.contentText
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.userEmailTypeCard.tvDescriptionCvText.text = it
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.linkText
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.userEmailTypeCard.tvLinkCvText.text = it
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.currentAccount
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.accountCard.tvCurrentAccount.text = it
                }
        }
    }

    private fun onCheckError(header: String, content: String, link: String) {
        with(binding) {
            val headerLayout = userEmailTypeCard.etCnangeHeaderCvText
            val contentLayout = userEmailTypeCard.etCnangeContentCvText
            val linkLayout = userEmailTypeCard.etCnangeLinkCvText

            headerLayout.error = resources.getString(R.string.error_input_layout)
            contentLayout.error = resources.getString(R.string.error_input_layout)
            linkLayout.error = resources.getString(R.string.error_input_layout)

            fragmentViewModel.validationError(header, headerLayout)
            fragmentViewModel.validationError(content, contentLayout)
            fragmentViewModel.validationError(link, linkLayout)
        }
    }

    private fun onCheckEmptyFields(): Boolean {
        with(binding) {
            val header = userEmailTypeCard.etDescriptionInput.text.toString()
            val content = userEmailTypeCard.etContentInput.text.toString()
            val link = userEmailTypeCard.etLinkCvInput.text.toString()

            onCheckError(header, content, link)

            return !(header.isNotEmpty() && content.isNotEmpty() && link.isNotEmpty())
        }
    }

    private fun clearFields() {
        with(binding) {
            userEmailTypeCard.etDescriptionInput.text?.clear()
            userEmailTypeCard.etContentInput.text?.clear()
            userEmailTypeCard.etLinkCvInput.text?.clear()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}