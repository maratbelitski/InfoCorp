package com.infocorp.presentation.detaildisplay

import android.R.attr.label
import android.R.attr.text
import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.infocorp.R
import com.infocorp.databinding.FragmentDetailCorporationBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.UpdateBottomMenu


class DetailCorporationFragment : Fragment() {

    companion object{
        private const val BELARUS_LOCATION = "geo:0,0?q= Беларусь"
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

        initArgs()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        onListeners()

    }

    private fun onListeners() {
        binding.btnSearchInInternet.setOnClickListener {
            val query = arguments.corporation.name + resources.getString(R.string.it_query)
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, query)
            startActivity(intent)
        }

        binding.btnShowLocation.setOnClickListener {
            val corporation = arguments.corporation.name
            val address = arguments.corporation.address
            val gmmIntentUri = Uri.parse("$BELARUS_LOCATION + $corporation + $address")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun initViews() {
        updateStateBottomMenu = activity as MainActivity
        updateStateBottomMenu.disableBottomMenu()
    }

    private fun initArgs() {
        with(binding) {
            Glide.with(ivPoster)
                .load(arguments.corporation.poster)
                .placeholder(R.drawable.corp_list_logo)
                .into(ivPoster)

            tvName.text = arguments.corporation.name
            tvDescriptionCorp.text = arguments.corporation.description
            tvAddressCorp.text = arguments.corporation.address
            tvPhonesCorp.text = arguments.corporation.phones
            tvEmailCorp.text = arguments.corporation.email
            tvWebsiteCorp.text = arguments.corporation.website
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateStateBottomMenu.enableBottomMenu()
        _binding = null
    }
}