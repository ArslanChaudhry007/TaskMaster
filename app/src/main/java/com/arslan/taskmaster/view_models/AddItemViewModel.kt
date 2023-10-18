import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.taskmaster.constants.getCurrentUser
import com.arslan.taskmaster.interfaces.UploadResultCallback
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun uploadItem(
        productName: String,
        productQuantity: String,
        productPrice: String,
        productDescription: String,
        callback: UploadResultCallback // Pass the callback as a parameter
    ) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val uid = currentUser.uid
            val itemData = hashMapOf(
                "productName" to productName,
                "productQuantity" to productQuantity,
                "productPrice" to productPrice,
                "productDescription" to productDescription
            )

            viewModelScope.launch(Dispatchers.IO){
                db.collection("users")
                    .document(uid)
                    .collection("items")
                    .add(itemData)
                    .addOnSuccessListener {
                        // Item upload was successful
                        callback.onSuccess()
                    }
                    .addOnFailureListener { e ->
                        // Item upload failed
                        callback.onFailure(e)
                    }
            }

        }
    }
}
