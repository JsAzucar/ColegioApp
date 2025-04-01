package com.sugardaddy.colegioapp.data.model

import java.io.Serializable

data class Estudiante(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val nota: String = "",
    val grado: String = "",
    val materia: String = ""
): Serializable{
    constructor() : this("", "", "", "", "", "") // Constructor vacío requerido por Firebase
}
