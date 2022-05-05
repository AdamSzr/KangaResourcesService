package com.data.service.responses

import org.springframework.http.HttpHeaders
import java.io.File
import java.nio.file.Files

fun createContentTypeHeader(filePath: File): HttpHeaders {
    val responseHeaders = HttpHeaders()
    responseHeaders.set("Content-Type", Files.probeContentType(filePath.toPath()))
    return responseHeaders
}