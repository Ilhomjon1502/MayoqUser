package uz.ilhomjon.soscaruser.view.sign_in

import MySharedPreference
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentSignInBinding
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.utils.MyData
import uz.ilhomjon.soscaruser.viewmodel.signinviewmodel.SignInViewModel
import uz.ilhomjon.soscaruser.viewmodel.signinviewmodel.SignInViewModelFactory
import kotlin.coroutines.CoroutineContext

class SignInFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentSignInBinding.inflate(layoutInflater) }
    private lateinit var signInRepository: SignInRepository
    private lateinit var signInViewModelFactory: SignInViewModelFactory
    private lateinit var signInViewModel: SignInViewModel
    private val TAG = "SignInFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        findNavController().popBackStack()

        signInRepository = SignInRepository()
        signInViewModelFactory = SignInViewModelFactory(signInRepository)
        signInViewModel =
            ViewModelProvider(this, signInViewModelFactory)[SignInViewModel::class.java]

        val list = ArrayList<User>()
        launch {
            signInViewModel.getAllUsers().collectLatest {
                list.addAll(it)
                Log.d(TAG, "onCreateView: $list")
            }
        }

        binding.nextBtn.setOnClickListener {
            for (user in list) {
                if (user.login == binding.edtLogin.text.toString() && user.parol == binding.edtPassword.text.toString()) {
                    MySharedPreference.init(binding.root.context)
                    MySharedPreference.setUser(user)
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }

        binding.showImage.setOnClickListener {
            togglePasswordVisibility(binding.edtPassword)
        }

        return binding.root
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

    override val coroutineContext: CoroutineContext
        get() = Job()
}