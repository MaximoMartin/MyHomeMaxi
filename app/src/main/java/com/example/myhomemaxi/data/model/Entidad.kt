package com.example.myhomemaxi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val telefono: String
)
