package com.infocorp.presentation.createusercorp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.databinding.FragmentCreateUserCorporationBinding
import com.infocorp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserCorporationFragment : Fragment() {

    companion object {
        private const val FIRE_BASE_USER = "USER_CORPORATION"
    }

    private var _binding: FragmentCreateUserCorporationBinding? = null
    private val binding: FragmentCreateUserCorporationBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: CreateUserCorporationFragmentViewModel by viewModels()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserCorporationBinding.inflate(layoutInflater)

        updateStateBottomMenu.disableBottomMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onListeners()
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
//            etPosterInput.text?.clear()
//            etTittleInput.text?.clear()
//            etDescriptionInput.text?.clear()
//            etAddressInput.text?.clear()
//            etPhonesInput.text?.clear()
//            etEmailInput.text?.clear()
//            etWebsiteInput.text?.clear()


