package com.sugardaddy.colegioapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sugardaddy.colegioapp.R
import com.sugardaddy.colegioapp.data.model.Estudiante
import com.sugardaddy.colegioapp.data.repository.FirebaseRepository
import com.sugardaddy.colegioapp.ui.main.adapter.EstudianteAdapter

class MostrarEstudiantes : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EstudianteAdapter
    private lateinit var spinnerGrado: Spinner
    private lateinit var spinnerMateria: Spinner

    private val firebaseRepository = FirebaseRepository()
    private var listaEstudiantes: List<Estudiante> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostrar_estudiantes)

        recyclerView = findViewById(R.id.rvEstudiantes)
        spinnerGrado = findViewById(R.id.spFiltroGrado)
        spinnerMateria = findViewById(R.id.spFiltroMateria)

        // Spinners
        val grados = resources.getStringArray(R.array.opciones_grado)
        val materias = resources.getStringArray(R.array.opciones_materia)

        spinnerGrado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grados)
        spinnerMateria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, materias)

        // RecyclerView
        adapter = EstudianteAdapter(listaEstudiantes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        // Escuchadores para filtrado
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filtrarEstudiantes()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerGrado.onItemSelectedListener = listener
        spinnerMateria.onItemSelectedListener = listener

        obtenerEstudiantes()

        val btnRegresar = findViewById<ImageButton>(R.id.imgbRegresar)
        btnRegresar.setOnClickListener{
            finish()
        }
    }
    private fun obtenerEstudiantes() {
        firebaseRepository.obtenerTodosLosEstudiantes(
            onSuccess = { estudiantes ->
                listaEstudiantes = estudiantes
                filtrarEstudiantes()
            },
            onFailure = { mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun filtrarEstudiantes() {
        val gradoSeleccionado = spinnerGrado.selectedItem.toString()
        val materiaSeleccionada = spinnerMateria.selectedItem.toString()

        Log.d("DEBUG-FILTRO", "Filtrando por: Grado=$gradoSeleccionado, Materia=$materiaSeleccionada")
        for (e in listaEstudiantes) {
            Log.d("DEBUG-FILTRO", "Estudiante: ${e.nombre} - Grado=${e.grado} - Materia=${e.materia}")
        }

        val filtrados = listaEstudiantes.filter {
            it.grado.trim() == gradoSeleccionado.trim() &&
                    it.materia.trim() == materiaSeleccionada.trim()
        }

        Log.d("DEBUG-FILTRO", "Total después del filtro: ${filtrados.size}")

        adapter.actualizarLista(filtrados)
    }
}