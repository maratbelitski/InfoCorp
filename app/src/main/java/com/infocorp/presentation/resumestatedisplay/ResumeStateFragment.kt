package com.infocorp.presentation.resumestatedisplay

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.databinding.FragmentResumeStateBinding
import com.infocorp.domain.model.Corporation
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.presentation.resumestatedisplay.adapter.ResumeStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResumeStateFragment : Fragment() {

    private var _binding: FragmentResumeStateBinding? = null
    private val binding: FragmentResumeStateBinding
        get() = _binding ?: throw NullPointerException()

    private val fragmentViewModel: ResumeStateViewModel by viewModels()

    private val myAdapter: ResumeStateAdapter by lazy {
        ResumeStateAdapter()
    }

    private var updateStateBottomMenu: (() -> Unit)? = null

    private var hasNavigated = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).disableBottomMenu() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumeStateBinding.inflate(layoutInflater)
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
        myAdapter.onClickButtonSave = {
            fragmentViewModel.updateResume(it, it.result, it.notes, it.dateResponse)
        }

        myAdapter.onClick = { corp ->

            lifecycleScope.launch {

                fragmentViewModel.getCorporationFromResume(corp.idCorporation)

                fragmentViewModel.corpFromResume
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .drop(1)
                    .collect {
                        if (!hasNavigated) {
                            val action = ResumeStateFragmentDirections
                                .actionResumeStateFragmentToDetailCorporationFragment(it)
                            findNavController().navigate(action)

                            hasNavigated = true
                        }
                    }
            }
        }
    }

    private fun onObservers() {
        lifecycleScope.launch {
            fragmentViewModel.allResume
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    with(binding) {
                        if (it.isNotEmpty()) {
                            tvEmptyList.visibility = View.GONE
                            recycler.visibility = View.VISIBLE
                            myAdapter.submitList(it)
                        }
                    }
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.isLoaded
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    with(binding) {
                        if (it) {
                            tvEmptyList.visibility = View.VISIBLE
                            recycler.visibility = View.GONE
                        }
                    }
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.shimmer
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    with(binding) {
                        when (it) {
                            true -> {
                                recycler.visibility = View.GONE
                                shimmer.visibility = View.VISIBLE
                            }

                            false -> {
                                recycler.visibility = View.VISIBLE
                                shimmer.visibility = View.GONE
                            }
                        }
                    }
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
                val currentResume = myAdapter.currentList[viewHolder.adapterPosition]
                fragmentViewModel.removeResume(currentResume)
            }
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun initViews() {
        fragmentViewModel.downloadResumes()

        updateStateBottomMenu?.invoke()
        binding.recycler.adapter = myAdapter
    }

    override fun onResume() {
        super.onResume()
        hasNavigated = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}