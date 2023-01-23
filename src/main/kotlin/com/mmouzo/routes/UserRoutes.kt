package com.mmouzo.routes

import com.mmouzo.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val users = mutableListOf(
    User(id = 1, "Martín", 24, "mmouzo@mmouzo.gal"),
    User(id = 2, "Pedro", 22, "pedro@mmouzo.gal")

)

fun Route.userRouting() {
    route("/user") {
        get {
            if (users.isNotEmpty()) {
                call.respond(users)
            } else {
                call.respondText("Non hai usuarios", status = HttpStatusCode.NotFound)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "ID non atopado",
                status = HttpStatusCode.BadRequest
            )
            val user = users.find { it.id == id.toInt() } ?: return@get call.respondText(
                "Usuario con $id non atopado",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }
        post {
            val user = call.receive<User>()
            users.add(user)
            call.respondText("Usuario creado", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "ID non atopado",
                status = HttpStatusCode.BadRequest
            )
            if (users.removeIf { it.id == id.toInt() }) {
                call.respondText(
                    "Usuario eliminado",
                    status = HttpStatusCode.Accepted
                )
            }
        }
    }
}