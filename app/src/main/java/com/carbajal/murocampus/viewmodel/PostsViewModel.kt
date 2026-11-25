package com.carbajal.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carbajal.murocampus.data.PostRepository
import com.carbajal.murocampus.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

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
}
