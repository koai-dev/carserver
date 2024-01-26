package com.vnpabk.plugins

import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.ExpectationFailed
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.h2.engine.User
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    val carService = CarService(database)
    routing {
        // Create user
        post("/cars") {
            val user = call.receive<Car>()
            val id = carService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user
        get("/cars/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = carService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/cars/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<Car>()
            val status = carService.update(id, user)
            if (status){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(ExpectationFailed)
            }
        }

        post("/cars/update_current_status/{id}&{currentStatus}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val currentStatus = call.parameters["currentStatus"]?.toBoolean() ?: throw IllegalArgumentException("Invalid currentStatus")
            val status = carService.update(id, Car(currentStatus = currentStatus))
            if (status){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        // Delete user
        delete("/cars/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            carService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
