package ru.alexch.service.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
