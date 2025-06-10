package com.example.myhomemaxi.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myhomemaxi.data.model.HistorialAccion
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {
    @Insert
    suspend fun insert(historial: HistorialAccion)

    @Query("SELECT * FROM HistorialAccion ORDER BY timestamp DESC")
    fun getAll(): Flow<List<HistorialAccion>>
}
