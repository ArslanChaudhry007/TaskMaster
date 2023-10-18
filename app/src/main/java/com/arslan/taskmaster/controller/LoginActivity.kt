package com.arslan.taskmaster.controller

import LoginViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.arslan.taskmaster.R
import com.arslan.taskmaster.constants.*
import com.arslan.taskmaster.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    val sharedPref = SharedPrefUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Set up listeners and observers
        initListeners()
        viewModelObserver()
    }

    override fun onStart() {
        super.onStart()
        checkSaveUser()
    }

    private fun checkSaveUser() {
        if (sharedPref.getSharedPrefValue(this, SAVE_LOGIN) != null){
            if (sharedPref.getSharedPrefValue(this, SAVE_LOGIN).equals("true")){
                val currentUser =getCurrentUser()
                if (currentUser != null) {
                    navigate<HomeActivity>()
                }
            }
        }

    }

    private fun viewModelObserver() {
        // Observe login result
        viewModel.loginResult.observe(this, Observer { result ->
            HideProgressDialog()
            if (result.success) {
                // Login was successful, navigate to the dashboard or the next screen
               // showToast(this, "Successful")
                sharedPref.saveSharedPrefValue(this,SAVE_LOGIN,"true")
                navigate<HomeActivity>()
            } else {
                // Login failed, display the error message to the user
                val errorMessage = result.errorMessage ?: "Login failed. Please try again later."
                showToast(this, errorMessage)
            }
        })
    }

    private fun initListeners() {
        // Set click listeners for buttons
        binding.signUp.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
        binding.forgotPassword.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.signUp -> {
                // Navigate to the sign-up screen
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.forgotPassword -> {
                // Navigate to the password forgot screen
                startActivity(Intent(this, ForgetPasswordActivity::class.java))
            }
            R.id.loginBtn -> {
                // Hide the keyboard
                hideKeyboard()
                // Validate user input before attempting login
                if (validation()) {
                    if (checkNetworkConnectivity(this)) {
                        // Show a progress dialog
                        ShowProgressDialog(this, "Loading", "Please wait...")
                        // Attempt login
                        lifecycleScope.launch{
                            viewModel.loginWithEmailPassword(
                                binding.userId.text.toString().trim(),
                                binding.passwordId.text.toString().trim()
                            )
                        }

                    } else {
                        showToast(this, CHECK_INTERNET)
                    }
                }
            }
        }
    }

    private fun validation(): Boolean {
        var check = true
        if (binding.userId.text.toString().trim().isEmpty()) {
            binding.userIDLayout.error = "Enter Email"
            binding.userId.disableError(binding.userIDLayout)
            check = false
        }

        if (binding.passwordId.text.toString().trim().isEmpty()) {
            binding.userPasswordLayout.error = "Enter Password"
            binding.passwordId.disableError(binding.userPasswordLayout)
            check = false
        }
        return check
    }
}
