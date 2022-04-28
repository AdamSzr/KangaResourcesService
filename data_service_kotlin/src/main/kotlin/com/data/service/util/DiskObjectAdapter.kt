package com.data.service.util
import com.data.service.model.DiskObject
import com.data.service.model.DiskObjectType
import com.data.service.model.ErrorStructure
import com.data.service.model.ResponseBodyStructure
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.name


fun GetDiskObjects(filePaths:Array<File>): MutableList<DiskObject> {
    val objects = mutableListOf<DiskObject>()
    for (f in filePaths)
    {
        val type = if(f.isDirectory) DiskObjectType.DIR else DiskObjectType.FILE
        val diskObj = DiskObject(f.name,type)
        objects.add(diskObj)
    }
    return objects
}
