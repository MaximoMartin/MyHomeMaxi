package com.example.myhomemaxi.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myhomemaxi.data.model.Entidad
import com.example.myhomemaxi.data.model.Accion
import com.example.myhomemaxi.data.model.HistorialAccion

@Database(entities = [Entidad::class, Accion::class, HistorialAccion::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entidadDao(): EntidadDao
    abstract fun accionDao(): AccionDao
    abstract fun historialDao(): HistorialDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "myhome_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

