package app.prepmymealy.application.acceptance.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
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
        private val REST_TEMPLATE: RestTemplate = RestTemplate()
    }

    @Value("\${base.url}")
    private lateinit var baseUrl: String

    fun getSettingsById(id: String): ResponseEntity<String> {
        val url = "$baseUrl/settings/$id"
        return getResponseEntity(url, MediaType.APPLICATION_JSON)
    }

    fun putSettings(
        id: String,
        body: Any,
    ): ResponseEntity<String> {
        val url = "$baseUrl/settings/$id"
        return putResponseEntity(url, MediaType.APPLICATION_JSON, body)
    }

    fun postSettings(body: Any): ResponseEntity<String> {
        val url = "$baseUrl/settings"
        return postResponseEntity(url, MediaType.APPLICATION_JSON, body)
    }

    private fun getResponseEntity(
        url: String,
        mediaType: MediaType,
    ): ResponseEntity<String> {
        REST_TEMPLATE.errorHandler = CustomResponseErrorHandler()
        val headers = HttpHeaders()
        headers.accept = singletonList(mediaType)
        return REST_TEMPLATE.exchange(url, HttpMethod.GET, HttpEntity<MediaType>(headers), String::class.java)
    }

    private fun putResponseEntity(
        url: String,
        mediaType: MediaType,
        requestBody: Any,
    ): ResponseEntity<String> {
        REST_TEMPLATE.errorHandler = CustomResponseErrorHandler()

        // Initialize headers
        val headers =
            HttpHeaders().apply {
                contentType = mediaType // Set Content-Type to application/json or any other mediaType
                accept = listOf(mediaType)
            }

        // Convert the request body object to JSON
        val objectMapper = ObjectMapper()
        val jsonBody = objectMapper.writeValueAsString(requestBody)

        // Create HttpEntity with headers and the JSON body
        val httpEntity = HttpEntity(jsonBody, headers)

        // Execute the PUT request
        return REST_TEMPLATE.exchange(
            url,
            HttpMethod.PUT,
            httpEntity,
            String::class.java,
        )
    }

    private fun postResponseEntity(
        url: String,
        mediaType: MediaType,
        requestBody: Any,
    ): ResponseEntity<String> {
        REST_TEMPLATE.errorHandler = CustomResponseErrorHandler()

        val headers =
            HttpHeaders().apply {
                contentType = mediaType
                accept = listOf(mediaType)
            }

        val objectMapper = ObjectMapper()
        val jsonBody = objectMapper.writeValueAsString(requestBody)

        val httpEntity = HttpEntity(jsonBody, headers)

        return REST_TEMPLATE.exchange(
            url,
            HttpMethod.POST,
            httpEntity,
            String::class.java,
        )
    }
}
