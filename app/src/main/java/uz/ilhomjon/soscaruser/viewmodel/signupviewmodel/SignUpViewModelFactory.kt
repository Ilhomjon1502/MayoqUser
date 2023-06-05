package uz.ilhomjon.soscaruser.viewmodel.signupviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.ilhomjon.soscaruser.view.sms_code.SmsCodeRepository

@Suppress("UNCHECKED_CAST", "UNREACHABLE_CODE")
class SignUpViewModelFactory(private val smsCodeRepository: SmsCodeRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(smsCodeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}