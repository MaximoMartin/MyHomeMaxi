package com.example.myhomemaxi.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myhomemaxi.data.model.Entidad
import com.example.myhomemaxi.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val entidades by viewModel.entidades.collectAsState()
    var showForm by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf<Entidad?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val rotationAngle by animateFloatAsState(if (showForm) 45f else 0f, label = "FAB Rotation")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showForm = !showForm }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar entidad",
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Mis Entidades") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("historial")
                    }) {
                        Icon(Icons.Default.History, contentDescription = "Historial")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(entidades.size) { index ->
                    val entidad = entidades[index]
                    var editando by remember { mutableStateOf(false) }
                    var nombre by remember { mutableStateOf(entidad.nombre) }
                    var telefono by remember { mutableStateOf(entidad.telefono) }
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
                                            label = { Text("Nombre") },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        OutlinedTextField(
                                            value = telefono,
                                            onValueChange = { telefono = it },
                                            label = { Text("Teléfono") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 8.dp)
                                        )
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            TextButton(onClick = {
                                                editando = false
                                                nombre = entidad.nombre
                                                telefono = entidad.telefono
                                            }) {
                                                Text("Cancelar")
                                            }
                                            TextButton(onClick = {
                                                viewModel.updateEntidad(entidad.copy(nombre = nombre, telefono = telefono))
                                                editando = false
                                            }) {
                                                Text("Guardar")
                                            }
                                        }
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    navController.navigate("acciones/${entidad.id}")
                                                }
                                        ) {
                                            Text(entidad.nombre, style = MaterialTheme.typography.titleMedium)
                                            Text(entidad.telefono, style = MaterialTheme.typography.bodyMedium)
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
                                                        confirmDelete = entidad
                                                    },
                                                    leadingIcon = {
                                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                                    }
                                                )
                                            }
                                        }
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
                    Spacer(modifier = Modifier.height(16.dp))
                    var nombre by remember { mutableStateOf("") }
                    var telefono by remember { mutableStateOf("") }

                    Text("Nueva entidad", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                                telefono = ""
                                showForm = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (nombre.isNotBlank() && telefono.isNotBlank()) {
                                    viewModel.addEntidad(nombre.trim(), telefono.trim())
                                    nombre = ""
                                    telefono = ""
                                    showForm = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Entidad agregada correctamente")
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }

            confirmDelete?.let { entidad ->
                AlertDialog(
                    onDismissRequest = { confirmDelete = null },
                    title = { Text("¿Eliminar entidad?") },
                    text = { Text("Esta acción eliminará también todas sus acciones asociadas.") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteEntidad(entidad)
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
