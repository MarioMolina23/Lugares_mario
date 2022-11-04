package com.lugares_mario.repository

import androidx.lifecycle.MutableLiveData
import com.lugares_mario.data.LugarDao
import com.lugares_mario.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    val getLugares : MutableLiveData<List<Lugar>> = lugarDao.getLugares()


    fun addLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }

    fun updateLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }

}