package dataservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DataServiceKotlinApplication

fun main(args: Array<String>) {
	runApplication<DataServiceKotlinApplication>(*args)
}
