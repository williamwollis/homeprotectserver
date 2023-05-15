package ru.alexch.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
