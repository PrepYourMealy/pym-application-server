package app.prepmymealy.application_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApplicationServerApplication

fun main(args: Array<String>) {
    runApplication<ApplicationServerApplication>(*args)
}
