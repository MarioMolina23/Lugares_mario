package com.lugares_mario.ui.lugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares_mario.R
import com.lugares_mario.databinding.FragmentUpdateLugarBinding
import com.lugares_mario.model.Lugar
import com.lugares_mario.viewmodel.LugarViewModel


class UpdateLugarFragment : Fragment() {

    //Se recupera un argumento pasado...
    private val args by navArgs<UpdateLugarFragmentArgs>()

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)
        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        //Se pasan los valores a los campos de la pantalla...
        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreoLugar.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)

        binding.btUpdateAgregar.setOnClickListener { updateLugar() }

        return binding.root

    }

    //Hacemos el registro del lugar en la base de datos
    private fun updateLugar () {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreoLugar.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if(nombre.isNotEmpty()) {
            val lugar = Lugar (args.lugar.id,nombre,correo,telefono,web,
                args.lugar.latitud,
                args.lugar.longitud,
                args.lugar.altura,
                args.lugar.rutaAudio,
                args.lugar.rutaImagen)
            lugarViewModel.saveLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_updated),
            Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        }else{ //No hay info de lugar
            Toast.makeText(requireContext(),getString(R.string.msg_data),
                Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}