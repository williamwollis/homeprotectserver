package ru.alexch.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.alexch.model.Device
import ru.alexch.model.User
import ru.alexch.service.UserService
import java.util.*
import kotlin.collections.ArrayList

fun Route.addDevice(userService: UserService) {
    authenticate {
        post("addDevice") {
            val request = call.receive<Device>()
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            val user = userService.findById(userId.toString())
            if(user==null) {
                call.respond("No such user")
                return@post
            }
            val devices = user.devices as ArrayList<Device>
            devices.add(request)
            userService.updateById(user.id.toString(), request = User(
                id = user.id,
                email = user.email,
                password = user.password,
                salt = user.salt,
                devices = devices
            )
            )
            call.respond(HttpStatusCode.OK)
        }
    }
}
fun Route.updateWarnings(userService: UserService) {
    authenticate {
        post("updateWarnings") {
            val request = call.receive<Device>()
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            val user = userService.findById(userId.toString())
            val warnings = ArrayList<String>()
            if(user==null) {
                call.respond(HttpStatusCode.OK,Device("No such user","No such user","No such user", warnings))
                return@post
            }
            val devices = user.devices as ArrayList<Device>
            devices.find { it.uuid == request.uuid }?.warnings = request.warnings

            userService.updateById(user.id.toString(), request = User(
                id = user.id,
                email = user.email,
                password = user.password,
                salt = user.salt,
                devices = devices
            )
            )
            call.respond(HttpStatusCode.OK, Device(uuid = "z",streamKey = "z", model = "z", warnings))
        }
    }
}
fun Route.updateStreamKey(userService: UserService) {
    authenticate {
        post("updateStreamKey") {
            val request = call.receive<Device>()
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            val user = userService.findById(userId.toString())
            val warnings = ArrayList<String>()
            warnings.add("test")
            if(user==null) {
                call.respond(HttpStatusCode.OK,Device("No such user","No such user","No such user", warnings))
                return@post
            }
            val devices = user.devices as ArrayList<Device>
            val streamKey = generateStreamKey()
            devices.find {it.uuid == request.uuid}?.streamKey = streamKey
            userService.updateById(user.id.toString(), request = User(
                id = user.id,
                email = user.email,
                password = user.password,
                salt = user.salt,
                devices = devices
            )
            )
            call.respond(HttpStatusCode.OK, Device(uuid = "z",streamKey = streamKey, model = "z", warnings))
        }
    }
}
fun Route.authStream(userService: UserService) {
    post("/auth") {
        val requestBody = call.receiveParameters()
        val streamKey = requestBody["key"].toString()
        val name = requestBody["name"].toString()
        System.out.println("$name | $streamKey")
        val user = userService.findByEmail(name)
        System.out.println(user.toString())
       // System.out.println("42525   ${user.email} || $name")
        if (user != null) {
            if(user.email==name) {
                for(device in user?.devices!!) {
                    System.out.println("${device.streamKey} | $streamKey"  )
                    if(device.streamKey==streamKey) {
                        call.respond(HttpStatusCode.OK)
                        return@post
                    }
                }
                System.out.println("unknown stream")
                call.respond(HttpStatusCode.Conflict,"Unknown stream key")
                return@post
            }
        }
        System.out.println("unknown user")
        call.respond(HttpStatusCode.Conflict,"Unknown user")
        return@post



    }
}
fun Route.getDevices(userService: UserService) {
    authenticate {
        get("devices") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            val user = userService.findById(userId.toString())
            if(user!=null) {
                call.respond(HttpStatusCode.OK,user.devices)
            }
        }
    }
}
fun generateStreamKey():String {
    val n = 32
    val characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    val random = Random(System.nanoTime())
    val password = StringBuilder()

    for (i in 0 until n)
    {
        val rIndex = random.nextInt(characterSet.length)
        password.append(characterSet[rIndex])
    }

    return password.toString()
}