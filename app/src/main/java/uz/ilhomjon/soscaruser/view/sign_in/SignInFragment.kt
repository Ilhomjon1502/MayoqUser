package uz.ilhomjon.soscaruser.view.sign_in

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                    MySharedPreference.setLogin(user.login.toString())
                    MySharedPreference.setPassword(user.parol.toString())
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }

        return binding.root
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}