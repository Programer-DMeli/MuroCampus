package com.carbajal.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.carbajal.murocampus.data.PostRepositoryRemote
import com.carbajal.murocampus.data.remote.RetrofitClient
import com.carbajal.murocampus.ui.PantallaDetalle
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
                    }
                ) { innerPadding ->
                    val idSeleccionado = viewModel.idSeleccionado

                    if (idSeleccionado == null) {
                        PantallaLista(
                            viewModel = viewModel,
                            onPostClick = { id -> viewModel.abrir(id) },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        PantallaDetalle(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
