package com.data.service

import com.data.service.errors.ERROR_MISSING_QUERY
import com.data.service.errors.ERROR_REQUESTED_OBJ_IS_DIR
import com.data.service.errors.ERROR_REQUESTED_PATH_NOT_EXIST
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
class DataServiceController{

    fun getFilesFromDir(dir: Path): Array<out File>? {
        println("getFilesFromDir -> "+ dir)
        return File(dir.toString()).listFiles()
    }

    fun tryFindFileInDir(dirPath: Path, fileWithoutExt:String): File? {
        val innerItems = getFilesFromDir(dirPath)
        println("fileToFind ->"+fileWithoutExt)
        for (x in innerItems!!)
            println(x.name.substringBeforeLast('.'))


        val predicate: (File) -> Boolean = { it.name.substringBeforeLast('.') == fileWithoutExt }
        return innerItems.find(predicate)
    }


    fun createResponseWithDirectoryStructure(path:Path): ResponseBodyStructure {
        val items = getFilesFromDir(path)
        val innerObjects = GetDiskObjects(items as Array<File>)
        return ResponseBodyStructure(path.toString(),innerObjects)
    }

    fun createResponseWithFile(path: String): ResponseEntity<ByteArray> {
        val file = File(path)
        val responseHeaders = HttpHeaders()
        responseHeaders.set(
            "Content-Disposition","attachment; filename=${file.name}"
        )

        val contentType = probeContentType(file.toPath())
        println("MIME_TYPE ->"+contentType)
        responseHeaders.set("Content-Type", contentType)

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(Files.readAllBytes(file.toPath()))
    }

    @GetMapping("/api/data")
    public fun two(@RequestParam path: String, @RequestParam(required = false) list: Boolean): Any {
        val fullPathString = PATH_PUBLIC_DIR.plus(path).substringBefore('?')
        val fileWithExt = fullPathString.substringAfterLast('/')
        val fileWithoutExt = fileWithExt.substringBeforeLast('.')

        val fullPath = Path(fullPathString)
        println("fullPath -> " + fullPath.toString())

        val parrentPath = fullPath.parent
        println("parentPath -> " + parrentPath.toString())

        println("parentExist ->" + parrentPath.exists())
        println("fullPathExist -> " + fullPath.exists())
        println("parentIsFile ->" + parrentPath.isRegularFile())
        println("parentIsDir ->" + parrentPath.isDirectory())
        println("fullPathIsFile-> " + fullPath.isRegularFile())
        println("fullPathIsDir-> " + fullPath.isDirectory())

        if(!parrentPath.exists())
            return ERROR_REQUESTED_PATH_NOT_EXIST

        if(fullPath.exists() && fullPath.isDirectory() && !list)
            return ERROR_REQUESTED_OBJ_IS_DIR

        if(fullPath.exists() && fullPath.isRegularFile()) {
            println("SUCCESS - 1")
            return  createResponseWithFile(fullPathString)
        }

        println("onlyFileWithExt -> " + fileWithExt)
        println("onlyFileWhtouthExt -> " + fileWithoutExt)

        if(!fullPath.exists() && parrentPath.exists())
        {
            println("findIn -> $parrentPath, file -> $fileWithoutExt")
            val requestedItem = tryFindFileInDir(parrentPath,fileWithoutExt)
            println("isInParentDir -> $requestedItem")
            println("list -> $list")
            if(requestedItem==null && list)
               return createResponseWithDirectoryStructure(parrentPath)

            if(requestedItem!=null)
                return createResponseWithFile(requestedItem.path)
        }

        if(fullPath.isDirectory() && list)
            return createResponseWithDirectoryStructure(fullPath)

        return ERROR_MISSING_QUERY
    }

}
