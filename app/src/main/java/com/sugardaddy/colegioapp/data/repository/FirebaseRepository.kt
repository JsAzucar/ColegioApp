package com.sugardaddy.colegioapp.data.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sugardaddy.colegioapp.data.model.Estudiante

class FirebaseRepository {


    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("estudiantes")


    fun guardarEstudiante(estudiante: Estudiante, onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        val query = dbRef.orderByChild("nombre").equalTo(estudiante.nombre)

        query.get().addOnSuccessListener { snapchot ->
            var estudianteExiste = false

            for(child in snapchot.children){
                val estudianteExistente = child.getValue(Estudiante::class.java)

                if(estudianteExistente != null && estudianteExistente.apellido == estudiante.apellido &&
                    estudianteExistente.grado == estudiante.grado){
                    estudianteExiste = true
                    break
                }
            }

            if(estudianteExiste){
                onFailure("El estudiante ya esta registrado en este grado.")
            }else{
                val userId = dbRef.push().key ?: return@addOnSuccessListener
                val nuevoEstudiante = estudiante.copy(id = userId)

                dbRef.child(userId).setValue(nuevoEstudiante)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { exception -> onFailure(exception.message ?: "Error desconocido") }
            }

        }.addOnFailureListener{ exception ->
            onFailure("Error al verificar duplicados: ${exception.message}")
        }

    }
    fun obtenerTodosLosEstudiantes(
        onSuccess: (List<Estudiante>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        dbRef.get().addOnSuccessListener { snapshot ->
            val lista = mutableListOf<Estudiante>()
            for (child in snapshot.children) {
                val estudiante = child.getValue(Estudiante::class.java)
                Log.d("DEBUG-FIREBASE", "Recibido: $estudiante")
                if (estudiante != null) lista.add(estudiante)
            }

            Log.d("DEBUG-FIREBASE", "Total recibidos: ${lista.size}") // 👈 AQUI
            onSuccess(lista)
        }.addOnFailureListener { error ->
            onFailure(error.message ?: "Error al leer los datos")
        }
    }
}