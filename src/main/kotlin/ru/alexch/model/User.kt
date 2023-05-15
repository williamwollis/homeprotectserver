package ru.alexch.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class User(
    @BsonId
    val id: Id<User>? = null,
    val email: String,
    val password: String,
    val salt: String,
    val devices: List<Device>
)
