package com.infocorp.presentation.listusercorporations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.R
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.databinding.FragmentListUserCorporationsBinding
import com.infocorp.databinding.FragmentUserCorpGeneralBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.listdisplay.ListCorporationsFragmentDirections
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListUserCorporationsFragment : Fragment() {
    private var _binding: FragmentListUserCorporationsBinding? = null
    private val binding: FragmentListUserCorporationsBinding
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
        _binding = FragmentListUserCorporationsBinding.inflate(layoutInflater)
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


        fragmentViewModel.disableBottomNavigation.observe(viewLifecycleOwner) {
            if (it) updateStateBottomMenu.disableBottomMenu()
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
                fragmentViewModel.removeCorpFromDataBase(currentCorp)
            }
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun initViews() {
        binding.recycler.adapter = myAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}