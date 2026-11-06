package com.example.elitegpus

import com.google.firebase.firestore.Exclude

data class GpuModel(
    @get:Exclude var id: String = "",
    var nombre: String = "",
    var precio: String = "",
    var imagenBase64: String = ""
) {
    // Constructor vacío obligatorio para que Firebase no truene al leer/escribir
    constructor() : this("", "", "", "")
}