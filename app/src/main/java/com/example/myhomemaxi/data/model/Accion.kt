package com.example.myhomemaxi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Accion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entidadId: Int,
    val nombre: String,
    val mensaje: String
)
