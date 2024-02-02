package com.vnpabk

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.vnpabk.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.FileInputStream


fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt()?:8000, host = "0.0.0.0", module = Application::module,  watchPaths = listOf("classes"))
        .start(wait = true)
}

fun Application.module() {
    val refreshToken = FileInputStream("vnpa-bk-car-manager-firebase-adminsdk-qwmgn-28d95305bf.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(refreshToken))
        .build()
    FirebaseApp.initializeApp(options);
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureTemplating()
    configureSockets()
    configureRouting()
}
