package com.arslan.taskmaster.constants
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.arslan.taskmaster.R
import com.arslan.taskmaster.model.IntentParams
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

fun EditText.disableError(parent: TextInputLayout) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                if (parent != null) {
                    if (parent.isErrorEnabled == true) {
                        if (s.toString().length > 0) {
                            parent.isErrorEnabled = false

                        }
                    }
                }
            }
        }
    })
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

var loadingBar: ProgressDialog? = null
fun ShowProgressDialog(context: Activity?, title: String?, msg: String?) {
    loadingBar = ProgressDialog(context)
    loadingBar!!.dismiss()
    loadingBar!!.setTitle(title)
    loadingBar!!.setMessage(msg)
    loadingBar!!.setCanceledOnTouchOutside(false)
    loadingBar!!.show()
}

fun HideProgressDialog() {
    loadingBar!!.dismiss()
}

fun checkNetworkConnectivity(context: Context): Boolean {
    val cm= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting && (netInfo.type == ConnectivityManager.TYPE_MOBILE || netInfo.type == ConnectivityManager.TYPE_WIFI)
}

fun Activity.hideKeyboard() {
    try {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0)

    } catch (exc: java.lang.Exception) {
        print(exc)
    }
}

 fun isEmailValid(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}

inline fun <reified T : Activity> Activity.navigate(
    params: List<IntentParams> = emptyList(),
    finish: Boolean = true,
    anim: String = ""
) {
    val intent = Intent(this, T::class.java)
    for (parameter in params) {
        intent.putExtra(parameter.key, parameter.value)
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
    if (finish) {
        finish()
    }
    if (anim.equals(SLIDE_RIGHT)) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}

fun getCurrentUser(): FirebaseUser? {
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser
}
