package ru.alexch.extension

import ru.alexch.model.Device
import ru.alexch.model.User
import ru.alexch.model.UserDto

fun User.toDto(): UserDto =
    UserDto(
        id = this.id.toString(),
        email = this.email,
        password = this.password
    )
/*fun UserDto.toPerson(): User =
    User(
        email = this.email,
        password = this.password,
        salt = "1",
        d
    ) */