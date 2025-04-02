package com.sugardaddy.colegioapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.sugardaddy.colegioapp.R

class RegistrarMaestros : AppCompatActivity() {

    //Objeto para interactuar con FireBase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_maestros)

        //Iniciar el auth
        auth = FirebaseAuth.getInstance()

        //Programar lo básico del screen
        val btnRegresarMa = findViewById<Button>(R.id.btnRegVolver)
        btnRegresarMa.setOnClickListener{
            //REEMPLAZAR EL MAIN ACTIVITY AQUI POR LA ACTIVITY DEL LOGIN
            startActivity(Intent(this, Login::class.java))
        }

        //Dentro del botón se crean y capturan los datos del formulario
        val btnRegistrarDatos = findViewById<Button>(R.id.btnRegRegistrar)
        btnRegistrarDatos.setOnClickListener{
            //Variables de datos de usuarios
            val txtUsuarioR = findViewById<EditText>(R.id.txtNuevoUsuario).text.toString()
            val txtContraR = findViewById<EditText>(R.id.txtNuevaContra).text.toString()
            //Enviar los datos a la función de registrar
            this.registrar(txtUsuarioR, txtContraR)
        }
    }

    private fun registrar(txtUsuarioR: String, txtContraR: String){
        auth.createUserWithEmailAndPassword(txtUsuarioR, txtContraR).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //REEMPLAZAR el ActivityRegistrado con el nombre de la activity a donde ira despues del login
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            //Mensaje de error en caso de no poderse registrar
        }.addOnFailureListener { exception -> Toast.makeText(
            applicationContext,
            exception.localizedMessage,
            Toast.LENGTH_LONG
        ).show()
        }
    }

    //Si se detecta que el usuario ya está registrado
    private fun yaRegistrado(){
        //CAMBIAR EL MainActivity aquí por la pantalla de login
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}