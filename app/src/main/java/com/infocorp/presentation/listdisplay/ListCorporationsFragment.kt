package com.infocorp.presentation.listdisplay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.domain.entity.Corporation
import com.infocorp.presentation.UpdateBottomMenu
import com.infocorp.presentation.listdisplay.ListCorporationsFragmentDirections
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCorporationsFragment : Fragment() {

    private var _binding: FragmentListCorporationsBinding? = null
    private val binding: FragmentListCorporationsBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: ListCorporationsViewModel by viewModels()
    private lateinit var myAdapter: CorporationAdapter
    private lateinit var updateStateBottomMenu: UpdateBottomMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCorporationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        onObservers()

        onListeners()

        searchCorporation()
    }
    private fun searchCorporation(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fragmentViewModel.listFromLocalSource.observe(viewLifecycleOwner) {
                    val newList = it.filter { product ->
                        product.name.contains(newText.toString().trim(), ignoreCase = true)
                    }
                    myAdapter.submitList(newList)
                }
                return false
            }
        })
    }
    private fun onListeners() {
        myAdapter.onLongClick = {
            fragmentViewModel.changeStateCorp(it)

            when (it.isFavourite) {
                true -> fragmentViewModel.removeCorpFromFavourite(it)
                false -> fragmentViewModel.addCorpToFavourite(it)
            }
        }

        myAdapter.onClick = {
            val action = ListCorporationsFragmentDirections
                .actionListCorporationsFragmentToDetailCorporationFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun onObservers() {
        fragmentViewModel.showShimmer.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.shimmer.visibility = View.VISIBLE
                    binding.recycler.visibility = View.GONE
                }
                false -> {
                    binding.recycler.visibility = View.VISIBLE
                    binding.shimmer.visibility = View.GONE
                }
            }
        }

        fragmentViewModel.listFromLocalSource.observe(viewLifecycleOwner) {
            myAdapter.submitList(it)
        }
    }

    private fun initViews() {
//        updateStateBottomMenu = activity as MainActivity
//        updateStateBottomMenu.enableBottomMenu()

        myAdapter = CorporationAdapter()

        binding.recycler.adapter = myAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //  updateStateBottomMenu.enableBottomMenu()
    }
}