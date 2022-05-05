package com.data.service

import com.data.service.errors.REQUESTED_OBJ_IS_DIR
import com.data.service.errors.REQUESTED_PATH_NOT_EXIST
import com.data.service.errors.NO_CONTENT
import com.data.service.model.*
import com.data.service.responses.*
import com.data.service.utils.getItemWithoutExt
import com.data.service.utils.resizeImage
import com.data.service.utils.tryFindItemInDir
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
            val responseHeaders = createContentTypeHeader(requestedItem)
            return createResponseWithByteArr(resizedImg.toList().toByteArray(),responseHeaders)
        }

        return createResponseWithFile(requestedItem.path)
    }





}