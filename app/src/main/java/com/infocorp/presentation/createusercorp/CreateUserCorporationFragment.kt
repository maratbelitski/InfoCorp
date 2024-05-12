package com.infocorp.presentation.createusercorp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.infocorp.R
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.databinding.FragmentCreateUserCorporationBinding
import com.infocorp.databinding.FragmentUserCorpGeneralBinding

class CreateUserCorporationFragment : Fragment() {

    private var _binding: FragmentCreateUserCorporationBinding? = null
    private val binding: FragmentCreateUserCorporationBinding
        get() = _binding ?: throw Exception()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserCorporationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseChild = Firebase.database.getReference("USER_CORPORATION")

        binding.btnSend.setOnClickListener {
            createUserCorporation(databaseChild)
            Toast.makeText(requireContext(), "Information send to developer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createUserCorporation(databaseChild: DatabaseReference) {
        with(binding) {
            val poster = etPosterInput.text.toString()
            val tittle = etTittleInput.text.toString()
            val descriptions = etDescriptionInput.text.toString()
            val address = etAddressInput.text.toString()
            val phones = etPhonesInput.text.toString()
            val email = etEmailInput.text.toString()
            val website = etWebsiteInput.text.toString()


            val userCorp = CorporationDto(
                idFirebase = databaseChild.key.toString(),
                name = tittle,
                address = address,
                poster = poster,
                description = descriptions,
                phones = phones,
                email = email,
                website = website
            )

            databaseChild.push().setValue(userCorp)

            etPosterInput.text?.clear()
            etTittleInput.text?.clear()
            etDescriptionInput.text?.clear()
            etAddressInput.text?.clear()
            etPhonesInput.text?.clear()
            etEmailInput.text?.clear()
            etWebsiteInput.text?.clear()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}