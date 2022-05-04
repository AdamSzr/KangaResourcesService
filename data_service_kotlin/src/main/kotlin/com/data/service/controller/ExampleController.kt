package com.data.service
import com.data.service.errors.REQUESTED_OBJ_IS_DIR
import com.data.service.errors.REQUESTED_PATH_NOT_EXIST
import com.data.service.errors.NO_CONTENT
import com.data.service.model.ResponseBodyStructure
import com.data.service.util.GetDiskObjects
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*


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

    fun createResponseWithFile(path: String): ResponseEntity<ByteArray> {
        val file = File(path)
        val responseHeaders = HttpHeaders()
        responseHeaders.set(
            "Content-Disposition","attachment; filename=${file.name}"
        )
        responseHeaders.set("Content-Type",MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE)

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(Files.readAllBytes(file.toPath()))
    }



    @GetMapping("/not")
    public fun two(@RequestParam path: String, @RequestParam(required = false) list: Boolean): Any {
        val fullPathString = PATH_PUBLIC_DIR.plus(path)
        val fileWithExt = fullPathString.substringAfterLast('/')
        val fileWithoutExt = fileWithExt.substringBeforeLast('.')

        val fullPath = Path(fullPathString)
        println("fullPath -> " + fullPath.toString())

        val parrentPath = fullPath.parent
        println("parentPath -> " + parrentPath.toString())

        println("parentExist ->" + parrentPath.exists())
        println("fullPathExist -> " + fullPath.exists())
        println("parentIsFile ->" + parrentPath.isRegularFile())
        println("fullPathIsFile-> " + fullPath.isRegularFile())

        if(!parrentPath.exists())
            return REQUESTED_PATH_NOT_EXIST

        if(fullPath.exists() && fullPath.isDirectory() && !list)
            return REQUESTED_OBJ_IS_DIR

        if(fullPath.exists() && fullPath.isRegularFile()) {
            println("SUCCESS - 1")
            return  createResponseWithFile(fullPathString)
        }

//        if(fullPath.exists() && fullPath.isDirectory()) {
//            println("MAYBE - 1")
//            return  "ANCS"
//        }

        println("onlyFileWithExt -> " + fileWithExt)
        println("onlyFileWhtouthExt -> " + fileWithoutExt)

        if(!fullPath.exists() && parrentPath.exists())
        {
            val requestedItem = tryFindFileInDir(parrentPath,fileWithoutExt)
            println("isInParentDir -> $requestedItem")
            if(requestedItem==null)
                return  NO_CONTENT

                println("SUCCESS - 2")
                return createResponseWithFile(requestedItem.path)

        }
       // fullPath = true && list = true

        val items = getFilesFromDir(parrentPath)
        val innerObjects = GetDiskObjects(items as Array<File>)
        return ResponseBodyStructure(parrentPath.toString(),innerObjects)
        }

}
