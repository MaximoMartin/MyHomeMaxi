package com.example.myhomemaxi.data.db

import androidx.room.*
import com.example.myhomemaxi.data.model.Entidad
import kotlinx.coroutines.flow.Flow

@Dao
interface EntidadDao {
    @Query("SELECT * FROM entidad")
    fun getAll(): Flow<List<Entidad>>

    @Insert
    suspend fun insert(entidad: Entidad)

    @Update
    suspend fun update(entidad: Entidad)

    @Delete
    suspend fun delete(entidad: Entidad)
}
