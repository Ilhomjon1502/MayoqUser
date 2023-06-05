package uz.ilhomjon.soscaruser.view.sms_code

import com.google.firebase.database.FirebaseDatabase
import uz.ilhomjon.soscaruser.models.User

class SmsCodeRepository {
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

    suspend fun addUser(user: User) {
        var userId = databaseReference.push().key ?: return
        databaseReference.child(userId).setValue(user)
    }


}