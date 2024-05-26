package com.infocorp.presentation.favouritedisplay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.databinding.FragmentFavouriteBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.egrdisplay.adapter.ResponseEgrAdapter
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding: FragmentFavouriteBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: FavouriteFragmentViewModel by viewModels()
    private val myAdapter by lazy {
        CorporationAdapter()
    }
    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }

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
        removeBySwipe()
    }

    private fun onListeners() {
        myAdapter.onLongClick = {
            fragmentViewModel.removeCorpFromFavourite(it)
            fragmentViewModel.changeStateCorp(it)
        }

        myAdapter.onClick = {
            val action = FavouriteFragmentDirections
                .actionFavouriteFragmentToDetailCorporationFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun removeBySwipe() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentCorp = myAdapter.currentList[viewHolder.adapterPosition]
                fragmentViewModel.changeStateCorp(currentCorp)
                fragmentViewModel.removeCorpFromFavourite(currentCorp)
            }
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun onObservers() {
        fragmentViewModel.listFavouriteCorp.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.shimmerCardList.shimmer.visibility = View.VISIBLE
                binding.recycler.visibility = View.GONE
            } else {
                binding.shimmerCardList.shimmer.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE

                myAdapter.submitList(it)
            }
        }

        fragmentViewModel.disableBottomNavigation.observe(viewLifecycleOwner) {
            if (!it) updateStateBottomMenu.enableBottomMenu()
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