package com.lugares_mario.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.lugares_mario.model.Lugar

class LugarDao {

    //Valores para la estructura de firestore cloud
    private val coleccion1 = "lugaresApp"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    //pirvate val usuario = Firebase.auth.currentUser?.uid
    private val coleccion2 = "misLugares"

    //Objeto para la "conexion" de la base de datos en la nube
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    //private var codigoUsuario: String


    init{ //Inicializa la conexion con Firestore para poder trabajar...
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //CRUD
    fun getLugares() : MutableLiveData<List<Lugar>>{
        val listaLugares = MutableLiveData<List<Lugar>>()

        firestore
            .collection(coleccion1)
            .document(usuario)
            .collection(coleccion2)
            .addSnapshotListener{ instantanea, error ->
                if (error != null) { //Se materializo algun error en la generacion de la vista/instantanea
                    return@addSnapshotListener
                }
                //Si estamos en esta lista... entonces si se tomo la instantanea
                if (instantanea != null) { //Hay datos en la instantanea
                    val lista = ArrayList<Lugar>()

                    //Se recorre la instantanea para transformar cada documento en un objeto lugar

                    val lugares = instantanea.documents
                    lugares.forEach {
                        val lugar = it.toObject(Lugar::class.java)
                        if (lugar!=null){
                            lista.add(lugar)
                        }
                    }
                    listaLugares.value = lista
                }
            }
        return listaLugares
    }
    //Se recibe un objeto lugar, se valida si el "id" tiene algo... es una actializacion, sino se crea.
    fun saveLugar(lugar: Lugar) {
        val document: DocumentReference
        if (lugar.id.isEmpty()) { //Si esta vacion, es un nuevo documento
            document = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document()
            lugar.id = document.id
        } else { //Si el id tenia algo... entonces ubico ese id como el documento...
            document = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(lugar.id)
        }
        //Ahora si se va a registrar la info (nueva o actualiza)
        //"Registra" la actualizacion
        val set = document.set(lugar)
        set.addOnSuccessListener {
            Log.d("saveLugar","Lugar agregado/actualizado")
        }
            .addOnCanceledListener {
                Log.e("saveLugar","Lugar NO agregado/actualizado")
            }
    }

    fun deleteLugar(lugar: Lugar) {
        if (lugar.id.isNotEmpty()) { //Se valida si el Id tiene algo
            firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(lugar.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("deleteLugar","Lugar eliminado")
                }
                .addOnCanceledListener {
                    Log.e("deleteLugar","Lugar NO eliminado")
                }
        }
    }
}