package com.data.service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path

val PATH_PUBLIC_DIR = System.getenv("PWD") + "/src/main/resources/static/data/";


@RestController
class DataServiceController{
    @GetMapping("/api/data")
    public fun one(@RequestParam path:String): String {
        val  fullPath = PATH_PUBLIC_DIR.plus(path)
        val f = Path(fullPath)

        System.out.println(fullPath)
        for (fileEntry in File(fullPath).listFiles()) {
            println(fileEntry.isFile)
        }

        val prop = Files.readAttributes(f, BasicFileAttributes::class.java)

        if(File(fullPath).exists())
            System.out.println(prop.isDirectory.toString() + prop.isRegularFile.toString())
        return "-> $path";
    }
}
