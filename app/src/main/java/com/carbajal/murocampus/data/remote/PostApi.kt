package com.carbajal.murocampus.data.remote


import com.carbajal.murocampus.model.Comentario
import com.carbajal.murocampus.model.NuevoPost
import com.carbajal.murocampus.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

//Anotaciones de Retrofit
//Utilizamos retrofit definiendo las peticiones HTTP, lo cual lo hacemos en esta interface
//
interface PostApi {

    //Utilizamos las anotaciones de Retrofit
    @GET("posts")  //con esto retrofit genera automaticamente la implementacion basandose en sus anotaciones
    suspend fun listar(): List<Post>  //suspend permite que las peticiones se realicen en un hilo secundario
    //sin congelar la interfaz de usuario

    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post  //reemplza de forma dinamica

    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post> //agrega un parametro de filtrado

    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post  //envia un objeto completo

}