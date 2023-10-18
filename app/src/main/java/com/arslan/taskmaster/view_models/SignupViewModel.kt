import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.taskmaster.model.SignInUpResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _signupResult = MutableLiveData<SignInUpResult>()
    val signupResult: LiveData<SignInUpResult> get() = _signupResult

    fun signupWithEmailPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signupResult.value = SignInUpResult(success = true, errorMessage = null)
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthException -> {
                                // Handle specific Firebase Authentication exceptions here
                                // For example, invalid email, weak password, etc.
                                task.exception?.message
                            }
                            else -> "Signup failed. Please try again later."
                        }
                        _signupResult.value = SignInUpResult(success = false, errorMessage)
                    }
                }
        }

    }
}


