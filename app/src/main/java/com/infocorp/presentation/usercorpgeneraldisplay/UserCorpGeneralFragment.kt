package com.infocorp.presentation.usercorpgeneraldisplay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infocorp.databinding.FragmentUserCorpGeneralBinding
import com.infocorp.domain.model.Corporation
import com.infocorp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserCorpGeneralFragment : Fragment() {

    private var _binding: FragmentUserCorpGeneralBinding? = null
    private val binding: FragmentUserCorpGeneralBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: UserCorpGeneralFragmentViewModel by viewModels()


    private val defaultCorp by lazy {
        Corporation()
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
        _binding = FragmentUserCorpGeneralBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onListeners()
        onObservers()
    }

    private fun onObservers() {
        fragmentViewModel.disableBottomNavigation.observe(viewLifecycleOwner) {
            if (it) {
                updateStateBottomMenu?.invoke()
            }
        }
    }

    private fun onListeners() {

        with(binding) {
            listUserCorpCard.listUserCorp.setOnClickListener {
                val action = UserCorpGeneralFragmentDirections
                    .actionUserCorpGeneralFragmentToListUserCorporationsFragment()
                findNavController().navigate(action)
            }
            createUserCorpCard.createUserCorp.setOnClickListener {
                val action = UserCorpGeneralFragmentDirections
                    .actionUserCorpGeneralFragmentToCreateUserCorporationFragment(defaultCorp)
                findNavController().navigate(action)
            }
            changeInfoCorpCard.changeInfoCorp.setOnClickListener {
                val action = UserCorpGeneralFragmentDirections
                    .actionUserCorpGeneralFragmentToListCorporationsFragment()
                findNavController().navigate(action)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}