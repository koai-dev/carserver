package com.vnpabk.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
        header(HttpHeaders.ContentType, "application/json; charset=UTF-8; multipart/form-data")
        header(HttpHeaders.Accept, "*/*")
        header(HttpHeaders.ContentDisposition,"form-data")
    }
}
