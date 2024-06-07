package com.infocorp.presentation.logindisplay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.infocorp.R
import com.infocorp.databinding.FragmentLoginBinding
import com.infocorp.presentation.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw NullPointerException()

    private val fragmentViewModel: LoginViewModel by viewModels()

    private var updateStateBottomMenu: (() -> Unit)? = null
    private var updateStateBanner: (() -> Unit)? = null


    private val firebase by lazy {
        fragmentViewModel.getFirebase()
    }

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
        onBackPressed()
    }

    private fun onListeners() {

        binding.btnRegistration.setOnClickListener {
            val (email, password) = getInputText()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                    if (it.isSuccessful) {
                        Toast.makeText(requireActivity(),
                            ContextCompat.getString(requireActivity(),
                                R.string.registration_is_completed), Toast.LENGTH_SHORT).show()
                        clearFields()
                        goToGeneralFrag()

                    } else {
                        exceptionRegistration(it)
                    }
                }
            } else {
                Toast.makeText(requireActivity(),
                    ContextCompat.getString(requireActivity(),
                        R.string.error_input_layout), Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(requireActivity(),
                            ContextCompat.getString(requireActivity(),
                                R.string.unknown_user), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireActivity(),
                    ContextCompat.getString(requireActivity(),
                        R.string.error_input_layout), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun exceptionRegistration(it: Task<AuthResult>) {
        when (it.exception) {
            is FirebaseAuthWeakPasswordException -> {
                Toast.makeText(requireActivity(),
                    ContextCompat.getString(requireActivity(),
                        R.string.password_error), Toast.LENGTH_SHORT).show()
            }

            is FirebaseAuthInvalidCredentialsException -> {
                Toast.makeText(requireActivity(),
                    ContextCompat.getString(requireActivity(),
                        R.string.email_error), Toast.LENGTH_SHORT).show()
            }

            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(requireActivity(),
                   ContextCompat.getString(requireActivity(),
                       R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getInputText(): Pair<String, String> {
        val email = binding.etEmailUserInput.text.toString()
        val password = binding.etPasswordInput.text.toString()
        return fragmentViewModel.getEmailPassword(email, password)
    }

    private fun goToGeneralFrag() {
        val action = LoginFragmentDirections
            .actionLoginFragmentToGeneralFragment()
        findNavController().navigate(action)
    }

    private fun clearFields() {
        with(binding) {
            etEmailUserInput.text?.clear()
            etPasswordInput.text?.clear()
        }
    }

    private fun initViews() {
        updateStateBottomMenu?.invoke()
        updateStateBanner?.invoke()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}