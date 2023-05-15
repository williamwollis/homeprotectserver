package ru.alexch.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String? = null,
    val email: String,
    val password: String
)