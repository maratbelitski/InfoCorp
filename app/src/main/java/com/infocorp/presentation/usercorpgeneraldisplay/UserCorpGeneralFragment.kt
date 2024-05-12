package com.infocorp.presentation.usercorpgeneraldisplay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.infocorp.R
import com.infocorp.databinding.FragmentUserCorpGeneralBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.favouritedisplay.FavouriteFragmentDirections

class UserCorpGeneralFragment : Fragment() {

    private var _binding: FragmentUserCorpGeneralBinding? = null
    private val binding: FragmentUserCorpGeneralBinding
        get() = _binding ?: throw Exception()

    private val updateStateBottomMenu by lazy {
        activity as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentUserCorpGeneralBinding.inflate(layoutInflater)

        updateStateBottomMenu.disableBottomMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listUserCorpCard.listUserCorp.setOnClickListener {
           val action = UserCorpGeneralFragmentDirections.actionUserCorpGeneralFragmentToListUserCorporationsFragment()
            findNavController().navigate(action)
        }
        binding.createUserCorpCard.createUserCorp.setOnClickListener {
            val action = UserCorpGeneralFragmentDirections.actionUserCorpGeneralFragmentToCreateUserCorporationFragment()
            findNavController().navigate(action)
        }
        binding.changeInfoCorpCard.changeInfoCorp.setOnClickListener {
            Toast.makeText(requireContext(), "EDIT", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateStateBottomMenu.enableBottomMenu()
        _binding = null
    }
}