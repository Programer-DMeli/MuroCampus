package com.carbajal.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.carbajal.murocampus.data.PostRepositoryRemote
import com.carbajal.murocampus.data.remote.RetrofitClient
import com.carbajal.murocampus.ui.PantallaCrear
import com.carbajal.murocampus.ui.PantallaDetalle
import com.carbajal.murocampus.ui.PantallaEditar
import com.carbajal.murocampus.ui.PantallaLista
import com.carbajal.murocampus.ui.theme.MurocampusTheme
import com.carbajal.murocampus.viewmodel.PostsViewModel
import com.carbajal.murocampus.viewmodel.PostsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MurocampusTheme {
                var pantallaCrearActiva by remember { mutableStateOf(false) }
                var pantallaEditarActiva by remember { mutableStateOf(false) }
                var idEditando by remember { mutableStateOf<Int?>(null) }
                var tituloEditando by remember { mutableStateOf("") }
                val idSeleccionado = viewModel.idSeleccionado

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    floatingActionButton = {
                        if (idSeleccionado == null && !pantallaCrearActiva && !pantallaEditarActiva) {
                            FloatingActionButton(
                                onClick = { pantallaCrearActiva = true },
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Crear publicación",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    when {
                        pantallaEditarActiva -> {
                            PantallaEditar(
                                postId = idEditando ?: 0,
                                tituloActual = tituloEditando,
                                viewModel = viewModel,
                                onBack = { pantallaEditarActiva = false },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        pantallaCrearActiva -> {
                            PantallaCrear(
                                viewModel = viewModel,
                                onBack = { pantallaCrearActiva = false },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        idSeleccionado != null -> {
                            PantallaDetalle(
                                viewModel = viewModel,
                                onEditClick = { id, title ->
                                    idEditando = id
                                    tituloEditando = title
                                    pantallaEditarActiva = true
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        else -> {
                            PantallaLista(
                                viewModel = viewModel,
                                onPostClick = { id -> viewModel.abrir(id) },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
