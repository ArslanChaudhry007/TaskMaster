import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.taskmaster.model.SignInUpResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    // Initialize Firebase Authentication
    private val auth = FirebaseAuth.getInstance()

    // LiveData for observing login results
    private val _loginResult = MutableLiveData<SignInUpResult>()
    val loginResult: LiveData<SignInUpResult> get() = _loginResult

    // Function to handle login with email and password
    fun loginWithEmailPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If login is successful, set the result as success
                        _loginResult.value = SignInUpResult(success = true, errorMessage = null)
                    } else {
                        // If login fails, extract error message or provide a default message
                        val errorMessage = task.exception?.message ?: "Login failed. Please try again later."
                        _loginResult.value = SignInUpResult(success = false, errorMessage)
                    }
                }
        }

    }
}
