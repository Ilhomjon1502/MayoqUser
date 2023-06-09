package uz.ilhomjon.soscaruser.viewmodel.signinviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.ilhomjon.soscaruser.view.sign_in.SignInRepository

@Suppress("UNCHECKED_CAST", "UNREACHABLE_CODE")
class SignInViewModelFactory(private val signInRepository: SignInRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)){
            return SignInViewModel(signInRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}