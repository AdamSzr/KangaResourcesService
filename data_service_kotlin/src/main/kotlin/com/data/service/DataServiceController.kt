package com.data.service

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class DataServiceController {
    @GetMapping("/api/data/{id}")
    fun one(@PathVariable id: Long?): String {
        return "-> ${id}";
    }
}
