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
        return File(dir.toString()).listFiles()
    }

    fun tryFindItemInDir(dirPath: Path, fileWithoutExt: String): File? {
        val innerItems = getFilesFromDir(dirPath)

        val predicate: (File) -> Boolean = { it.name.substringBeforeLast('.') == fileWithoutExt }
        return innerItems!!.find(predicate)
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

        responseHeaders.set("Content-Type", probeContentType(file.toPath()))

        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(Files.readAllBytes(file.toPath()))
    }

    @GetMapping("/api/data")
    fun dataProvider(@RequestParam path: String, @RequestParam(required = false) list: Boolean): Any {
        val fullPathString = PATH_PUBLIC_DIR.plus(path).substringBefore('?')
        val itemWithExt = fullPathString.substringAfterLast('/')
        val itemWithoutExt = itemWithExt.substringBeforeLast('.')
        val fullPath = Path(fullPathString)
        val parentPath = fullPath.parent

        if (!parentPath.exists())
            return ERROR_REQUESTED_PATH_NOT_EXIST

        if (fullPath.exists() && fullPath.isDirectory() && !list)
            return ERROR_REQUESTED_OBJ_IS_DIR

        if (fullPath.exists() && fullPath.isRegularFile())
            return createResponseWithFile(fullPathString)

        if (fullPath.exists() && fullPath.isDirectory() && list)
            return createResponseWithDirectoryStructure(fullPath)

        // parentPath exist && fullPath !exist -> must be a request for a file without ext
        val requestedItem = tryFindItemInDir(parentPath, itemWithoutExt)

        if (requestedItem == null)
            return NO_CONTENT


        return createResponseWithFile(requestedItem.path)
    }

}
