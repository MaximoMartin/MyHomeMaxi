package com.example.myhomemaxi.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myhomemaxi.data.model.Accion
import com.example.myhomemaxi.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AccionesScreen(navController: NavController, viewModel: MainViewModel, entidadId: Int) {
    val context = LocalContext.current
    val acciones by viewModel.acciones.collectAsState()
    val entidad = viewModel.entidades.collectAsState().value.find { it.id == entidadId }
    var showForm by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf<Accion?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val rotationAngle by animateFloatAsState(if (showForm) 45f else 0f, label = "FAB Rotation")

    LaunchedEffect(entidadId) {
        viewModel.loadAcciones(entidadId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showForm = !showForm }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar acción",
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Acciones: ${entidad?.nombre ?: "Entidad"}") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            acciones.forEach { accion ->
                var editando by remember { mutableStateOf(false) }
                var nombre by remember { mutableStateOf(accion.nombre) }
                var mensaje by remember { mutableStateOf(accion.mensaje) }
                var expandedMenu by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Crossfade(targetState = editando) { isEditing ->
                            if (isEditing) {
                                Column {
                                    OutlinedTextField(
                                        value = nombre,
                                        onValueChange = { nombre = it },
                                        label = { Text("Nombre del botón") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = mensaje,
                                        onValueChange = { mensaje = it },
                                        label = { Text("Mensaje SMS") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        TextButton(onClick = {
                                            editando = false
                                            nombre = accion.nombre
                                            mensaje = accion.mensaje
                                        }) {
                                            Text("Cancelar")
                                        }
                                        TextButton(onClick = {
                                            viewModel.updateAccion(accion.copy(nombre = nombre, mensaje = mensaje))
                                            editando = false
                                        }) {
                                            Text("Guardar")
                                        }
                                    }
                                }
                            } else {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(accion.nombre, style = MaterialTheme.typography.titleMedium)
                                            Text(accion.mensaje, style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Box {
                                            IconButton(onClick = { expandedMenu = true }) {
                                                Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                                            }
                                            DropdownMenu(
                                                expanded = expandedMenu,
                                                onDismissRequest = { expandedMenu = false }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text("Editar") },
                                                    onClick = {
                                                        expandedMenu = false
                                                        editando = true
                                                    },
                                                    leadingIcon = {
                                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Eliminar") },
                                                    onClick = {
                                                        expandedMenu = false
                                                        confirmDelete = accion
                                                    },
                                                    leadingIcon = {
                                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Button(
                                        onClick = {
                                            entidad?.let {
                                                val smsUri = Uri.parse("smsto:${it.telefono}")
                                                val intent = Intent(Intent.ACTION_SENDTO, smsUri)
                                                intent.putExtra("sms_body", accion.mensaje)
                                                viewModel.registrarHistorial(it, accion.mensaje)
                                                context.startActivity(intent)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Enviar SMS")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showForm,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(24.dp))

                    var nombre by remember { mutableStateOf("") }
                    var mensaje by remember { mutableStateOf("") }

                    Text("Nueva acción", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del botón") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = { mensaje = it },
                        label = { Text("Mensaje SMS") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                nombre = ""
                                mensaje = ""
                                showForm = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (nombre.isNotBlank() && mensaje.isNotBlank()) {
                                    viewModel.addAccion(entidadId, nombre.trim(), mensaje.trim())
                                    nombre = ""
                                    mensaje = ""
                                    showForm = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Acción agregada correctamente")
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Agregar Acción")
                        }
                    }
                }
            }

            confirmDelete?.let { accion ->
                AlertDialog(
                    onDismissRequest = { confirmDelete = null },
                    title = { Text("¿Eliminar acción?") },
                    text = { Text("¿Seguro que quieres eliminar esta acción?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteAccion(accion)
                            confirmDelete = null
                        }) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { confirmDelete = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
