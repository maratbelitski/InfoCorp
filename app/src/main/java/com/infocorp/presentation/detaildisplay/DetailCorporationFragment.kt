package com.infocorp.presentation.detaildisplay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.infocorp.R
import com.infocorp.databinding.FragmentDetailCorporationBinding
import com.infocorp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailCorporationFragment : Fragment() {

    private var _binding: FragmentDetailCorporationBinding? = null
    private val binding: FragmentDetailCorporationBinding
        get() = _binding ?: throw Exception()

    private val arguments: DetailCorporationFragmentArgs by navArgs()
    private val fragmentViewModel: DetailCorporationViewModel by viewModels()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCorporationBinding.inflate(layoutInflater)

        initArgs()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        onListeners()

    }

    private fun onListeners() {
        binding.btnGoToAdditionally.setOnClickListener {
            val action = DetailCorporationFragmentDirections
                .actionDetailCorporationFragmentToAdditionallyFragment(arguments.corporation)
            findNavController().navigate(action)
        }
    }

    private fun initViews() {
        updateStateBottomMenu.disableBottomMenu()
    }

    private fun initArgs() {
        with(binding) {
            Glide.with(ivPoster)
                .load(arguments.corporation.poster)
                .placeholder(R.drawable.corp_list_logo)
                .into(ivPoster)

            val defaultValue = getString(R.string.not_specified)
            val defaultColor = resources.getColor(R.color.unknown_text_color)

            val name = arguments.corporation.name
            var description = arguments.corporation.description
            var address = arguments.corporation.address
            var phones = arguments.corporation.phones
            var email = arguments.corporation.email
            var website = arguments.corporation.website


            if (description.isBlank()) {
                description = defaultValue
                tvDescriptionCorp.setTextColor(defaultColor)
            }
            if (address.isBlank()) {
                address = defaultValue
                addressCard.tvAddressText.setTextColor(defaultColor)
            }
            if (phones.isBlank()) {
                phones = defaultValue
                tvPhonesCorp.setTextColor(defaultColor)
            }
            if (email.isBlank()) {
                email = defaultValue
                tvEmailCorp.setTextColor(defaultColor)
            }
            if (website.isBlank()) {
                website = defaultValue
                tvWebsiteCorp.setTextColor(defaultColor)
            }


            nameCard.tvName.text = name
            tvDescriptionCorp.text = description
            addressCard.tvAddressText.text = address
            tvPhonesCorp.text = phones
            tvEmailCorp.text = email
            tvWebsiteCorp.text = website
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateStateBottomMenu.enableBottomMenu()
        _binding = null
    }
}