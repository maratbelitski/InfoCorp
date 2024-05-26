package com.infocorp.presentation.favouritedisplay


import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.infocorp.databinding.FragmentFavouriteBinding
import com.infocorp.presentation.MainActivity
import com.infocorp.presentation.listdisplay.adapter.CorporationAdapter
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context;
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding: FragmentFavouriteBinding
        get() = _binding ?: throw Exception()

    private val fragmentViewModel: FavouriteFragmentViewModel by viewModels()

    private val myAdapter by lazy {
        CorporationAdapter()
    }

    private lateinit var updateStateBottomMenu: (() -> Unit)
    private lateinit var isNetworkAvailable: (() -> Boolean)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) updateStateBottomMenu = { context.enableBottomMenu() }
        if (context is MainActivity) isNetworkAvailable = { context.isNetworkAvailable() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(layoutInflater)
        updateStateBottomMenu.invoke()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        onObservers()
        onListeners()
        removeBySwipe()
    }

    private fun onListeners() {
        myAdapter.onLongClick = {
            fragmentViewModel.removeCorpFromFavourite(it)
            fragmentViewModel.changeStateCorp(it)
        }

        myAdapter.onClick = {
            val action = FavouriteFragmentDirections
                .actionFavouriteFragmentToDetailCorporationFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun removeBySwipe() {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentCorp = myAdapter.currentList[viewHolder.adapterPosition]
                fragmentViewModel.changeStateCorp(currentCorp)
                fragmentViewModel.removeCorpFromFavourite(currentCorp)
            }
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun onObservers() {
        lifecycleScope.launch {
            fragmentViewModel.listFavouriteCorp
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collect {

                    if (it.isEmpty() && isNetworkAvailable.invoke()) {
                        Toast.makeText(
                            requireActivity(),
                            "Favourite list is empty",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.shimmerCardList.shimmer.visibility = View.GONE

                    } else if (!isNetworkAvailable.invoke()) {
                        Toast.makeText(
                            requireActivity(),
                            "Check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.shimmerCardList.shimmer.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE

                    } else {
                        binding.shimmerCardList.shimmer.visibility = View.GONE
                        binding.recycler.visibility = View.VISIBLE

                        myAdapter.submitList(it)
                    }
                }
        }
    }

    private fun initViews() {
        binding.recycler.adapter = myAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}