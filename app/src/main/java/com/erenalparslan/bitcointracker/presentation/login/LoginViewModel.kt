package com.erenalparslan.bitcointracker.presentation.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.erenalparslan.bitcointracker.common.Constants.LOGIN_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _loginState = MutableStateFlow<Boolean>(false)
    val loginState: StateFlow<Boolean> = _loginState

    init {
        checkLoginStatus()
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sharedPreferences.edit().putBoolean(LOGIN_KEY, true).apply()
                    _loginState.value = true
                } else {
                    _loginState.value = false
                }
            }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = sharedPreferences.getBoolean(LOGIN_KEY, false)
        _loginState.value = isLoggedIn
    }
}
