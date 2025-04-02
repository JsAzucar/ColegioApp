package com.sugardaddy.colegioapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.sugardaddy.colegioapp.R

class Login : AppCompatActivity() {

    //Para el login
    //Para autorizar la firebase
    private lateinit  var auth: FirebaseAuth

    //Referencia a componentes
    private lateinit var btnLog_in: Button
    private lateinit var textViewRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        //Iniciar el auth
        auth = FirebaseAuth.getInstance()

        //Programar botón para ir a registrarse
        val btnRegistrarU = findViewById<Button>(R.id.btnIRegistrarse)
        btnRegistrarU.setOnClickListener{
            startActivity(Intent(this, RegistrarMaestros::class.java))
        }

        //Funcionamiento del botón para registrarse
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        btnIngresar.setOnClickListener{
            //Se crean las variables con las que trabajara el metodo login
            val mailUsuario = findViewById<TextView>(R.id.txtMaUsuario).text.toString()
            val contraUsuario = findViewById<TextView>(R.id.txtMaContra).text.toString()
            this.login(mailUsuario, contraUsuario)
        }

    }

    //Función para hacer login a la app
    private fun login(mailUsuario: String, contraUsuario: String){
        auth.signInWithEmailAndPassword(mailUsuario, contraUsuario).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                //Se irá al activity que debe abrirse cuando se haya registrado
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            //Se añade un mensaje de error en caso de presentarse
        }.addOnFailureListener{exception -> Toast.makeText(
            applicationContext, exception.localizedMessage,Toast.LENGTH_LONG
        ).show()}
    }
}