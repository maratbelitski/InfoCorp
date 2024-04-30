package com.infocorp.presentation.listdisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCorporationsFragment : Fragment() {

    private var _binding: FragmentListCorporationsBinding? = null
    private val binding: FragmentListCorporationsBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: ListCorporationsViewModel by viewModels()
    private lateinit var myAdapter: CorporationAdapter
    //private lateinit var updateStateBottomMenu: UpdateBottomMenu

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
            Toast.makeText(requireActivity(), "click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onObservers() {
        fragmentViewModel.listFromFirebase.observe(viewLifecycleOwner) {
            myAdapter.submitList(it)
        }
    }

    private fun initViews() {
        myAdapter = CorporationAdapter()

        binding.recycler.adapter = myAdapter

        // updateStateBottomMenu = activity as MainActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //  updateStateBottomMenu.enableBottomMenu()
    }
}