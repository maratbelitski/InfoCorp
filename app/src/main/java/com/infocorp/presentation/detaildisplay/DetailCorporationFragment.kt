package com.infocorp.presentation.detaildisplay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

    private var updateStateBottomMenu: (() -> Unit)? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).disableBottomMenu() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCorporationBinding.inflate(layoutInflater)
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

    private fun onListeners() {
        binding.btnGoToAdditionally.setOnClickListener {
            val action = DetailCorporationFragmentDirections
                .actionDetailCorporationFragmentToAdditionallyFragment(arguments.corporation)
            findNavController().navigate(action)
        }

        binding.btnEditInfo.setOnClickListener {
            val action = DetailCorporationFragmentDirections
                .actionDetailCorporationFragmentToCreateUserCorporationFragment(arguments.corporation)
            findNavController().navigate(action)
        }

        binding.emailCard.btnSendResume.setOnClickListener {
            val addresses = arguments.corporation.email.split(",").toTypedArray()
            val tittleCv = fragmentViewModel.getTittleCvUser()
            val bodyCv = fragmentViewModel.getBodyCvUser()
            val linkCv = fragmentViewModel.getLinkCvUser()
            sendEmailResume(addresses, tittleCv, bodyCv, linkCv)
        }
    }

    private fun initArgs() {
        with(binding) {
            Glide.with(posterCard.ivPoster)
                .load(arguments.corporation.poster)
                .placeholder(R.drawable.corp_list_logo)
                .into(posterCard.ivPoster)

            val defaultValue = getString(R.string.not_specified)
            val defaultColor = ContextCompat.getColor(requireContext(), R.color.unknown_text_color)

            val name = arguments.corporation.name
            var description = arguments.corporation.description
            var address = arguments.corporation.address
            var phones = arguments.corporation.phones
            var email = arguments.corporation.email
            var website = arguments.corporation.website


            if (description.isBlank()) {
                description = defaultValue
                descriptionCard.tvDescriptionCorp.setTextColor(defaultColor)
            }
            if (address.isBlank()) {
                address = defaultValue
                addressCard.tvAddressText.setTextColor(defaultColor)
            }
            if (phones.isBlank()) {
                phones = defaultValue
                phonesCard.tvPhonesCorp.setTextColor(defaultColor)
            }
            if (email.isBlank()) {
                email = defaultValue
                emailCard.tvEmailCorp.setTextColor(defaultColor)
            }
            if (website.isBlank()) {
                website = defaultValue
                websiteCard.tvWebsiteCorp.setTextColor(defaultColor)
            }

            nameCard.tvName.text = name
            descriptionCard.tvDescriptionCorp.text = description
            addressCard.tvAddressText.text = address
            phonesCard.tvPhonesCorp.text = phones
            emailCard.tvEmailCorp.text = email
            websiteCard.tvWebsiteCorp.text = website
        }
    }

    private fun sendEmailResume(
        addresses: Array<String>,
        tittle: String,
        body: String,
        link: String
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {

            val bodyMessage = body + "\n\n" + link
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, tittle)
            putExtra(Intent.EXTRA_TEXT, bodyMessage)
        }
        startActivity(Intent.createChooser(intent, "Choose client"))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}