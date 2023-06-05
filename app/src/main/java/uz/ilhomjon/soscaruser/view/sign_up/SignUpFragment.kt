package uz.ilhomjon.soscaruser.view.sign_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.soscaruser.R
import uz.ilhomjon.soscaruser.databinding.FragmentSignUpBinding
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.utils.MyData
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModel
import uz.ilhomjon.soscaruser.viewmodel.signupviewmodel.SignUpViewModelFactory

class SignUpFragment : Fragment() {

    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding.nextBtn.setOnClickListener {

            val user = User(
                login = binding.edtLogin.text.toString(),
                parol = binding.edtPassword.text.toString(),
                bfd = binding.edtDate.text.toString(),
                address = binding.edtAddress.text.toString(),
                history = binding.edtHistory.text.toString(),
                phoneNumber = binding.edtPhoneNumber.text.toString(),
                imageLink = "salom"
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
}