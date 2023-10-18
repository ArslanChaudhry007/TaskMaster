import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _resetPasswordResult = MutableLiveData<ResetPasswordResult>()
    val resetPasswordResult: LiveData<ResetPasswordResult> get() = _resetPasswordResult

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _resetPasswordResult.value = ResetPasswordResult(success = true, errorMessage = null)
                    } else {
                        val errorMessage = task.exception?.message ?: "Password reset email could not be sent. Please try again later."
                        _resetPasswordResult.value = ResetPasswordResult(success = false, errorMessage)
                    }
                }
        }

    }
}

data class ResetPasswordResult(val success: Boolean, val errorMessage: String?)
