package com.sugardaddy.colegioapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sugardaddy.colegioapp.R
import com.sugardaddy.colegioapp.data.model.Estudiante
import com.sugardaddy.colegioapp.data.repository.FirebaseRepository
import com.sugardaddy.colegioapp.utils.ValidationUtils

class EditarEstudianteActivity : AppCompatActivity() {
    private lateinit var edtNombre: EditText
    private lateinit var edtApellido: EditText
    private lateinit var edtNota: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var spGrado: Spinner
    private lateinit var spMateria: Spinner
    private lateinit var btnGuardar: Button

    private val firebaseRepository = FirebaseRepository()
    private lateinit var estudianteOriginal: Estudiante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_estudiante)

        edtNombre = findViewById(R.id.edtNombre)
        edtApellido = findViewById(R.id.edtApellido)
        edtNota = findViewById(R.id.edtNota)
        spGrado = findViewById(R.id.spGrado)
        spMateria = findViewById(R.id.spMateria)
        progressBar = findViewById(R.id.progressBar)
        btnGuardar = findViewById(R.id.btnGuardar)

        val grados = resources.getStringArray(R.array.opciones_grado)
        val materias = resources.getStringArray(R.array.opciones_materia)

        spGrado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grados)
        spMateria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, materias)

        // Obtener el estudiante desde el intent
        estudianteOriginal = intent.getSerializableExtra("estudiante") as Estudiante

        // Cargar los datos actuales
        edtNombre.setText(estudianteOriginal.nombre)
        edtApellido.setText(estudianteOriginal.apellido)
        edtNota.setText(estudianteOriginal.nota)
        spGrado.setSelection(grados.indexOf(estudianteOriginal.grado))
        spMateria.setSelection(materias.indexOf(estudianteOriginal.materia))

        btnGuardar.setOnClickListener {
            validarYConfirmar()
        }

        val btnRegresar = findViewById<ImageButton>(R.id.imgbRegresar)
        btnRegresar.setOnClickListener{
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun validarYConfirmar() {
        val nombre = edtNombre.text.toString().trim()
        val apellido = edtApellido.text.toString().trim()
        val nota = edtNota.text.toString().trim()
        val grado = spGrado.selectedItem.toString()
        val materia = spMateria.selectedItem.toString()

        if (!ValidationUtils.esNombreValido(nombre)) {
            edtNombre.error = "Nombre no válido"
            return
        }

        if (!ValidationUtils.esApellidoValido(apellido)) {
            edtApellido.error = "Apellido no válido"
            return
        }

        if (ValidationUtils.esNumeroCompleto(nota)) {
            edtNota.error = "Ingrese una nota entre 0 y 10"
            return
        }

        if (!ValidationUtils.esNumeroValido(nota)) {
            edtNota.error = "Nota inválida. Debe estar entre 0 y 10"
            return
        }

        // Validar si no se han hecho cambios
        val sinCambios = (
                nombre == estudianteOriginal.nombre &&
                        apellido == estudianteOriginal.apellido &&
                        nota == estudianteOriginal.nota &&
                        grado == estudianteOriginal.grado &&
                        materia == estudianteOriginal.materia
                )

        if (sinCambios) {
            Toast.makeText(this, "No hiciste ningún cambio", Toast.LENGTH_SHORT).show()
            return
        }

        // Confirmar antes de guardar
        AlertDialog.Builder(this)
            .setTitle("Confirmar edición")
            .setMessage("¿Deseas guardar los cambios?")
            .setPositiveButton("OK") { dialog, _ ->
                guardarCambios(nombre, apellido, nota, grado, materia)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun guardarCambios(
        nombre: String,
        apellido: String,
        nota: String,
        grado: String,
        materia: String
    ) {
        val nuevoEstudiante = Estudiante(
            id = estudianteOriginal.id,
            nombre = nombre,
            apellido = apellido,
            nota = nota,
            grado = grado,
            materia = materia
        )
        btnGuardar.isEnabled = false
        progressBar.visibility= View.VISIBLE
        Log.d("DEBUG", "Estudiante creado: $nuevoEstudiante")

        // Validar que no exista duplicado
        firebaseRepository.verificarDuplicadoEditado(nuevoEstudiante) { duplicado ->
            if (duplicado) {
                Toast.makeText(this, "Ya existe un estudiante con esos datos", Toast.LENGTH_SHORT).show()
            } else {
                firebaseRepository.actualizarEstudiante(nuevoEstudiante,
                    onSuccess = {
                        btnGuardar.isEnabled = true
                        progressBar.visibility=View.GONE
                        Log.i("INFO", "Estudiante actualizado exitosamente en Firebase")
                        mostrarPopup("Estudiante actualizado exitosamente")
                    },
                    onFailure = {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }
    private fun mostrarPopup(mensaje:String){
        AlertDialog.Builder(this)
            .setTitle("Exito")
            .setMessage(mensaje)
            .setPositiveButton("Ok") { dialog,_ -> dialog.dismiss()}
            .show()
    }
}