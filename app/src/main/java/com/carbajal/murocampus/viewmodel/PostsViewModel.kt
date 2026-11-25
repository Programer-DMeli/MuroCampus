package com.carbajal.murocampus.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.murocampus.data.PostRepository
import com.carbajal.murocampus.model.Comentario
import com.carbajal.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    var idSeleccionado by mutableStateOf<Int?>(null)
        private set

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = repo.listar()
            resultado.onSuccess { posts ->
                _lista.value = UiState.Exito(posts)
            }.onFailure { error ->
                _lista.value = UiState.Error(error.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun abrir(id: Int) {
        idSeleccionado = id
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            try {
                coroutineScope {
                    val deferredPost = async { repo.detalle(id) }
                    val deferredComentarios = async { repo.comentarios(id) }

                    val resPost = deferredPost.await()
                    val resComentarios = deferredComentarios.await()

                    if (resPost.isSuccess && resComentarios.isSuccess) {
                        val post = resPost.getOrThrow()
                        val comentarios = resComentarios.getOrThrow()
                        _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
                    } else {
                        val error = resPost.exceptionOrNull() ?: resComentarios.exceptionOrNull()
                        _detalle.value = UiState.Error(error?.localizedMessage ?: "Error al cargar el detalle")
                    }
                }
            } catch (e: Exception) {
                _detalle.value = UiState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun cerrarDetalle() {
        idSeleccionado = null
    }
}
