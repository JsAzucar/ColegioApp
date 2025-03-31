package com.sugardaddy.colegioapp.utils

object ValidationUtils {

    fun esNombreValido(nombre:String):Boolean = nombre.isNotEmpty()
    fun esApellidoValido(apellido:String):Boolean = apellido.isNotEmpty()
    fun esNumeroValido(numero:String):Boolean{
        val valor = numero.toDoubleOrNull() ?: 0.0
        return valor in 0.0..10.0
    }
    fun esNumeroCompleto(numero:String):Boolean = numero.isNullOrBlank()
    /*
    fun esNumeroValido(numero:String):Boolean{
        if(numero.isNullOrEmpty()) return false
    }
     */
}