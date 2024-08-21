package myapplication.android.mindall.data.dto.userDto

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireBaseUserService {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var storageReference: StorageReference = FirebaseStorage.getInstance().reference

    companion object {
        private var INSTANCE: FireBaseUserService? = null

        fun getInstance(): FireBaseUserService? {
            if (INSTANCE == null) {
                INSTANCE = FireBaseUserService()
            }
            return INSTANCE
        }
    }
}