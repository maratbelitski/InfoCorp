package com.infocorp.presentation.createusercorp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.infocorp.R
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.databinding.FragmentCreateUserCorporationBinding
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserCorporationFragment : Fragment() {

    private var _binding: FragmentCreateUserCorporationBinding? = null
    private val binding: FragmentCreateUserCorporationBinding
        get() = _binding ?: throw Exception()

    private val arguments: CreateUserCorporationFragmentArgs by navArgs()
    private val fragmentViewModel: CreateUserCorporationFragmentViewModel by viewModels()

    private var updateStateBottomMenu: (() -> Unit)? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).disableBottomMenu() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserCorporationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initArgs()
        onListeners()
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
    }

    private fun initArgs() {
        with(binding) {
            etPosterInput.setText(arguments.corporation.poster)
            etTittleInput.setText(arguments.corporation.name)
            etDescriptionInput.setText(arguments.corporation.description)
            etAddressInput.setText(arguments.corporation.address)
            etPhonesInput.setText(arguments.corporation.phones)
            etEmailInput.setText(arguments.corporation.email)
            etWebsiteInput.setText(arguments.corporation.website)
            etNotesInput.setText(arguments.corporation.notes)
        }
    }

    private fun onListeners() {
        binding.btnSend.setOnClickListener {
            val emptiesFields = onCheckEmptyFields()

            if (!emptiesFields) {
                sendUserCorporation()
                Toast.makeText(
                    requireContext(),
                    ContextCompat.getString(requireActivity(), R.string.send_to_developer),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnCreate.setOnClickListener {
            val emptiesFields = onCheckEmptyFields()
            if (!emptiesFields) {
                addUserCorporation()
                Toast.makeText(
                    requireContext(),
                    ContextCompat.getString(requireActivity(), R.string.saved_to_database),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnYourList.setOnClickListener {
            val action = CreateUserCorporationFragmentDirections
                .actionCreateUserCorporationFragmentToListUserCorporationsFragment()
            findNavController().navigate(action)
        }

        binding.btnClearFields.setOnClickListener {
            with(binding) {
                etPosterInput.text?.clear()
                etTittleInput.text?.clear()
                etDescriptionInput.text?.clear()
                etAddressInput.text?.clear()
                etPhonesInput.text?.clear()
                etEmailInput.text?.clear()
                etWebsiteInput.text?.clear()
                etNotesInput.text?.clear()

                etPoster.requestFocus()
            }
        }
    }

    private fun onCheckError(title: String, description: String, email: String) {
        with(binding) {
            val titleLayout = etTittle
            val descriptionLayout = etDescription
            val emailLayout = etEmail

            titleLayout.error = resources.getString(R.string.error_input_layout)
            descriptionLayout.error = resources.getString(R.string.error_input_layout)
            emailLayout.error = resources.getString(R.string.error_input_layout)

            fragmentViewModel.validationError(title, titleLayout)
            fragmentViewModel.validationError(description, descriptionLayout)
            fragmentViewModel.validationError(email, emailLayout)
        }
    }

    private fun addUserCorporation() {
        val newCorp = getValuesAndCreateCorp()
        fragmentViewModel.addUserCorporationToDataBase(newCorp)
    }

    private fun sendUserCorporation() {
        val newCorp = getValuesAndCreateCorp()
        fragmentViewModel.sendUserCorporation(newCorp)
    }

    private fun getValuesAndCreateCorp(): UserCorporationDto {
        with(binding) {
            val poster = etPosterInput.text.toString()
            val tittle = etTittleInput.text.toString()
            val descriptions = etDescriptionInput.text.toString()
            val address = etAddressInput.text.toString()
            val phones = etPhonesInput.text.toString()
            val email = etEmailInput.text.toString()
            val website = etWebsiteInput.text.toString()
            val notes = etNotesInput.text.toString()

            return UserCorporationDto(
                idFirebase = Constants.USER_DB.value,
                name = tittle,
                address = address,
                poster = poster,
                description = descriptions,
                phones = phones,
                email = email,
                website = website,
                notes = notes
            )
        }
    }

    private fun onCheckEmptyFields(): Boolean {
        with(binding) {
            val title = etTittleInput.text.toString()
            val description = etDescriptionInput.text.toString()
            val email = etEmailInput.text.toString()

            onCheckError(title, description, email)

            return !(title.isNotEmpty() && description.isNotEmpty() && email.isNotEmpty())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}