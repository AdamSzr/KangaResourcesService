package com.data.service

import com.data.service.model.ResponseBodyStructure
import com.data.service.util.GetDiskObjects
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile


@RestController
class ExampleController {
    fun getFilesFromDir(dir: Path): Array<out File>? {
        println("getFilesFromDir -> "+ dir)
        return File(dir.toString()).listFiles()
    }

    fun tryFindFileInDir(dirPath:Path, fileWithoutExt:String): File? {
        val innerItems = getFilesFromDir(dirPath)
        for (x in innerItems!!)
            println(x.name.substringBeforeLast('.'))

        val predicate: (File) -> Boolean = { it.name.substringBeforeLast('.') == fileWithoutExt }
        return innerItems.find(predicate)
    }

    public fun getRequestedResource(path:Path): FileSystemResource {
        return FileSystemResource(path.absolutePathString())
    }


    @GetMapping("/example")
    public fun two(@RequestParam path: String): Any {
        val fullPathString = PATH_PUBLIC_DIR.plus(path)
        val fileWithExt = fullPathString.substringAfterLast('/')
        val fileWithoutExt = fileWithExt.substringBeforeLast('.')

        val fullPath = Path(fullPathString)
        println("fullPath -> " + fullPath.toString())

        val parrentPath = fullPath.parent
        println("parentPath -> " + parrentPath.toString())

        println("parentExist ->" + parrentPath.exists())
        println("fullPathExist -> " + fullPath.exists())

        if(fullPath.exists() && fullPath.isRegularFile()) {
            println("SUCCESS - 1")
            val responseHeaders = HttpHeaders()
            responseHeaders.set(
                "Content-Disposition","attachment; filename=${fileWithExt}"
            )

            return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(Files.readAllBytes(fullPath))
        }

        println("onlyFileWithExt -> " + fileWithExt)
        println("onlyFileWhtouthExt -> " + fileWithoutExt)

        if(!fullPath.exists() && parrentPath.exists())
        {
            val requestedItem = tryFindFileInDir(parrentPath,fileWithoutExt)
            println("isInParentDir -> $requestedItem")
            if(requestedItem!=null) {
                println("SUCCESS - 2")
                val responseHeaders = HttpHeaders()
                responseHeaders.set(
                    "Content-Disposition","attachment; filename=${requestedItem.name}"
                )
                responseHeaders.set("Content-Type",MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE)

                return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(Files.readAllBytes(requestedItem.toPath()))
            }
        }

        val items = getFilesFromDir(parrentPath)
        val innerObjects = GetDiskObjects(items as Array<File>)
        val res = ResponseBodyStructure(fullPathString,innerObjects)
//        items?.forEach { f -> res.addObject(GetDiskObjects(f)) }

        return res
        }

}
