package com.sugardaddy.colegioapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sugardaddy.colegioapp.R
import com.sugardaddy.colegioapp.data.model.Estudiante
import com.sugardaddy.colegioapp.data.model.PromedioEstudiante
import com.sugardaddy.colegioapp.data.repository.FirebaseRepository
import com.sugardaddy.colegioapp.ui.main.adapter.PromedioAdapter

class MostrarNotaGlobal : AppCompatActivity() {

    private lateinit var spinnerGrado: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromedioAdapter

    private val firebaseRepository = FirebaseRepository()
    private var estudiantesPorGrado = listOf<Estudiante>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_nota_global)

        spinnerGrado = findViewById(R.id.spFiltroGrado)
        recyclerView = findViewById(R.id.rvPromedios)

        val grados = resources.getStringArray(R.array.opciones_grado)
        spinnerGrado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grados)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PromedioAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )


        spinnerGrado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val gradoSeleccionado = spinnerGrado.selectedItem.toString()
                cargarPromedios(gradoSeleccionado)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val btnRegresar = findViewById<ImageButton>(R.id.imgbRegresar)
        btnRegresar.setOnClickListener{
            finish()
        }
    }

    private fun cargarPromedios(grado: String) {
        firebaseRepository.obtenerTodosLosEstudiantes(
            onSuccess = { lista ->
                // Agrupar por nombre + apellido
                val estudiantes = lista.filter { it.grado == grado }

                val agrupados = estudiantes.groupBy { it.nombre to it.apellido }

                val promedios = agrupados.map { (clave, listaNotas) ->
                    val materias = resources.getStringArray(R.array.opciones_materia)
                    val notasMap = mutableMapOf<String, Double>()

                    // Inicializar todas las materias en 0
                    for (materia in materias) {
                        notasMap[materia] = 0.0
                    }

                    for (e in listaNotas) {
                        val notaDouble = e.nota.toDoubleOrNull() ?: 0.0
                        notasMap[e.materia] = notaDouble
                    }

                    val promedio = notasMap.values.average()

                    PromedioEstudiante(
                        nombreCompleto = "${clave.first} ${clave.second}",
                        grado = grado,
                        promedio = promedio
                    )
                }

                adapter.actualizarLista(promedios)
            },
            onFailure = {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
