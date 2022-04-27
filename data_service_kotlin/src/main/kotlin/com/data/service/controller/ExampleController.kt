package com.data.service

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController {
    @GetMapping("/example")
    public fun two(@RequestParam path:String): String {
        System.out.println(path)
        return "-> $path";
    }
}
