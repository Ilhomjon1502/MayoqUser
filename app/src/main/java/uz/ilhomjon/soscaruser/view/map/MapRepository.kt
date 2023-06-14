package uz.ilhomjon.soscaruser.view.map

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import uz.ilhomjon.soscaruser.models.User

class MapRepository {

    private val databaseReference = FirebaseDatabase.getInstance().reference.child("emergency_calls")
    private val mutableStateFlow = MutableStateFlow<List<User>>(emptyList())


}