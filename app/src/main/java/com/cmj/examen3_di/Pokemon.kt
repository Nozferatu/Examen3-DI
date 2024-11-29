package com.cmj.examen3_di

import java.io.Serializable

class Pokemon(
    var nombre: String,
    var entrenador: String,
    var tipo: String,
    var estatura: Int,
    var comentarios: String,
    var fechaAtrapado: String
):Serializable {
}