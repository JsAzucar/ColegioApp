package com.sugardaddy.colegioapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sugardaddy.colegioapp.R
import com.sugardaddy.colegioapp.data.model.Estudiante
import com.sugardaddy.colegioapp.data.repository.FirebaseRepository
import com.sugardaddy.colegioapp.ui.main.adapter.EstudianteAccionAdapter

class EditarEliminarEstudiantes : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EstudianteAccionAdapter
    private val firebaseRepository = FirebaseRepository()
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private var listaEstudiantes = listOf<Estudiante>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_eliminar_estudiantes)

        recyclerView = findViewById(R.id.rvEditarEliminar)
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter = EstudianteAccionAdapter(listaEstudiantes,
            onEditar = { estudiante ->
                val intent = Intent(this, EditarEstudianteActivity::class.java)
                intent.putExtra("estudiante", estudiante)
                editLauncher.launch(intent)
            },
            onEliminar = { estudiante ->
                mostrarConfirmacionEliminar(estudiante)
            })

        recyclerView.adapter = adapter

        cargarEstudiantes()

        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                cargarEstudiantes() // 👈 se actualiza
            }
        }


        val btnRegresar = findViewById<ImageButton>(R.id.imgbRegresar)
        btnRegresar.setOnClickListener{
            finish()
        }
    }

    private fun cargarEstudiantes() {
        firebaseRepository.obtenerTodosLosEstudiantes(
            onSuccess = {
                listaEstudiantes = it
                adapter.actualizarLista(it)
            },
            onFailure = {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun mostrarConfirmacionEliminar(estudiante: Estudiante) {
        AlertDialog.Builder(this)
            .setTitle("¿Eliminar?")
            .setMessage("¿Deseas eliminar a ${estudiante.nombre} ${estudiante.apellido}?")
            .setPositiveButton("OK") { _, _ ->
                firebaseRepository.eliminarEstudiante(estudiante.id,
                    onSuccess = {
                        cargarEstudiantes()
                        Toast.makeText(this, "Estudiante eliminado", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}
