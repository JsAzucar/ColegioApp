package com.sugardaddy.colegioapp.data.model

data class Estudiante(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val nota: String = "",
    val grado: String = "",
    val materia: String = ""
){
    constructor() : this("", "", "", "", "", "") // Constructor vacío requerido por Firebase
}
