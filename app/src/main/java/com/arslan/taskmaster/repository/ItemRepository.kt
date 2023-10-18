import com.arslan.taskmaster.constants.ServerMessage
import com.arslan.taskmaster.constants.getCurrentUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ItemRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection: CollectionReference = db.collection("users")

    fun getItemsForCurrentUser(callback: (List<Item>?, Exception?) -> Unit) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val uid = currentUser.uid
            db.collection("users")
                .document(uid)
                .collection("items")
                .get()
                .addOnSuccessListener { result ->
                    val items = mutableListOf<Item>()
                    for (document in result) {
                        val item = document.toObject(Item::class.java)
                        item.id = document.id
                        items.add(item)
                    }
                    callback(items, null)
                }
                .addOnFailureListener { exception ->
                    callback(null, exception)
                }
        } else {
            callback(null, Exception(ServerMessage))
        }
    }

    fun deleteItem(itemId: String, callback: (Boolean, String?) -> Unit) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val uid = currentUser.uid
            usersCollection
                .document(uid)
                .collection("items")
                .document(itemId)
                .delete()
                .addOnSuccessListener {
                    callback(true, null) // Deletion successful
                }
                .addOnFailureListener { exception ->
                    callback(false, exception.message) // Deletion failed with exception
                }
        } else {
            callback(false, ServerMessage) // User not logged in
        }
    }


    fun updateItem(item: Item, callback: (Boolean, String?) -> Unit) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val uid = currentUser.uid
            val productId = item.id
            if (productId != null) {
                val itemData = mapOf(
                    "productName" to item.productName,
                    "productQuantity" to item.productQuantity,
                    "productPrice" to item.productPrice,
                    "productDescription" to item.productDescription
                )

                usersCollection
                    .document(uid)
                    .collection("items")
                    .document(productId)
                    .update(itemData)
                    .addOnSuccessListener {
                        callback(true, null) // Item updated successfully
                    }
                    .addOnFailureListener { exception ->
                        callback(false, exception.message) // Error message in case of failure
                    }
            } else {
                callback(false, ServerMessage)
            }
        }
    }


}
