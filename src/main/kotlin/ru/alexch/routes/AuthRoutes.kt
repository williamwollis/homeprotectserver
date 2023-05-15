package ru.alexch.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.LoggingEvent
import ru.alexch.model.Device
import ru.alexch.model.User
import ru.alexch.model.UserDto
import ru.alexch.model.response.AuthResponse
import ru.alexch.service.UserService
import ru.alexch.service.hashing.HashingService
import ru.alexch.service.hashing.SaltedHash
import ru.alexch.service.token.JwtTokenService
import ru.alexch.service.token.TokenClaim
import ru.alexch.service.token.TokenConfig

fun Route.signUp(hashingService: HashingService, userService: UserService) {
    post("signup") {
        val request = call.receive<UserDto>()
        val areFieldsBlank = request.email.isBlank() || request.password.isBlank()
        val isPasswordShort = request.password.length < 8
        if(areFieldsBlank) {
            call.respond(HttpStatusCode.Conflict, "Fields are blank")
            return@post
        }
        if(isPasswordShort) {
            call.respond(HttpStatusCode.Conflict, "Password must be at least 8 characters in length")
            return@post
        }
        val devices =  ArrayList<Device>()
        val warnings = ArrayList<String>()
        warnings.add("test")
        devices.add(Device("test","test", "test", warnings))
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            email = request.email,
            password = request.password,
            salt = saltedHash.salt,
            devices = devices
        )


        userService.create(user)
    }
}
fun Route.signIn(userService: UserService,
                 hashingService: HashingService,
                 tokenService: JwtTokenService,
                 tokenConfig: TokenConfig) {
    post("signin") {
        val request = call.receive<UserDto>()
        val user = userService.findByEmail(request.email)
        if(user == null) {
            call.respond(HttpStatusCode.OK,AuthResponse(token="Incorrect email or password."))
            return@post
        }
        System.out.println("${request.email}  ${request.password} | ${user.email} ${user.password}")
       /* if(user!=null) {
            call.respond("Incorrect email or password.")
            return@post
        } */

        //val isValidPassword = hashingService.verify(value = request.password, saltedHash = SaltedHash(hash = user.password, salt = user.salt))

        val isValidPassword = request.password == user.password
            if(!isValidPassword || request.email != user.email)  {
                call.respond(HttpStatusCode.OK,AuthResponse(token="Incorrect email or password."))
                return@post
            }


        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            ))

    }

}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your id is " + userId.toString())
        }
    }
}
fun Route.test() {
    get("test") {
        call.respond(HttpStatusCode.OK, UserDto(email = "bebe@mail.ru", password = "bebe"))
    }
}
fun Route.verify() {
    authenticate {
        get("verify") {
            call.respond(HttpStatusCode.OK,"Succeed")
        }
    }
}