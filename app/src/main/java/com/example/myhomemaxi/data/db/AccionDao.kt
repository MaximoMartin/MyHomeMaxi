package com.example.myhomemaxi.data.db

import androidx.room.*
import com.example.myhomemaxi.data.model.Accion

@Dao
interface AccionDao {
    @Query("SELECT * FROM accion WHERE entidadId = :entidadId")
    suspend fun getByEntidad(entidadId: Int): List<Accion>

    @Insert
    suspend fun insert(accion: Accion)

    @Update
    suspend fun update(accion: Accion)

    @Delete
    suspend fun delete(accion: Accion)

    @Query("DELETE FROM accion WHERE entidadId = :entidadId")
    suspend fun deleteByEntidadId(entidadId: Int)
}
