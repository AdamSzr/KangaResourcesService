package com.data.service

import com.data.service.errors.ERROR_REQUESTED_OBJ_IS_DIR
import com.data.service.errors.ERROR_REQUESTED_PATH_NOT_EXIST
import com.data.service.errors.NO_CONTENT
import com.data.service.model.ResponseBodyStructure
import com.data.service.util.GetDiskObjects
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.probeContentType
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile


@RestController
class DataServiceController {
    fun getFilesFromDir(dir: Path): Array<out File>? {
        println("getFilesFromDir -> " + dir)
        return File(dir.toString()).listFiles()
    }

    fun tryFindItemInDir(dirPath: Path, fileWithoutExt: String): File? {
        val innerItems = getFilesFromDir(dirPath)
        println("itemToFind ->" + fileWithoutExt)
        for (x in innerItems!!)
            println(x.name.substringBeforeLast('.'))


        val predicate: (File) -> Boolean = { it.name.substringBeforeLast('.') == fileWithoutExt }
        return innerItems.find(predicate)
    }


    fun createResponseWithDirectoryStructure(path: Path): ResponseBodyStructure {
        val innerObjects = getFilesFromDir(path)
        val items = GetDiskObjects(innerObjects as Array<File>)
        return ResponseBodyStructure(path.toString(), items)
    }

    fun createResponseWithFile(path: String): ResponseEntity<ByteArray> {
        val file = File(path)
        val responseHeaders = HttpHeaders()
        responseHeaders.set(
            "Content-Disposition", "attachment; filename=${file.name}"
        )

        val contentType = probeContentType(file.toPath())
        println("MIME_TYPE ->$contentType")
        responseHeaders.set("Content-Type", contentType)

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(Files.readAllBytes(file.toPath()))
    }

    @GetMapping("/api/data")
    fun two(@RequestParam path: String, @RequestParam(required = false) list: Boolean): Any {
        val fullPathString = PATH_PUBLIC_DIR.plus(path).substringBefore('?')
        val fileWithExt = fullPathString.substringAfterLast('/')
        val fileWithoutExt = fileWithExt.substringBeforeLast('.')
        val fullPath = Path(fullPathString)
        println("fullPath -> " + fullPath.toString())

        val parentPath = fullPath.parent
        println("parentPath -> $parentPath")

        println("parentExist ->" + parentPath.exists())
        println("fullPathExist -> " + fullPath.exists())
        println("parentIsDir ->" + parentPath.isDirectory())
        println("fullPathIsFile-> " + fullPath.isRegularFile())
        println("fullPathIsDir-> " + fullPath.isDirectory())

        if (!parentPath.exists())
            return ERROR_REQUESTED_PATH_NOT_EXIST

        if (fullPath.exists() && fullPath.isDirectory() && !list)
            return ERROR_REQUESTED_OBJ_IS_DIR

        if (fullPath.exists() && fullPath.isRegularFile())
            return createResponseWithFile(fullPathString)

        if (fullPath.exists() && fullPath.isDirectory() && list)
            return createResponseWithDirectoryStructure(fullPath)

        println("onlyFileWithExt -> " + fileWithExt)
        println("onlyFileWihtouthExt -> " + fileWithoutExt)

        println("findIn -> $parentPath, file -> $fileWithoutExt")
        val requestedItem = tryFindItemInDir(parentPath, fileWithoutExt)
        println("isItemInParentDir -> $requestedItem")

        if (requestedItem == null)
            return NO_CONTENT

        return createResponseWithFile(requestedItem.path)
    }

}
