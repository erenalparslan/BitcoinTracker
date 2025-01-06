package com.erenalparslan.bitcointracker.presentation.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erenalparslan.bitcointracker.HomeActivity
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

            // Kayıt ol butonuna tıklandığında kullanıcıyı kaydet
            registerButton.setOnClickListener {
                val username = username.text.toString().trim()
                val email = email.text.toString().trim()
                val password = password.text.toString().trim()
                val confirmPassword = confirmPassword.text.toString().trim()

                // Şifrelerin uyuştuğundan emin ol
                if (password != confirmPassword) {
                    Toast.makeText(requireContext(), "Şifreler uyuşmuyor", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    registerUser(username, email, password)
                } else {
                    Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun registerUser(username: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Kayıt başarılı ise kullanıcıyı yönlendir
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        // Firebase'e kullanıcı adını eklemek için kullanıcı profilini güncelle
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
                    // Kayıt başarısızsa hata mesajı göster
                    Toast.makeText(
                        requireContext(),
                        "Kayıt başarısız: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}