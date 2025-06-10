package com.example.myhomemaxi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhomemaxi.data.db.AppDatabase
import com.example.myhomemaxi.data.model.Entidad
import com.example.myhomemaxi.data.model.Accion
import com.example.myhomemaxi.data.model.HistorialAccion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)

    val entidades: StateFlow<List<Entidad>> = db.entidadDao().getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _acciones = MutableStateFlow<List<Accion>>(emptyList())
    val acciones: StateFlow<List<Accion>> = _acciones

    val historial: StateFlow<List<HistorialAccion>> = db.historialDao().getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun loadAcciones(entidadId: Int) {
        viewModelScope.launch {
            _acciones.value = db.accionDao().getByEntidad(entidadId)
        }
    }

    fun addEntidad(nombre: String, telefono: String) {
        viewModelScope.launch {
            db.entidadDao().insert(Entidad(nombre = nombre, telefono = telefono))
        }
    }

    fun updateEntidad(entidad: Entidad) {
        viewModelScope.launch {
            db.entidadDao().update(entidad)
        }
    }

    fun deleteEntidad(entidad: Entidad) {
        viewModelScope.launch {
            db.entidadDao().delete(entidad)
            db.accionDao().deleteByEntidadId(entidad.id)
        }
    }

    fun addAccion(entidadId: Int, nombre: String, mensaje: String) {
        viewModelScope.launch {
            db.accionDao().insert(Accion(entidadId = entidadId, nombre = nombre, mensaje = mensaje))
            loadAcciones(entidadId)
        }
    }

    fun updateAccion(accion: Accion) {
        viewModelScope.launch {
            db.accionDao().update(accion)
            loadAcciones(accion.entidadId)
        }
    }

    fun deleteAccion(accion: Accion) {
        viewModelScope.launch {
            db.accionDao().delete(accion)
            loadAcciones(accion.entidadId)
        }
    }

    fun registrarHistorial(entidad: Entidad, mensaje: String) {
        viewModelScope.launch {
            db.historialDao().insert(
                HistorialAccion(
                    entidadId = entidad.id,
                    nombreEntidad = entidad.nombre,
                    mensaje = mensaje,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
