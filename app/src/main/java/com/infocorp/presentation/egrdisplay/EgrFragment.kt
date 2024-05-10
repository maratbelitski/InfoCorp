package com.infocorp.presentation.egrdisplay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.PagerSnapHelper
import com.infocorp.R
import com.infocorp.databinding.FragmentEgrBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.egrdisplay.adapter.ResponseEgrAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EgrFragment : Fragment() {

    private var _binding: FragmentEgrBinding? = null
    private val binding: FragmentEgrBinding
        get() = _binding ?: throw Exception()

    private val arguments: EgrFragmentArgs by navArgs()
    private val fragmentViewModel: EgrViewModel by viewModels()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }

    private val myAdapter by lazy {
        ResponseEgrAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEgrBinding.inflate(layoutInflater)

        initArgs()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateStateBottomMenu.disableBottomMenu()

        onListeners()

        onObservers()

        initViews()
    }

    private fun onObservers() {
        fragmentViewModel.listDataEgr.observe(viewLifecycleOwner) {
            if (it.isEmpty()) { Toast.makeText(requireContext(), "Response is empty. Check your enter data", Toast.LENGTH_SHORT).show()
            }
            myAdapter.submitList(it)
        }

        fragmentViewModel.showShimmer.observe(viewLifecycleOwner) {
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

        fragmentViewModel.exceptionNetwork.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onListeners() {
        binding.searchingForma.btnSearchByName.setOnClickListener {
            val title = binding.searchingForma.etByTitle.text.toString()
            fragmentViewModel.getInfoEgrByName(title)
        }

        binding.searchingForma.btnSearchByUnp.setOnClickListener {
            val unp = binding.searchingForma.etByUnp.text.toString()
            fragmentViewModel.getInfoEgrByUnp(unp)
        }
    }

    private fun initArgs() {
        binding.searchingForma.etByTitle.setText(arguments.corporation.name)
    }

    private fun initViews() {
        binding.recycler.adapter = myAdapter
        // val helper = PagerSnapHelper()
        // helper.attachToRecyclerView(binding.recycler)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}