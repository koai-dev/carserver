package com.vnpabk.plugins

import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.*

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/carChannel") {
            println("WEBSOCKET connected!")
            for (text in incoming) {
                println("WEBSOCKET COMMING: $text")
                send(text)
            }
        }
    }
}
