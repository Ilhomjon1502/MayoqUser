package uz.ilhomjon.soscaruser.viewmodel.signinviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.soscaruser.models.User
import uz.ilhomjon.soscaruser.view.sign_in.SignInRepository
import uz.ilhomjon.soscaruser.view.sms_code.SmsCodeRepository

class SignInViewModel(private val signInRepository: SignInRepository) : ViewModel() {

    suspend fun getAllUsers(): MutableStateFlow<List<User>> {
        return signInRepository.getAllUsers()
    }
}