package com.infocorp.presentation.resumestatedisplay

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.infocorp.R
import com.infocorp.databinding.FragmentResumeStateBinding
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.ResumeState
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.presentation.resumestatedisplay.adapter.ResumeStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResumeStateFragment : Fragment() {

    private var _binding: FragmentResumeStateBinding? = null
    private val binding: FragmentResumeStateBinding
        get() = _binding ?: throw NullPointerException()

    private val viewModel: ResumeStateViewModel by viewModels()

    private val myAdapter:  ResumeStateAdapter by lazy {
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
        onListeners()

        val list = listOf(
            ResumeState(
                poster = "http://belorussia.su/com_logo/1422426178logo1_big.jpg",
                title = "TEST",
                dateSent = "11.12.2024",
                dateResponse = "13.12.2024",
                notes = "4Д, ОДООбщество с дополнительной ответственностью. Год основания: 2008. Количество сотрудников: 6. УНП: 590830939"
            ),
            ResumeState(
                poster = "http://belorussia.su/com_logo/1422426178logo1_big.jpg",
                title = "TEST 2",
                dateSent = "11.12.2024",
                dateResponse = "13.12.2024",
                notes = "4Д, ОДООбщество с дополнительной ответственностью. Год основания: 2008. Количество сотрудников: 6. УНП: 590830939"
            ),
            ResumeState(
                poster = "",
                title = "TEST 3",
                dateSent = "11.12.2024",
                dateResponse = "13.12.2024",
                notes = "4Д, ОДООбщество с дополнительной ответственностью. Год основания: 2008. Количество сотрудников: 6. УНП: 590830939"
            )
        )

        myAdapter.submitList(list)
    }

    private fun onListeners() {
//        with(binding) {
//            myAdapter.onClick = {
//
//                val action = ResumeStateFragmentDirections
//                    .actionResumeStateFragmentToDetailCorporationFragment()
//                findNavController().navigate(action)
//            }
//        }
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