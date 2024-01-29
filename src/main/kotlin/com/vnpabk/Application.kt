package com.vnpabk

import com.vnpabk.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt()?:8000, host = "0.0.0.0", module = Application::module,  watchPaths = listOf("classes"))
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureSockets()
    configureRouting()
}
