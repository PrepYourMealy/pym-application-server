package app.prepmymealy.application_server

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<ApplicationServerApplication>().with(TestcontainersConfiguration::class).run(*args)
}
