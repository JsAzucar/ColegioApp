package com.sugardaddy.colegioapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sugardaddy.colegioapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            val pantalla = Intent(this, RegistrarNotas::class.java)
            startActivity(pantalla)
        }

        val btnMEstudiantes = findViewById<Button>(R.id.btnMEstudiantes)
        btnMEstudiantes.setOnClickListener {
            val pantalla = Intent(this, MostrarEstudiantes::class.java)
            startActivity(pantalla)
        }

        val btnMGlobal = findViewById<Button>(R.id.btnMGlobal)
        btnMGlobal.setOnClickListener {
            val pantalla = Intent(this, MostrarNotaGlobal::class.java)
            startActivity(pantalla)
        }

        val btnEditar = findViewById<Button>(R.id.btnEditar)
        btnEditar.setOnClickListener {
            val pantalla = Intent(this, EditarEliminarEstudiantes::class.java)
            startActivity(pantalla)
        }
    }
}