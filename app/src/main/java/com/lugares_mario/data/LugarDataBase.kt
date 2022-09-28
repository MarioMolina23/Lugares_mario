package com.lugares_mario.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lugares_mario.model.Lugar

@Database(entities = [Lugar::class], version=1, exportSchema = false)
abstract class LugarDataBase : RoomDatabase() {
    abstract fun lugarDao() : LugarDao

    companion object {
        @Volatile
        private var INSTANCE: LugarDataBase? = null

        fun getDatabase(context: android.content.Context) : LugarDataBase {
            var temp = INSTANCE
            if (temp != null){
                return temp
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LugarDataBase::class.java,
                    "lugar_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}