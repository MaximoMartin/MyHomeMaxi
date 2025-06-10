package com.example.myhomemaxi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistorialAccion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entidadId: Int,
    val nombreEntidad: String,
    val mensaje: String,
    val timestamp: Long
)
