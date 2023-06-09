package uz.ilhomjon.soscaruser.view.sms_code

import MySharedPreference
import android.os.Bundle
import android.provider.Telephony.Sms
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentSmsCodeBinding
import uz.ilhomjon.soscaruser.utils.MyData
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModel
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModelFactory
import java.util.concurrent.TimeUnit


class SmsCodeFragment : Fragment() {

    private val binding by lazy { FragmentSmsCodeBinding.inflate(layoutInflater) }
    private lateinit var smsCodeRepository: SmsCodeRepository
    private lateinit var signUpViewModelFactory: SignUpViewModelFactory
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private val TAG = "SmsCodeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        smsCodeRepository = SmsCodeRepository()
        signUpViewModelFactory = SignUpViewModelFactory(smsCodeRepository)
        signUpViewModel =
            ViewModelProvider(this, signUpViewModelFactory)[SignUpViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        var phoneNumber = MyData.user!!.phoneNumber
        sentVerificationCode(phoneNumber.toString())

        binding.nextBtn.setOnClickListener {
            var text = binding.edtPassword.text.toString()
            try {
                verifyCode(text)
            } catch (e: Exception) {
                Log.d(TAG, "onCreateView: ${e.message}")
            }
        }
        return binding.root
    }

    private fun verifyCode(code: String) {
        if (code.length == 6) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        }
    }

    //send verification code
    fun sentVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(requireActivity())
            .setCallbacks(callbacks).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    //callback
    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String, token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    //Signin with credential
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")

                val user = task.result?.user
                Toast.makeText(binding.root.context, "Successful", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "signInWithPhoneAuthCredential: ${user!!.phoneNumber}")
                findNavController().navigate(R.id.registerInfoFragment)
                signUpViewModel.addUser(MyData.user!!)

                //Shared
                MySharedPreference.init(binding.root.context)
                MySharedPreference.setLogin(MyData.user!!.login.toString())
                MySharedPreference.setPassword(MyData.user!!.parol.toString())
            } else {
                // Sign in failed, display a message and update the UI
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(
                        binding.root.context, "Password is invalid", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //true
                    Log.d(TAG, "signInWithPhoneAuthCredential: Yeah")
                    Toast.makeText(binding.root.context, "Correct!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}