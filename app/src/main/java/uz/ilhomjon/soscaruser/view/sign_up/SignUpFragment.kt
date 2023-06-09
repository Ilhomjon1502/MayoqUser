package uz.ilhomjon.soscaruser.view.sign_up

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentSignUpBinding
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.utils.MyData
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModel
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModelFactory
import java.util.UUID

class SignUpFragment : Fragment() {

    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private val TAG = "SignUpFragment"
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var reference: StorageReference
    private var photoUri = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("user_images")

        binding.photo.setOnClickListener {
            getImageContent.launch("image/*")
        }
        binding.nextBtn.setOnClickListener {
            val user = User(
                login = binding.edtLogin.text.toString(),
                parol = binding.edtPassword.text.toString(),
                bfd = binding.edtDate.text.toString(),
                address = binding.edtAddress.text.toString(),
                history = binding.edtHistory.text.toString(),
                phoneNumber = binding.edtPhoneNumber.text.toString(),
                imageLink = photoUri
            )

            if (user.login != "" && user.parol != "" && user.phoneNumber != "" && user.bfd != "" && user.address != "" && user.history != "" && user.imageLink != "") {
                MyData.user = user
                findNavController().navigate(R.id.smsCodeFragment)
            } else {
                Toast.makeText(
                    context,
                    "Iltimos hamma maydonlarni to'ldiring.!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult

            val uuid = UUID.randomUUID()
            val uploadTask = reference.child(uuid.toString()).putFile(uri)

            uploadTask.addOnSuccessListener {
                if (it.task.isSuccessful) {
                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { imageUri ->
                        photoUri = imageUri.toString()
                    }
                }
            }.addOnFailureListener {
                Log.d(TAG, "Get Image: ${it.message}")
            }
        }
}