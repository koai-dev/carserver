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
    val driverClassName = "com.mysql.cj.jdbc.Driver"
    val jdbcURL =
        "jdbc:mysql://jwd1a0c1c28d1qsg:qrceod6may1jl7he@cwe1u6tjijexv3r6.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/xrsqp5d5dxrlhcwz"
    val database = Database.connect(jdbcURL, driverClassName, user = "jwd1a0c1c28d1qsg", password = "qrceod6may1jl7he")
    val carService = CarService(database)
    routing {
        route("/api") {
            // Create user
            post("/cars/add") {
                val user = call.receive<Car>()
                val id = carService.create(user)
                call.respond(HttpStatusCode.OK, id)
            }
            get("/cars/all") {
                val list = carService.all()
                call.respond(HttpStatusCode.OK, list)
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
                if (status) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(ExpectationFailed)
                }
            }

            post("/cars/update_current_status/{id}&{currentStatus}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val currentStatus = call.parameters["currentStatus"]?.toBoolean()
                    ?: throw IllegalArgumentException("Invalid currentStatus")
                val status = carService.update(id, Car(currentStatus = currentStatus))
                if (status) {
                    call.respond(HttpStatusCode.OK)
                } else {
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
}
