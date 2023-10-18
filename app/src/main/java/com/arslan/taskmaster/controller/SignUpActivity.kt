package com.arslan.taskmaster.controller
import SignupViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.arslan.taskmaster.R
import com.arslan.taskmaster.constants.*
import com.arslan.taskmaster.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
     lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        initListeners()
        viewModelObserver()

    }

    private fun initListeners() {
        binding.sgnUpBtn.setOnClickListener(this)
    }
    private fun viewModelObserver() {
        // Observe signup success
        viewModel.signupResult.observe(this, Observer { result ->
            HideProgressDialog()
            if (result.success) {
                showToast(this, "Account created successfull")
                finish()
            } else {
                // Signup failed, display the error message to the user
                val errorMessage = result.errorMessage ?: "Signup failed. Please try again later."
                showToast(this, errorMessage)
            }
        })
    }

    override fun onClick(view: View?) {
       when(view?.id){
           R.id.sgnUpBtn -> {
               hideKeyboard()
               if(validation()){
                   if (checkNetworkConnectivity(this)) {
                       ShowProgressDialog(this,"Loading", "Please wait...")
                       lifecycleScope.launch{
                           viewModel.signupWithEmailPassword(
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
        if (binding.userId.text.toString().trim().isEmpty()){
            binding.userIDLayout.error = "Enter Email"
            binding.userId.disableError(binding.userIDLayout)
            check = false

        } else if (!isEmailValid( binding.userId.text.toString().trim())){
            binding.userIDLayout.error = "Enter valid email address"
            binding.userId.disableError(binding.userIDLayout)
            check = false
        }

        if (binding.passwordId.text.toString().trim().isEmpty()){
            binding.userPasswordLayout.error = "Enter Password"
            binding.passwordId.disableError(binding.userPasswordLayout)
            check = false

        } else if (binding.passwordId.text.toString().trim().length < 6){
            binding.userPasswordLayout.error = "Password must be at least 6 characters"
            binding.passwordId.disableError(binding.userPasswordLayout)
            check = false
        }
        return check
    }
}