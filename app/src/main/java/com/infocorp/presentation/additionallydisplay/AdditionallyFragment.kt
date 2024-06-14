package com.infocorp.presentation.additionallydisplay

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.infocorp.R
import com.infocorp.databinding.FragmentAdditionallyBinding
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.utils.Constants


class AdditionallyFragment : Fragment() {

    private var _binding: FragmentAdditionallyBinding? = null
    private val binding: FragmentAdditionallyBinding
        get() = _binding ?: throw Exception()

    private val arguments: AdditionallyFragmentArgs by navArgs()

    private var updateStateBottomMenu: (() -> Unit)? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).disableBottomMenu() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdditionallyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArgs()
        onListeners()
        initViews()
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
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

            val gmmIntentUri =
                Uri.parse("${Constants.BELARUS_LOCATION.value} + $corporation + $address")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage(Constants.GOOGLE_PACKAGE.value)
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