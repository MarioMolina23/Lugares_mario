package com.lugares_mario.ui.lugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lugares_mario.R
import com.lugares_mario.adapter.LugarAdapter
import com.lugares_mario.databinding.FragmentLugarBinding
import com.lugares_mario.viewmodel.LugarViewModel

class LugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)
        _binding = FragmentLugarBinding.inflate(inflater, container, false)

        binding.addLugarFabBt.setOnClickListener {
            findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
        }

        //Activar el RecyclerView
        val lugarAdapter=LugarAdapter()
        val reciclador = binding.reciclador
        reciclador.adapter = lugarAdapter
        reciclador.layoutManager = LinearLayoutManager(requireContext())

        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]

        lugarViewModel.getLugares.observe(viewLifecycleOwner) {
                lugares -> lugarAdapter.setLugares(lugares)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}