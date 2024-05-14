package com.infocorp.presentation.usercorpgeneraldisplay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infocorp.R
import com.infocorp.databinding.FragmentUserCorpGeneralBinding
import com.infocorp.domain.model.Corporation
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.favouritedisplay.FavouriteFragmentDirections
import com.infocorp.presentation.listdisplay.ListCorporationsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserCorpGeneralFragment : Fragment() {

    private var _binding: FragmentUserCorpGeneralBinding? = null
    private val binding: FragmentUserCorpGeneralBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: UserCorpGeneralFragmentViewModel by viewModels()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }

    private val defaultCorp by lazy {
        Corporation()
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
                updateStateBottomMenu.disableBottomMenu()
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