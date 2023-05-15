package ru.alexch.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.alexch.routes.*
import ru.alexch.service.token.JwtTokenService
import ru.alexch.service.token.TokenConfig
import ru.alexch.service.UserService
import ru.alexch.service.hashing.HashingService

fun Application.configureRouting(
    hashingService: HashingService,
    tokenService: JwtTokenService,
    userService: UserService,
    tokenConfig: TokenConfig
    ) {

    routing {
        signUp(hashingService,userService)
        signIn(userService,hashingService,tokenService,tokenConfig)
        getSecretInfo()
        addDevice(userService = userService)
        updateStreamKey(userService = userService)
        updateWarnings(userService = userService)
        authStream(userService = userService)
        test()
        verify()
        getDevices(userService = userService)
        /*get("/person/{id}") {
            val id = call.parameters["id"].toString()
            service.findById(id)
                ?.let { foundPerson -> call.respond(foundPerson.toDto()) }
                ?: call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)

        }
        put("/person/{id}") {
            val id = call.parameters["id"].toString()
            val personRequest = call.receive<PersonDto>()
            val person = personRequest.toPerson()
            val updatedSuccessfully = service.updateById(id, person)
            if (updatedSuccessfully) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
            }
        }
        delete("/person/{id}") {
            val id = call.parameters["id"].toString()
            val deletedSuccessfully = service.deleteById(id)
            if (deletedSuccessfully) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse.NOT_FOUND_RESPONSE)
            }
        }
        get("/person/search") {
            val email = call.request.queryParameters["email"].toString()
            val foundPeople = service.findByEmail(email).map(User::toDto)
            call.respond(foundPeople)
        }
        get("/person") {
            val peopleList =
                service.findAll()
                    .map(User::toDto)
            call.respond(peopleList)
        }
        post("/person") {
            val request = call.receive<PersonDto>()
            val person = request.toPerson()
            service.create(person)
                ?.let { userId ->
                    call.response.headers.append("My-User-Id-Header", userId.toString())
                    call.respond(HttpStatusCode.Created)
                } ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
        }

         */

    }
}
