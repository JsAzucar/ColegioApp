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

class RegistrarNotas : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var notaEditText: EditText
    private lateinit var gradoSpinner: Spinner
    private lateinit var progressBar:ProgressBar
    private lateinit var materiaSpinner: Spinner
    private lateinit var guardarButton: Button

    private val firebaseRepository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_notas)


        nombreEditText = findViewById(R.id.edtNombre)
        apellidoEditText = findViewById(R.id.edtApellido)
        notaEditText = findViewById(R.id.edtNota)
        gradoSpinner = findViewById(R.id.spGrado)
        materiaSpinner = findViewById(R.id.spMateria)
        guardarButton = findViewById(R.id.btnRegistrar)
        progressBar = findViewById(R.id.progressBar)

        // Configurar Spinners
        val grados = resources.getStringArray(R.array.opciones_grado)
        val materias = resources.getStringArray(R.array.opciones_materia)

        gradoSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grados)
        materiaSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, materias)

        guardarButton.setOnClickListener {
            guardarEstudiante()
        }

        /*
        val spinnerGrado:Spinner = findViewById(R.id.spGrado)
        val opcionesGrado= resources.getStringArray(R.array.opciones_grado)
        val adapterGrado= ArrayAdapter(this,android.R.layout.simple_spinner_item,opcionesGrado)
        adapterGrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrado.adapter = adapterGrado

        val spinnerMateria:Spinner = findViewById(R.id.spMateria)
        val opcionesMateria= resources.getStringArray(R.array.opciones_materia)
        val adapterMateria= ArrayAdapter(this,android.R.layout.simple_spinner_item,opcionesMateria)
        adapterMateria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMateria.adapter = adapterMateria

         */



        val btnRegresar = findViewById<ImageButton>(R.id.imgbRegresar)
        btnRegresar.setOnClickListener{
            finish()
        }

    }

    private fun guardarEstudiante(){

        val nombre = nombreEditText.text.toString().trim()
        val apellido = apellidoEditText.text.toString().trim()
        val nota = notaEditText.text.toString()
        val grado = gradoSpinner.selectedItem.toString()
        val materia = materiaSpinner.selectedItem.toString()

        Log.d("DEBUG", "Datos ingresados - Nombre: $nombre, Apellido: $apellido, Nota: $nota, Grado: $grado, Materia: $materia")

        if(!ValidationUtils.esNombreValido(nombre)){
            Log.e("ERROR", "Nombre no válido")
            nombreEditText.error ="Nombre no valido"
            return
        }
        if(!ValidationUtils.esApellidoValido(apellido)){
            Log.e("ERROR", "Apellido no válido")
            apellidoEditText.error="Apellido no valido"
            return
        }
        if(ValidationUtils.esNumeroCompleto(nota)){
            Log.e("ERROR", "Nota no válida")
            notaEditText.error="Ingrese un número valido entre el 0 y el 10"
            return
        }
        if(!ValidationUtils.esNumeroValido(nota)){
            Log.e("ERROR", "Nota no válida")
            notaEditText.error="Ingrese un número valido entre el 0 y el 10"
            return
        }

        val estudiante = Estudiante(
            nombre = nombre,
            apellido = apellido,
            nota = nota,
            grado = grado,
            materia = materia
        )
        guardarButton.isEnabled = false
        progressBar.visibility=View.VISIBLE
        Log.d("DEBUG", "Estudiante creado: $estudiante")

        firebaseRepository.guardarEstudiante(estudiante,
            onSuccess = {
                guardarButton.isEnabled = true
                progressBar.visibility=View.GONE
                Log.i("INFO", "Estudiante guardado exitosamente en Firebase")
                mostrarPopup("Estudiante agregado exitosamente")

            },
            onFailure = {
                mensaje ->
                progressBar.visibility = View.GONE
                guardarButton.isEnabled=true
                Log.e("ERROR", "Error guardando en Firebase: $mensaje")
                Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
            })

    }

    private fun mostrarPopup(mensaje:String){
        AlertDialog.Builder(this)
            .setTitle("Exito")
            .setMessage(mensaje)
            .setPositiveButton("Ok") { dialog,_ -> dialog.dismiss()}
            .show()
    }
}