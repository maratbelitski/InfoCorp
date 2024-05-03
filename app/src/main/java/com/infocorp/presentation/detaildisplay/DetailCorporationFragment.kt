package com.infocorp.presentation.detaildisplay

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.infocorp.R
import com.infocorp.databinding.FragmentDetailCorporationBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.UpdateBottomMenu

class DetailCorporationFragment : Fragment() {

    companion object {

    }


    private var _binding: FragmentDetailCorporationBinding? = null
    private val binding: FragmentDetailCorporationBinding
        get() = _binding ?: throw Exception()

    private val arguments: DetailCorporationFragmentArgs by navArgs()
    private val fragmentViewModel: DetailCorporationViewModel by viewModels()
    private lateinit var updateStateBottomMenu: UpdateBottomMenu


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCorporationBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateStateBottomMenu = activity as MainActivity
        updateStateBottomMenu.disableBottomMenu()
    }

    private fun initViews() {
        Glide.with(binding.ivPoster)
            .load(arguments.corporation.poster)
            .placeholder(R.drawable.corp_list_logo)
            .into(binding.ivPoster)
        binding.tvName.text = arguments.corporation.name
        binding.tvDescriptionCorp.text = arguments.corporation.description
        binding.tvAddressCorp.text = arguments.corporation.address
        binding.tvPhonesCorp.text = arguments.corporation.phones
        binding.tvEmailCorp.text = arguments.corporation.email
        binding.tvWebsiteCorp.text = arguments.corporation.website
    }

    override fun onDestroy() {
        super.onDestroy()
        updateStateBottomMenu.enableBottomMenu()
        _binding = null
    }
}