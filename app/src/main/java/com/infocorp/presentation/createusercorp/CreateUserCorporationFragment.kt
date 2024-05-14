package com.infocorp.presentation.createusercorp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.databinding.FragmentCreateUserCorporationBinding
import com.infocorp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserCorporationFragment : Fragment() {

    companion object {
        private const val FIRE_BASE_USER = "USER_IT_CORPORATION"
    }

    private var _binding: FragmentCreateUserCorporationBinding? = null
    private val binding: FragmentCreateUserCorporationBinding
        get() = _binding ?: throw Exception()

    private val arguments: CreateUserCorporationFragmentArgs by navArgs()
    private val fragmentViewModel: CreateUserCorporationFragmentViewModel by viewModels()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserCorporationBinding.inflate(layoutInflater)

        initArgs()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onListeners()

        onObservers()
    }

    private fun onObservers() {
        fragmentViewModel.disableBottomNavigation.observe(viewLifecycleOwner) {
            if (it) updateStateBottomMenu.disableBottomMenu()
        }
    }

    private fun initArgs(){
        with(binding){
            etPosterInput.setText(arguments.corporation.poster)
            etTittleInput.setText(arguments.corporation.name)
            etDescriptionInput.setText(arguments.corporation.description)
            etAddressInput.setText(arguments.corporation.address)
            etPhonesInput.setText(arguments.corporation.phones)
            etEmailInput.setText(arguments.corporation.email)
            etWebsiteInput.setText(arguments.corporation.website)
        }
    }
    private fun onListeners() {
        binding.btnSend.setOnClickListener {
            sendUserCorporation()
            Toast.makeText(requireContext(), "Information send to developer", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnCreate.setOnClickListener {
            addUserCorporation()
            Toast.makeText(
                requireContext(),
                "Information saved to your database",
                Toast.LENGTH_SHORT
            ).show()
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

                etPoster.requestFocus()
            }
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


            return UserCorporationDto(
                idFirebase = FIRE_BASE_USER,
                name = tittle,
                address = address,
                poster = poster,
                description = descriptions,
                phones = phones,
                email = email,
                website = website
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}