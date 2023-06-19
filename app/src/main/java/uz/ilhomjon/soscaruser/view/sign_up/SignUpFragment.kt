package uz.ilhomjon.soscaruser.view.sign_up

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentSignUpBinding
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.utils.MyData
import uz.ilhomjon.soscaruser.view.sms_code.SmsCodeRepository
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModel
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModelFactory
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class SignUpFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private val TAG = "SignUpFragment"
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var reference: StorageReference
    private var photoUri = ""
    private lateinit var smsCodeRepository: SmsCodeRepository
    private lateinit var signUpViewModelFactory: SignUpViewModelFactory
    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("user_images")

        smsCodeRepository = SmsCodeRepository()
        signUpViewModelFactory = SignUpViewModelFactory(smsCodeRepository)
        signUpViewModel =
            ViewModelProvider(this, signUpViewModelFactory)[SignUpViewModel::class.java]



        MySharedPreference.init(binding.root.context)
        var user: User? = null
        user = MySharedPreference.getUser()

        if (user.login != null && user.parol != null) {
            findNavController().popBackStack()
        }


        val list = ArrayList<User>()
        launch {
            signUpViewModel.getAllUsers().collectLatest {
                list.addAll(it)
            }
        }


        //Get image
        binding.photo.setOnClickListener {
            getImageContent.launch("image/*")
        }

        //save btn listener
        binding.nextBtn.setOnClickListener {
            val user = User(
                login = binding.edtLogin.text.toString(),
                parol = binding.edtPassword.text.toString(),
                bfd = binding.edtDate.text.toString(),
                address = binding.edtAddress.text.toString(),
                history = binding.edtHistory.text.toString(),
                phoneNumber = binding.edtPhoneNumber.text.toString(),
                imageLink = photoUri,
                fullName = binding.edtName.text.toString(),
                number = binding.edtNumber.text.toString(),
                lat = null,
                long = null
            )


            //Check number from firebase realtime
            if (list.filter { it.phoneNumber == user.phoneNumber }.isNotEmpty()) {
                Toast.makeText(
                    context,
                    "Bu raqam bilan oldin royhatdan otilgan",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            //NEXT FRAGMENT
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

        //Edit Text listener
        binding.showBtn.setOnClickListener {
            togglePasswordVisibility(binding.edtPassword)
        }
        return binding.root
    }


    //GetImage Content
    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult

            binding.photo.text = uri.path
            val uuid = UUID.randomUUID()
            val uploadTask = reference.child(uuid.toString()).putFile(uri)

            binding.progressBar.visibility = View.VISIBLE
            uploadTask.addOnSuccessListener {
                if (it.task.isSuccessful) {
                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { imageUri ->
                        photoUri = imageUri.toString()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }.addOnFailureListener {
                Log.d(TAG, "Get Image: ${it.message}")
                binding.progressBar.visibility = View.GONE
            }
        }


    //Password EditText visible and invisible
    private fun togglePasswordVisibility(editText: EditText) {
        val currentInputType = editText.inputType
        if (currentInputType == InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT) {
            // Show password
            editText.inputType =
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT
        } else {
            // Hide password
            editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        }
        // Move the cursor to the end of the text
        editText.setSelection(editText.text.length)
    }


    //coroutineScope
    override val coroutineContext: CoroutineContext
        get() = Job()
}