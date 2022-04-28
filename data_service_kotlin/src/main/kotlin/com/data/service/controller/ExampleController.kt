package com.data.service

import com.data.service.model.DiskObject
import com.data.service.model.DiskObjectType
import com.data.service.model.ErrorStructure
import com.data.service.model.ResponseBodyStructure
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class ExampleController {
    @GetMapping("/example")
    public fun two(@RequestParam path: String): ResponseBodyStructure {
        val res = ResponseBodyStructure("/a/b/c")
        res.addObject(DiskObject("item.txt",DiskObjectType.FILE))
        res.addObject(DiskObject("intel.pdf",DiskObjectType.FILE))
        res.addObject(DiskObject("files",DiskObjectType.DIR))
        return res
    }
}
