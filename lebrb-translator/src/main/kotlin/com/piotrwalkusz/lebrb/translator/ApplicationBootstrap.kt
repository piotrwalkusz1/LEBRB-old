package com.piotrwalkusz.lebrb.translator

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ApplicationBootstrap

fun main(args: Array<String>) {
    SpringApplication.run(ApplicationBootstrap::class.java, *args)
}