package com.sugardaddy.colegioapp.ui.main

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sugardaddy.colegioapp.R

class EditarEstudiante : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_estudiante)


        val btnRegresar = findViewById<ImageButton>(R.id.imgbRegresar)
        btnRegresar.setOnClickListener{
            finish()
        }

    }
}