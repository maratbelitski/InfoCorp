package com.infocorp.presentation.additionallydisplay

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.infocorp.R
import com.infocorp.databinding.FragmentAdditionallyBinding
import com.infocorp.domain.model.Corporation
import com.infocorp.presentation.MainActivity


class AdditionallyFragment : Fragment() {

    companion object {
        private const val BELARUS_LOCATION = "geo:0,0?q= Беларусь"
        private const val GOOGLE_PACKAGE = "com.google.android.apps.maps"
    }

    private var _binding: FragmentAdditionallyBinding? = null
    private val binding: FragmentAdditionallyBinding
        get() = _binding ?: throw Exception()

    private val arguments: AdditionallyFragmentArgs by navArgs()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdditionallyBinding.inflate(layoutInflater)

        initArgs()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onListeners()
        initViews()
    }

    private fun initViews() {
        updateStateBottomMenu.disableBottomMenu()
    }

    private fun onListeners() {
        binding.searchCard.search.setOnClickListener {
            val query = arguments.corporation.name + resources.getString(R.string.it_query)
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, query)
            startActivity(intent)
        }

        binding.locationCard.location.setOnClickListener {
            val corporation = arguments.corporation.name
            val address = arguments.corporation.address

            val gmmIntentUri = Uri.parse("$BELARUS_LOCATION + $corporation + $address")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage(GOOGLE_PACKAGE)
            startActivity(mapIntent)
        }

        binding.btnGoToGeneral.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.egrCard.egr.setOnClickListener {
            val action = AdditionallyFragmentDirections
                .actionAdditionallyFragmentToEgrFragment(arguments.corporation)
            findNavController().navigate(action)
        }
    }

    private fun initArgs() {
        with(binding) {
            nameCard.tvName.text = arguments.corporation.name
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}