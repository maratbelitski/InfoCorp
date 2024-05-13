package com.infocorp.presentation.listdisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCorporationsFragment : Fragment() {

    private var _binding: FragmentListCorporationsBinding? = null
    private val binding: FragmentListCorporationsBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: ListCorporationsViewModel by viewModels()
    private val myAdapter: CorporationAdapter by lazy {
        CorporationAdapter()
    }
    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }

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

    private fun searchCorporation() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fragmentViewModel.listFromLocalSource.observe(viewLifecycleOwner) {
                    val newList = fragmentViewModel.searchCorporation(it, newText ?: "")
                    myAdapter.submitList(newList)
                }
                return false
            }
        })
    }

    private fun onListeners() {
        with(fragmentViewModel) {

            myAdapter.onLongClick = {
                addInOldCorps(it)
                changeStateFavoriteCorp(it)
                changeStateNewCorp(it)

                when (it.isFavourite) {
                    true -> removeCorpFromFavourite(it)
                    false -> addCorpToFavourite(it)
                }
            }

            myAdapter.onClick = {
                addInOldCorps(it)
                changeStateNewCorp(it)

                val action = ListCorporationsFragmentDirections
                    .actionListCorporationsFragmentToDetailCorporationFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun onObservers() {
        fragmentViewModel.showShimmer.observe(viewLifecycleOwner) {

            with(binding) {
                when (it) {
                    true -> {
                        shimmerCardList.shimmer.visibility = View.VISIBLE
                        recycler.visibility = View.GONE
                    }

                    false -> {
                        recycler.visibility = View.VISIBLE
                        shimmerCardList.shimmer.visibility = View.GONE
                    }
                }
            }
        }
        fragmentViewModel.disableBottomNavigation.observe(viewLifecycleOwner) {
           if (!it) updateStateBottomMenu.enableBottomMenu()
        }

        fragmentViewModel.listFromLocalSource.observe(viewLifecycleOwner) {
            myAdapter.submitList(it)
        }
    }

    private fun initViews() {
        binding.recycler.adapter = myAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //  updateStateBottomMenu.enableBottomMenu()
    }
}