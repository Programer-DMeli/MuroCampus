package com.carbajal.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carbajal.murocampus.viewmodel.PostsViewModel
import com.carbajal.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(
    viewModel: PostsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }

    val crearEstado by viewModel.crearEstado.collectAsStateWithLifecycle()

    // Limpiar formulario al tener éxito
    LaunchedEffect(crearEstado) {
        if (crearEstado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    BackHandler {
        viewModel.limpiarCrearEstado()
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Título
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // Campo Contenido
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Publicar (Deshabilitado si título o contenido están vacíos)
        val isFormValid = titulo.isNotBlank() && contenido.isNotBlank()
        val isCargando = crearEstado is UiState.Cargando

        Button(
            onClick = {
                viewModel.publicar(titulo, contenido)
            },
            enabled = isFormValid && !isCargando,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
        ) {
            if (isCargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Publicar",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultado / Éxito o Error
        when (val st = crearEstado) {
            is UiState.Exito -> {
                val postId = st.datos.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color(0xFF4CAF50), // Verde
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9) // Verde muy claro
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Publicado con id $postId",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        fontSize = 16.sp
                    )
                }
            }
            is UiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Error al publicar: ${st.mensaje}\nRevisa tu red e inténtalo de nuevo.",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
            else -> {}
        }
    }
}
