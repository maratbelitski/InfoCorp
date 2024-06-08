package com.infocorp.presentation.listusercorporations

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.databinding.FragmentListUserCorporationsBinding
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUserCorporationsFragment : Fragment() {
    private var _binding: FragmentListUserCorporationsBinding? = null
    private val binding: FragmentListUserCorporationsBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: ListUserCorporationsFragmentViewModel by viewModels()

    private val myAdapter: CorporationAdapter by lazy {
        CorporationAdapter()
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

            if (it.isEmpty()){
                binding.tvEmptyList.visibility = View.VISIBLE
                binding.recycler.visibility = View.GONE
            } else {
                binding.tvEmptyList.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
                myAdapter.submitList(it)
            }
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
        updateStateBottomMenu?.invoke()
        binding.recycler.adapter = myAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}