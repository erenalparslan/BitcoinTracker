package com.erenalparslan.bitcointracker.presentation.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erenalparslan.bitcointracker.presentation.activities.HomeActivity
import com.erenalparslan.bitcointracker.R
import com.erenalparslan.bitcointracker.common.viewBinding
import com.erenalparslan.bitcointracker.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding(FragmentRegisterBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


            goBackText.setOnClickListener {
                findNavController().navigate(R.id.loginFragment)
            }

            registerButton.setOnClickListener {
                val username = username.text.toString().trim()
                val email = email.text.toString().trim()
                val password = password.text.toString().trim()
                val confirmPassword = confirmPassword.text.toString().trim()

                if (password != confirmPassword) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    registerUser(username, email, password)
                } else {
                    Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun registerUser(username: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()

                        it.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    val intent = Intent(requireContext(), HomeActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}