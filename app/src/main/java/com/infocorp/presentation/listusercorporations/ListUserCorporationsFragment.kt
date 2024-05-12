package com.infocorp.presentation.listusercorporations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infocorp.R
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.databinding.FragmentListUserCorporationsBinding
import com.infocorp.databinding.FragmentUserCorpGeneralBinding
import com.infocorp.presentation.MainActivity

class ListUserCorporationsFragment : Fragment() {
    private var _binding: FragmentListUserCorporationsBinding? = null
    private val binding:  FragmentListUserCorporationsBinding
        get() = _binding ?: throw Exception()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentListUserCorporationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}