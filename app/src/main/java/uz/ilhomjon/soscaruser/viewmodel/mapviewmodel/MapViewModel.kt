package uz.ilhomjon.soscaruser.viewmodel.mapviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.ilhomjon.soscaruser.models.Call
import uz.ilhomjon.soscaruser.view.map.MapRepository

class MapViewModel(private val mapRepository: MapRepository):ViewModel() {

    fun addCall(call: Call){
        viewModelScope.launch {
            mapRepository.addCall(call)
        }
    }

    suspend fun getAllCalls():MutableStateFlow<List<Call>>{
        return mapRepository.getAllCalls()
    }
}