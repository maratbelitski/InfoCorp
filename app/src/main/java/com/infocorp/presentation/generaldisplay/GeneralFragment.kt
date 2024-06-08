package com.infocorp.presentation.generaldisplay

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.infocorp.R
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.databinding.FragmentGeneralBinding
import com.infocorp.presentation.mainactivity.MainActivity
import com.infocorp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null

    private val binding: FragmentGeneralBinding
        get() = _binding ?: throw NullPointerException()

    private val firebase by lazy {
        fragmentViewModel.getFirebase()
    }

    private val auth by lazy {
        firebase.auth
    }

    private val firebaseDB by lazy {
        firebase.database.getReference(Constants.GENERAL_DB.value)
    }

    private val fragmentViewModel: GeneralFragmentViewModel by viewModels()

    private var isNetworkAvailable: (() -> Boolean)? = null
    private var updateStateBottomMenu: (() -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isNetworkAvailable = { (activity as MainActivity).isNetworkAvailable() }
        updateStateBottomMenu = { (activity as MainActivity).enableBottomMenu() }
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

        checkCurrentUser()
        initViews()
        downloadData()
        onObservers()
    }

    private fun downloadData() {
        lifecycleScope.launch(Dispatchers.IO) {

            val listIdFavourite = fragmentViewModel.downloadListIdFavourite()
            val listIdOldCorps = fragmentViewModel.downloadListIdOldCorps()

            val listFromFirebase = mutableListOf<CorporationDto>()

            fragmentViewModel.clearLocalDataBase()

            firebaseDB.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val myScope = CoroutineScope(Dispatchers.IO)
                    val responseFirebase = snapshot.children

                    responseFirebase.forEach { child ->
                        val corpDto = child.getValue(CorporationDto::class.java)
                        val corpDtoWithChildId = corpDto?.copy(id = child.key.toString())

                        if (corpDtoWithChildId != null) {

                            for (value in listIdFavourite) {
                                if (value == corpDtoWithChildId.id) {
                                    corpDtoWithChildId.isFavourite = true
                                    corpDtoWithChildId.isNew = false
                                }
                            }

                            for (value in listIdOldCorps) {
                                if (value == corpDtoWithChildId.id) {
                                    corpDtoWithChildId.isNew = false
                                }
                            }

                            listFromFirebase.add(corpDtoWithChildId)

                        }
                    }
                    myScope.launch {
                        fragmentViewModel.addAllCorpInDataBase(listFromFirebase)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MyLog", "Error message ${error.message}")
                }
            })
        }
    }

    private fun onObservers() {

        lifecycleScope.launch {
            fragmentViewModel.allCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    with(binding) {
                        if (isNetworkAvailable?.invoke() == false) {
                            shimmerLayout.shimmer.visibility = View.VISIBLE
                            statisticCard.statistic.visibility = View.GONE

                        } else if (isNetworkAvailable?.invoke() == true && it == 0) {
                            shimmerLayout.shimmer.visibility = View.VISIBLE
                            statisticCard.statistic.visibility = View.GONE

                        } else {
                            statisticCard.statistic.visibility = View.VISIBLE
                            shimmerLayout.shimmer.visibility = View.GONE
                            statisticCard.countAllInBase.text = it.toString()
                        }
                    }
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.userCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.statisticCard.countInUserBase.text = it.toString()
                }
        }

        lifecycleScope.launch {
            fragmentViewModel.oldCorporation
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.statisticCard.countNewInBase.text = it.toString()
                }
        }

    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser == null) {

            val action = GeneralFragmentDirections.actionGeneralFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            findNavController().popBackStack(R.id.loginFragment, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}