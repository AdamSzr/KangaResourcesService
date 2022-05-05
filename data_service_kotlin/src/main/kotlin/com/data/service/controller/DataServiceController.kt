package com.data.service

import com.data.service.errors.REQUESTED_OBJ_IS_DIR
import com.data.service.errors.REQUESTED_PATH_NOT_EXIST
import com.data.service.errors.NO_CONTENT
import com.data.service.model.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.probeContentType
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

@RestController
class DataServiceController {
    @GetMapping("/api/data")
    fun dataProviderHandler(@RequestParam path: String,
                            @RequestParam(required = false) list: Boolean,
                            @RequestParam(required = false) size: String?): Any {
        val fullPathString = PATH_PUBLIC_DIR.plus(path).substringBefore('?') // remove query from URL
        val itemWithoutExt = getItemWithoutExt(fullPathString)
        val fullPath = Path(fullPathString)
        val parentPath = fullPath.parent
        println(fullPath)

        println("Parent ->"+parentPath.exists())
        if (!parentPath.exists())
            return createResponseWithError(HttpStatus.BAD_REQUEST, REQUESTED_PATH_NOT_EXIST)

        if (fullPath.exists() && fullPath.isDirectory() && !list)
            return createResponseWithError(HttpStatus.NOT_FOUND, REQUESTED_OBJ_IS_DIR)

        if (fullPath.exists() && fullPath.isRegularFile())
            return createResponseWithFile(fullPathString)

        if (fullPath.exists() && fullPath.isDirectory() && list)
            return createResponseWithDirectoryStructure(fullPath)



        // parentPath exist && fullPath !exist -> possibly a request for a file, the file name is without ext
        val requestedItem = tryFindItemInDir(parentPath, itemWithoutExt)

        if (requestedItem == null)
            return createResponseWithError(HttpStatus.NO_CONTENT, NO_CONTENT)

        val contentType = probeContentType(requestedItem.toPath())
        val newImgSize = parseImageSize(size)

        if(newImgSize!=null && contentType.startsWith("image"))
        {
            val resizedImg = resizeImage(requestedItem, newImgSize)
            val responseHeaders = HttpHeaders()
            responseHeaders.set("Content-Type", contentType)
            return createResponseWithByteArr(resizedImg.toList().toByteArray(),responseHeaders)
        }

        return createResponseWithFile(requestedItem.path)
    }

    fun resizeImage(imageFile:File,size: ImageSize):ByteArray
    {
        var oryginalImg = ImageIO.read(imageFile)
        val resizedImg = oryginalImg.getScaledInstance(size.width,size.height, Image.SCALE_DEFAULT)
        val baos =  ByteArrayOutputStream()
        val buf = BufferedImage(size.width,size.height,BufferedImage.SCALE_DEFAULT)
        buf.graphics.drawImage(resizedImg,0,0,null)
        ImageIO.write(buf,"jpg", baos)
        return baos.toByteArray()
    }
    fun getItemWithoutExt(fullPath: String): String {
        val itemWithExt = fullPath.substringAfterLast('/')
        val itemWithoutExt = itemWithExt.substringBeforeLast('.')
        return itemWithoutExt
    }

    fun getFilesFromDir(dir: Path): Array<out File>? {
        return File(dir.toString()).listFiles()
    }

    fun tryFindItemInDir(dirPath: Path, fileWithoutExt: String): File? {
        val innerItems = getFilesFromDir(dirPath)

        val predicate: (File) -> Boolean = { it.name.substringBeforeLast('.') == fileWithoutExt }
        return innerItems!!.find(predicate)
    }

    fun getDiskObjects(filePaths: Array<File>): MutableList<DiskObject> {
        val objects = mutableListOf<DiskObject>()
        for (f in filePaths) {
            val type = if (f.isDirectory) DiskObjectType.DIR else DiskObjectType.FILE
            val diskObj = DiskObject(f.name, type)
            objects.add(diskObj)
        }
        return objects
    }

    fun createResponseWithDirectoryStructure(path: Path): ResponseBodyStructure {
        val innerObjects = getFilesFromDir(path)
        val items = getDiskObjects(innerObjects as Array<File>)
        return ResponseBodyStructure(path.toString(), items)
    }

    fun createResponseWithByteArr(data :ByteArray): ResponseEntity<ByteArray> {
        return ResponseEntity.ok()
            .body(data)
    }
    fun createResponseWithByteArr(data :ByteArray,headers: HttpHeaders): ResponseEntity<ByteArray> {
            return  ResponseEntity.ok().headers(headers).body(data)
    }

    fun createResponseWithFile(path: String): ResponseEntity<ByteArray> {
        val file = File(path)
        val responseHeaders = HttpHeaders()
        responseHeaders.set("Content-Type", probeContentType(file.toPath()))

        if (FILE_DOWNLOAD_ENABLE)
            responseHeaders.set(
                "Content-Disposition", "attachment; filename=${file.name}"
            )

        println("Send File -> createResponseWithFile() ")
        return  createResponseWithByteArr(Files.readAllBytes((file.toPath())),responseHeaders)
//        return ResponseEntity.ok()
//            .headers(responseHeaders)
//            .body(Files.readAllBytes(file.toPath()))
    }

    fun createResponseWithError(status: HttpStatus, error: ErrorStructure): ResponseEntity<ErrorStructure> {
        return ResponseEntity.status(status).body(error)
    }

}