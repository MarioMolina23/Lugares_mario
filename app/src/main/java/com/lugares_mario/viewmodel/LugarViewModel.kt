package com.lugares_mario.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lugares_mario.data.LugarDataBase
import com.lugares_mario.model.Lugar
import com.lugares_mario.repository.LugarRepository
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LugarRepository
    val getLugares: LiveData<List<Lugar>>

    init {
        val lugarDao = LugarDataBase.getDatabase(application).lugarDao()
        repository = LugarRepository(lugarDao)
        getLugares = repository.getLugares
    }

    suspend fun addLugar(lugar: Lugar){
        viewModelScope.launch { repository.addLugar(lugar) }
    }

    suspend fun updateLugar(lugar: Lugar) {
        viewModelScope.launch { repository.updateLugar(lugar) }
    }

    suspend fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch { repository.deleteLugar(lugar) }
    }

}