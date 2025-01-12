package com.erenalparslan.bitcointracker.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.erenalparslan.bitcointracker.presentation.activities.HomeActivity
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            loginButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.loginUser(email, password)
                } else {
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    Toast.makeText(
                        requireContext(),
                        "Please enter email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            registerButton.setOnClickListener {
                findNavController().navigate(R.id.registerFragment)
            }

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.loginState.collect { isLoggedIn ->
                        if (isLoggedIn) {
                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

    }


}