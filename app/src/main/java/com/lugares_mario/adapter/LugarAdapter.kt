package com.lugares_mario.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares_mario.databinding.LugarFilaBinding
import com.lugares_mario.model.Lugar
import com.lugares_mario.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>(){

    //Una lista para almacenar la informacion de los lugares
    private var listaLugares = emptyList<Lugar>()

    //Contenedor de vistas "Cajitas" en  memoria...
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding) :
    RecyclerView.ViewHolder(itemBinding.root){
        fun dibuja (lugar: Lugar) {
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvWeb.text = lugar.web

            Glide.with(itemBinding.root.context)
                .load(lugar.rutaImagen)
                .circleCrop()
                .into(itemBinding.imagen)

            itemBinding.vistaFila.setOnClickListener {
                val accion = LugarFragmentDirections
                    .actionNavLugarToUpdateLugarFragment(lugar)
                itemView.findNavController().navigate(accion)
            }
        }
    }

    //Para crear una "vista" de cada fila de lugares
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding
            .inflate(LayoutInflater.from(parent.context)
                , parent
                , false)
        return LugarViewHolder(itemBinding)
    }

    //Para "dibujar" la informacion de cada lugar...
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugarActual = listaLugares[position]
        holder.dibuja(lugarActual)
    }

    override fun getItemCount(): Int {
        return listaLugares.size
    }

    fun setLugares(lugares: List<Lugar>) {
        listaLugares = lugares
        notifyDataSetChanged() //Provoca que se redibuje la lista
    }
}

//Para que se refleje el ajuste...