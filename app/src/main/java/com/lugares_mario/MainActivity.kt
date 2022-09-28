package com.lugares_mario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares_mario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Definimos un objeto para acceder a la autentificación de Firebase
    private lateinit var auth : FirebaseAuth

    //Definimos un objeto para acceder a los elementos de la pantalla xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializar la autenticación
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //Definir el evento onClick del boton "Register"
        binding.btRegister.setOnClickListener { hacerRegistro() }

        //Definir el evento onClick del boton "Login"
        binding.btLogin.setOnClickListener { hacerLogin() }

    }

    private fun hacerRegistro() {
        //Recupero la información que el usuario escribió en la App
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Utilizo el objeto "auth" para hacer el registro
        auth.createUserWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //Si se logra, se crea el usuario
                    val user = auth.currentUser
                    refresca(user)
                }else{ //Si no se logra, es porque hay un error
                    Toast.makeText(baseContext, "Falló",Toast.LENGTH_LONG).show() //Así es como se muestra un msj
                    refresca(null)
                }
            }
    }

    private fun refresca(user: FirebaseUser?) {
        if (user != null) { //Si hay un usuario entonces paso a la pantalla principal
            val intent = Intent(this,Principal::class.java)
            startActivity(intent)
        }
    }

    private fun hacerLogin() {
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Se hace el login
        auth.signInWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Autenticando", "Se autenticó")
                    val user = auth.currentUser
                    refresca(user)
                }else{
                    Log.e("Autenticando", "Error de Autenticación")
                    Toast.makeText(baseContext, "Falló",Toast.LENGTH_LONG).show() //Así es como se muestra un msj
                    refresca(null)
                }
            }
        Log.d("Autenticando","Sale del proceso...")
    }

    //Esto hará que una vez autenticado, no pida autenticarse al menos que haya cerrado sesión.
    public override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        refresca(usuario)
    }
}