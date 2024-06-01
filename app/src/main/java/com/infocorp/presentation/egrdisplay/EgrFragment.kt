package com.infocorp.presentation.egrdisplay

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.PagerSnapHelper
import com.infocorp.R
import com.infocorp.databinding.FragmentEgrBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.egrdisplay.adapter.ResponseEgrAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EgrFragment : Fragment() {

    private var _binding: FragmentEgrBinding? = null
    private val binding: FragmentEgrBinding
        get() = _binding ?: throw Exception()

    private val arguments: EgrFragmentArgs by navArgs()
    private val fragmentViewModel: EgrViewModel by viewModels()

    private val myAdapter by lazy {
        ResponseEgrAdapter()
    }

    private var updateStateBottomMenu: (() -> Unit)? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).disableBottomMenu() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEgrBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArgs()
        onListeners()
        onObservers()
        initViews()
    }

    private fun onObservers() {
        fragmentViewModel.listDataEgr.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    ContextCompat.getString(requireActivity(), R.string.response_is_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
            myAdapter.submitList(it)
        }

        lifecycleScope.launch {
            fragmentViewModel.showShimmer
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    with(binding) {
                        when (it) {
                            true -> {
                                shimmerCardResponse.visibility = View.VISIBLE
                                recycler.visibility = View.GONE
                            }

                            false -> {
                                recycler.visibility = View.VISIBLE
                                shimmerCardResponse.visibility = View.GONE
                            }
                        }
                    }
                }
        }

        fragmentViewModel.exceptionNetwork.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onListeners() {
        binding.searchingForma.btnSearchByName.setOnClickListener {
            val title = binding.searchingForma.etByTitle.text.toString()
            val titleLayout = binding.searchingForma.byName
            titleLayout.error = resources.getString(R.string.error_input_layout)

            fragmentViewModel.validationError(title, titleLayout)
            fragmentViewModel.getInfoEgrByName(title)
        }

        binding.searchingForma.btnSearchByUnp.setOnClickListener {
            val unpText = binding.searchingForma.etByUnp.text.toString()
            val unpLayout = binding.searchingForma.byUnp
            unpLayout.error = resources.getString(R.string.error_input_layout)

            fragmentViewModel.validationError(unpText, unpLayout)
            fragmentViewModel.getInfoEgrByUnp(unpText)
        }
    }

    private fun initArgs() {
        binding.searchingForma.etByTitle.setText(arguments.corporation.name)
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
        binding.recycler.adapter = myAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}