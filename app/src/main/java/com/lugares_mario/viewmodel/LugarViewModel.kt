package com.lugares_mario.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lugares_mario.data.LugarDao
import com.lugares_mario.model.Lugar
import com.lugares_mario.repository.LugarRepository
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LugarRepository = LugarRepository(LugarDao())
    val getLugares = repository.getLugares

    fun saveLugar(lugar: Lugar){
        repository.addLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        repository.deleteLugar(lugar)
    }
}