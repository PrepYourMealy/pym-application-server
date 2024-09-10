package app.prepmymealy.application_server.acceptance.api


import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.util.Collections.singletonList

@Component
class ControllerApi {
    class CustomResponseErrorHandler : ResponseErrorHandler {
        override fun hasError(clientHttpResponse: ClientHttpResponse): Boolean {
            return false
        }

        override fun handleError(clientHttpResponse: ClientHttpResponse) {
        }
    }

    companion object {
        private val REST_TEMPLATE : RestTemplate = RestTemplate()
    }

    @Value("\${base.url}")
    private lateinit var baseUrl: String

    fun getSessionById(id: String) : ResponseEntity<String> {
        val url = "$baseUrl/settings/$id"
        return getResponseEntity(url, MediaType.APPLICATION_JSON, HttpMethod.GET)
    }

    private fun getResponseEntity(url: String, mediaType: MediaType, httpMethod: HttpMethod) : ResponseEntity<String> {
        REST_TEMPLATE.errorHandler = CustomResponseErrorHandler()
        val headers = HttpHeaders()
        headers.accept = singletonList(mediaType)
        return REST_TEMPLATE.exchange(url, HttpMethod.GET, HttpEntity<MediaType>(headers), String::class.java)
    }
}