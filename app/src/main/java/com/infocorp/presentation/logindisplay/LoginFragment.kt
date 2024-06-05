package com.infocorp.presentation.logindisplay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.infocorp.databinding.FragmentLoginBinding
import com.infocorp.presentation.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw NullPointerException()

    private var updateStateBottomMenu: (() -> Unit)? = null
    private var updateStateBanner: (() -> Unit)? = null

    @Inject
    lateinit var firebase: Firebase

    private val auth by lazy {
        firebase.auth
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateStateBottomMenu = { (activity as MainActivity).disableBottomMenu() }
        updateStateBanner = { (activity as MainActivity).disableBanner() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        onListeners()
    }

    private fun onListeners() {

        binding.btnRegistration.setOnClickListener {
            val (email, password) = getInputText()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireActivity(), "Registration is completed", Toast.LENGTH_SHORT)
                            .show()

                        clearFields()
                        goToGeneralFrag()

                    } else {
                        Toast.makeText(requireActivity(), "Registration is failure ", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        binding.btnLogIn.setOnClickListener {
            val (email, password) = getInputText()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        clearFields()
                        goToGeneralFrag()
                    } else {
                        Toast.makeText(requireActivity(), "Unknown user", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun getInputText(): Pair<String, String> {
        val email = binding.etEmailUserInput.text.toString()
        val password = binding.etPasswordInput.text.toString()
        return Pair(email, password)
    }

    private fun goToGeneralFrag() {
        val action = LoginFragmentDirections.actionLoginFragmentToGeneralFragment()
        findNavController().navigate(action)
    }

    private fun clearFields() {
        with(binding){
            etEmailUserInput.text?.clear()
            etPasswordInput.text?.clear()
        }
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
        updateStateBanner?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}