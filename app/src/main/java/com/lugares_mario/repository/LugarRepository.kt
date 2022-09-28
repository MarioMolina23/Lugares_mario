package com.lugares_mario.repository

import androidx.lifecycle.LiveData
import com.lugares_mario.data.LugarDao
import com.lugares_mario.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    suspend fun addLugar(lugar: Lugar){
        lugarDao.addLugar(lugar)
    }

    suspend fun updateLugar(lugar: Lugar) {
        lugarDao.updateLugar(lugar)
    }

    suspend fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }

    val getLugares : LiveData<List<Lugar>> = lugarDao.getLugares()
}