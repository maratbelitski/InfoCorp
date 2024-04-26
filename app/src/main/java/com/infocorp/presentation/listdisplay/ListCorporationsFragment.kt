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
import com.infocorp.databinding.FragmentListCorporationsBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.UpdateBottomMenu

class ListCorporationsFragment : Fragment() {

    companion object {
        private const val FIRE_BASE_KEY = "CORPORATION"
    }

    private var _binding: FragmentListCorporationsBinding? = null
    private val binding: FragmentListCorporationsBinding
        get() = _binding ?: throw Exception()


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
        updateStateBottomMenu.disableBottomMenu()

        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children
                result.forEach {
                    val corp = it.getValue(CorporationDto::class.java)
                    if (corp != null) {
                        Log.i("MyLog", "$corp")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    private fun initViews() {
        firebase = Firebase.database.getReference(FIRE_BASE_KEY)

        updateStateBottomMenu = activity as MainActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        updateStateBottomMenu.enableBottomMenu()
    }
}