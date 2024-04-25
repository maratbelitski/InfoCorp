package com.infocorp.presentation.generaldisplay

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.initialize
import com.infocorp.R
import com.infocorp.data.corporationdto.Corporation
import com.infocorp.data.network.CorporationFactory
import com.infocorp.databinding.FragmentGeneralBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null
    private val binding: FragmentGeneralBinding
        get() = _binding ?: throw Exception()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGeneralBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // проверка dadata
//        GlobalScope.launch(Dispatchers.Default) {
//           val result = CorporationFactory().corporationService.getAllCorporations("АБУШЕНКО ВИ")
//            Log.i("MyLog","${result.body()?.suggestions}")
//        }


        val database = Firebase.database.getReference("CORPORATION")


        val corp1 = Corporation()
       // database.push().setValue(corp1)

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children
                result.forEach {
                    Log.i("MyLog", "${it.value}")
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}