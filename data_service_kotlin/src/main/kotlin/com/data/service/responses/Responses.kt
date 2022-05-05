package com.data.service.responses

import com.data.service.FILE_DOWNLOAD_ENABLE
import com.data.service.model.ErrorStructure
import com.data.service.model.ResponseBodyStructure
import com.data.service.utils.getDiskObjects
import com.data.service.utils.getFilesFromDir
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun createResponseWithDirectoryStructure(path: Path): ResponseBodyStructure {
    val innerObjects = getFilesFromDir(path)
    val items = getDiskObjects(innerObjects as Array<File>)
    return ResponseBodyStructure(path.toString(), items)
}

fun createResponseWithByteArr(data :ByteArray,headers: HttpHeaders): ResponseEntity<ByteArray> {
    return  ResponseEntity
        .ok()
        .headers(headers)
        .body(data)
}

fun createResponseWithFile(path: String): ResponseEntity<ByteArray> {
    val file = File(path)
    val headers = createContentTypeHeader(file)

    if (FILE_DOWNLOAD_ENABLE)
        headers.set(
            "Content-Disposition", "attachment; filename=${file.name}"
        )

    return  createResponseWithByteArr(Files.readAllBytes((file.toPath())),headers)
}

fun createResponseWithError(status: HttpStatus, error: ErrorStructure): ResponseEntity<ErrorStructure> {
    return ResponseEntity.status(status).body(error)
}
