package com.data.service.utils

import com.data.service.model.DiskObject
import com.data.service.model.DiskObjectType
import java.io.File
import java.nio.file.Path



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