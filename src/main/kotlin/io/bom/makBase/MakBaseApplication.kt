package io.bom.makBase

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MakBaseApplication

fun main(args: Array<String>) {
    runApplication<MakBaseApplication>(*args)
}
