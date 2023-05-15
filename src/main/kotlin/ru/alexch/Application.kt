package ru.alexch

import io.ktor.server.application.*
import ru.alexch.plugins.*
import ru.alexch.service.UserService
import ru.alexch.service.hashing.SHA256HashingService
import ru.alexch.service.token.JwtTokenService
import ru.alexch.service.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        exiresIn = 365L * 1000L * 60L * 24L,
        //secret = System.getenv("JWT_SECRET")
        secret = environment.config.property("jwt.secret").getString()
    )
    val hashingService = SHA256HashingService()
    val tokenService = JwtTokenService()
    val userService = UserService()
    configureMonitoring()
    configureSerialization()
    configureSecurity(tokenConfig)
    configureRouting(
        hashingService = hashingService,
        tokenService = tokenService,
        userService = userService,
        tokenConfig = tokenConfig)
}
