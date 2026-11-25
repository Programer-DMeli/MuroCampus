package com.carbajal.murocampus.data

import com.carbajal.murocampus.data.remote.PostApi
import com.carbajal.murocampus.model.Comentario
import com.carbajal.murocampus.model.NuevoPost
import com.carbajal.murocampus.model.Post
import kotlin.coroutines.cancellation.CancellationException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun eliminar(id: Int): Result<Unit>
    suspend fun actualizarTitulo(id: Int, nuevoTitulo: String): Result<Post>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> {
        return try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun listar(): Result<List<Post>> = llamar {
        api.listar()
    }

    override suspend fun detalle(id: Int): Result<Post> = llamar {
        api.detalle(id)
    }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar {
        api.comentarios(id)
    }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> = llamar {
        api.porUsuario(userId)
    }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar {
        api.crear(nuevo)
    }

    override suspend fun eliminar(id: Int): Result<Unit> = llamar {
        val response = api.eliminar(id)
        if (!response.isSuccessful) {
            throw Exception("Error HTTP: ${response.code()}")
        }
    }

    override suspend fun actualizarTitulo(id: Int, nuevoTitulo: String): Result<Post> = llamar {
        api.actualizarTitulo(id, mapOf("title" to nuevoTitulo))
    }
}
