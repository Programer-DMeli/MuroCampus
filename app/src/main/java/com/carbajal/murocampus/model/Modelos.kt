package com.carbajal.murocampus.model

import kotlinx.serialization.Serializable

//marcamos las data class con anotaciones @Serializable para que retrofit y kotlin puedan transformar los datos que vienen de internet
//en formato Json


@Serializable
data class Post (
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)

