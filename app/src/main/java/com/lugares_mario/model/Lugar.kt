package com.lugares_mario.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName="Lugar")
data class Lugar(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name="nombre")
    val nombre: String,
    @ColumnInfo(name="correo")
    val correo: String?,
    @ColumnInfo(name="telefono")
    val telefono: String?,
    @ColumnInfo(name="web")
    val web: String?,
    @ColumnInfo(name="latitud")
    val latitud: Double?,
    @ColumnInfo(name="longitud")
    val longitud: Double?,
    @ColumnInfo(name="altura")
    val altura: Double?,
    @ColumnInfo(name="ruta_audio")
    val rutaAudio: String?,
    @ColumnInfo(name="ruta_imagen")
    val rutaImagen: String?,
) : Parcelable //"Traducir" = Se utiliza para que nosotros podamos pasar informacion de una pantalla a otra.