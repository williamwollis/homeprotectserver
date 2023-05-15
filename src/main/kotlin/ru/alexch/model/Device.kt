package ru.alexch.model

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val uuid: String,
    var streamKey: String,
    val model: String,
    var warnings: List<String>
)
