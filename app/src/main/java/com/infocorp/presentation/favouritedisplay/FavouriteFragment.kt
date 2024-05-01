package com.infocorp.presentation.favouritedisplay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infocorp.databinding.FragmentFavouriteBinding
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding: FragmentFavouriteBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: FavouriteFragmentViewModel by viewModels()
    private lateinit var myAdapter: CorporationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        onObservers()

        onListeners()
    }

    private fun onListeners() {
        myAdapter.onLongClick = {
            fragmentViewModel.removeCorpFromFavourite(it)
            fragmentViewModel.changeStateCorp(it)
        }
    }

    private fun onObservers() {
        fragmentViewModel.listFavouriteCorp.observe(viewLifecycleOwner) {
            myAdapter.submitList(it)
        }
    }

    private fun initViews() {
        myAdapter = CorporationAdapter()
        binding.recycler.adapter = myAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}