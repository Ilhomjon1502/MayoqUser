package uz.ilhomjon.soscaruser.viewmodel.signupviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.view.sms_code.SmsCodeRepository

class SignUpViewModel(private val smsCodeRepository: SmsCodeRepository) : ViewModel() {
    fun addUser(user: User) {
        viewModelScope.launch {
            smsCodeRepository.addUser(user)
        }
    }
}