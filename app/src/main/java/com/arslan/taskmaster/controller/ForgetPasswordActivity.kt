package com.arslan.taskmaster.controller

import ForgotPasswordViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.arslan.taskmaster.R
import com.arslan.taskmaster.constants.*
import com.arslan.taskmaster.databinding.ActivityForgetPasswordBinding
import kotlinx.coroutines.launch

class ForgetPasswordActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]

        // Set up listeners and observers
        initListeners()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewModel.resetPasswordResult.observe(this) { result ->
            HideProgressDialog()

            if (result.success) {
                // Password reset email sent successfully
                showToast(this, "Password reset email sent. Check your email.")
                finish()
            } else {
                // Password reset email sending failed
                showToast(
                    this,
                    result.errorMessage
                        ?: "Password reset email could not be sent. Please try again later."
                )
            }
        }
    }

    private fun initListeners() {
        binding.forgotPasswordId.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.forgotPasswordId -> {
                // Hide the keyboard
                hideKeyboard()
                if (validation()) {
                    if (checkNetworkConnectivity(this)) {
                        // Show a progress dialog
                        ShowProgressDialog(this, "Loading", "Please wait...")
                        lifecycleScope.launch {
                            viewModel.sendPasswordResetEmail(binding.userId.text.toString().trim())
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

        return check
    }
}
