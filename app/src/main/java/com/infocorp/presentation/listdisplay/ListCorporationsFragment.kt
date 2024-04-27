package com.infocorp.presentation.listdisplay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.domain.Corporation
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.UpdateBottomMenu
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter

class ListCorporationsFragment : Fragment() {

    companion object {
        private const val FIRE_BASE_KEY = "CORPORATION"
    }

    private var _binding: FragmentListCorporationsBinding? = null
    private val binding: FragmentListCorporationsBinding
        get() = _binding ?: throw Exception()

    private lateinit var myAdapter: CorporationAdapter
    private lateinit var updateStateBottomMenu: UpdateBottomMenu
    private lateinit var firebase: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCorporationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        val mapper = CorporationMapper()

        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children
                val list = mutableListOf<Corporation>()
                result.forEach {
                    val corpDto = it.getValue(CorporationDto::class.java)
                    if (corpDto != null) {
                        val corp = mapper.corporationDtoToCorporation(corpDto)
                       // Log.i("MyLog", "$corp")

                        list.add(corp)

                    }
                }
                myAdapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    private fun initViews() {
        myAdapter = CorporationAdapter()
        binding.recycler.adapter = myAdapter

        firebase = Firebase.database.getReference(FIRE_BASE_KEY)

        updateStateBottomMenu = activity as MainActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
      //  updateStateBottomMenu.enableBottomMenu()
    }
}