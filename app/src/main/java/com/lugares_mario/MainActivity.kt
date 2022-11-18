package com.lugares_mario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares_mario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    //Cliente de autenticación de google
    private lateinit var googleSignInCliente: GoogleSignInClient

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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_r))
            .requestEmail()
            .build()
        googleSignInCliente = GoogleSignIn.getClient (this,gso)
        binding.btGoogle.setOnClickListener { googleSignIn() }

    }

    //Llamado a la función para autenticarse con Google
    private fun googleSignIn() {
        val signInIntent = googleSignInCliente.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle (idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    refresca(user)
                }else {
                    refresca(null)
                }

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(cuenta.idToken!!)
            } catch (e: ApiException){

            }
        }
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