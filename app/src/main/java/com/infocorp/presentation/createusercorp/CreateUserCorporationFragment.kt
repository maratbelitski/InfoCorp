package com.infocorp.presentation.createusercorp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infocorp.R
import com.infocorp.databinding.FragmentCreateUserCorporationBinding
import com.infocorp.databinding.FragmentUserCorpGeneralBinding

class CreateUserCorporationFragment : Fragment() {

    private var _binding: FragmentCreateUserCorporationBinding? = null
    private val binding: FragmentCreateUserCorporationBinding
        get() = _binding ?: throw Exception()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentCreateUserCorporationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}