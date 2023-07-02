package nonblocking

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Component
class Handler(private val usecase: Usecase) {

    suspend fun hello(req: ServerRequest) =
        usecase.hello().let { ServerResponse.ok().bodyValueAndAwait(it) }
}

@Configuration
class Router {
    @Bean
    fun routes(handler: Handler) = coRouter { GET("/test", handler::hello) }
}

@Component
class Usecase(private val port: Port) {
    suspend fun hello(): String {
        return port.findOne()
    }
}

interface Port {
    fun findOne(): String
}

@Component
class Gateway: Port {
    override fun findOne(): String {
        return "hello"
    }
}

