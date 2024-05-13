package com.infocorp.presentation.listusercorporations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infocorp.R
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.databinding.FragmentListUserCorporationsBinding
import com.infocorp.databinding.FragmentUserCorpGeneralBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.listdisplay.ListCorporationsFragmentDirections
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUserCorporationsFragment : Fragment() {
    private var _binding: FragmentListUserCorporationsBinding? = null
    private val binding:  FragmentListUserCorporationsBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: ListUserCorporationsFragmentViewModel by viewModels()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }
    private val myAdapter: CorporationAdapter by lazy {
        CorporationAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentListUserCorporationsBinding.inflate(layoutInflater)

        updateStateBottomMenu.disableBottomMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        onObservers()

        onListeners()
    }

    private fun onListeners() {
        myAdapter.onClick = {
            val action = ListUserCorporationsFragmentDirections
                .actionListUserCorporationsFragmentToDetailCorporationFragment(it)

            findNavController().navigate(action)
        }
    }

    private fun onObservers() {
        fragmentViewModel.listFromLocalSource.observe(viewLifecycleOwner) {
            myAdapter.submitList(it)
        }
    }

    private fun initViews() {
        binding.recycler.adapter = myAdapter
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}