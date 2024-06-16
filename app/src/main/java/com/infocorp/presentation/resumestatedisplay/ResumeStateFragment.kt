package com.infocorp.presentation.resumestatedisplay

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.infocorp.R
import com.infocorp.databinding.FragmentResumeStateBinding
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.ResumeState
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.presentation.resumestatedisplay.adapter.ResumeStateAdapter
import dagger.hilt.android.AndroidEntryPoint
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
    }

    private fun onListeners() {

    }

    private fun onObservers() {
        lifecycleScope.launch {
            fragmentViewModel.allResume
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (it.isEmpty()) {
                        binding.tvEmptyList.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE

                    } else {
                        binding.tvEmptyList.visibility = View.GONE
                        binding.recycler.visibility = View.VISIBLE
                        myAdapter.submitList(it)
                    }
                }
        }
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
        binding.recycler.adapter = myAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}