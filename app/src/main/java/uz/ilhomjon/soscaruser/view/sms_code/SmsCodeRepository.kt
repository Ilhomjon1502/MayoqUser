package uz.ilhomjon.soscaruser.view.sms_code

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import uz.ilhomjon.soscaruser.models.User

class SmsCodeRepository {
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

    private val mutableStateFlow = MutableStateFlow<List<User>>(emptyList())

    suspend fun getAllUsers(): MutableStateFlow<List<User>> {

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = ArrayList<User>()
                for (child in snapshot.children) {
                    val user = child.getValue(User::class.java)
                    user?.let { users.add(it) }
                }
                mutableStateFlow.value = users
                Log.d("onDataChange", "onDataChange: ${mutableStateFlow.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SignInRepository", "onCancelled: ${error.message}")
            }
        })
        return mutableStateFlow
    }

    suspend fun addUser(user: User) {
        var userId = databaseReference.push().key ?: return
        databaseReference.child(userId).setValue(user)
    }


}