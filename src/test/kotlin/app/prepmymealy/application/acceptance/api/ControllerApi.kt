package app.prepmymealy.application.acceptance.api

import app.prepmymealy.application.configuration.AppConfig
import app.prepmymealy.application.controller.DiscountController
import app.prepmymealy.application.controller.MenuController
import app.prepmymealy.application.controller.SettingsController
import app.prepmymealy.application.controller.ShoppingListController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.Collections.singletonList

@Component
class ControllerApi {
    private var port: Int = 0

    fun setPort(port: Int) {
        this.port = port
    }

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Value("\${base.url}")
    private lateinit var baseUrl: String

    fun recreateMenuForId(id: String): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${MenuController.MENU_PATH}/$id"
        return putResponseEntity(url, "")
    }

    fun getDiscounts(): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${DiscountController.DISCOUNTS_PATH}"
        return getResponseEntity(url)
    }

    fun postDiscounts(discounts: List<Any>): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${DiscountController.DISCOUNTS_PATH}"
        return postResponseEntity(url, discounts)
    }

    fun getSettingsById(id: String): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${SettingsController.SETTINGS_PATH}/$id"
        return getResponseEntity(url)
    }

    fun putSettings(
        id: String,
        body: Any,
    ): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${SettingsController.SETTINGS_PATH}/$id"
        return putResponseEntity(url, body)
    }

    fun postSettings(body: Any): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${SettingsController.SETTINGS_PATH}"
        return postResponseEntity(url, body)
    }

    fun getListById(id: String): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${ShoppingListController.LIST_PATH}/$id"
        return getResponseEntity(url)
    }

    fun putList(
        id: String,
        body: Any,
    ): ResponseEntity<String> {
        val url = "$baseUrl${port}${AppConfig.API}${AppConfig.API_VERSION}${ShoppingListController.LIST_PATH}/$id"
        return putResponseEntity(url, body)
    }

    private fun getResponseEntity(url: String): ResponseEntity<String> {
        val headers = HttpHeaders()
        headers.accept = singletonList(MediaType.APPLICATION_JSON)
        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity<MediaType>(headers), String::class.java)
    }

    private fun putResponseEntity(
        url: String,
        requestBody: Any,
    ): ResponseEntity<String> {
        // Initialize headers
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON // Set Content-Type to application/json or any other mediaType
                accept = listOf(MediaType.APPLICATION_JSON)
            }

        // Convert the request body object to JSON
        val objectMapper = ObjectMapper()
        val jsonBody = objectMapper.writeValueAsString(requestBody)

        // Create HttpEntity with headers and the JSON body
        val httpEntity = HttpEntity(jsonBody, headers)

        // Execute the PUT request
        return restTemplate.exchange(
            url,
            HttpMethod.PUT,
            httpEntity,
            String::class.java,
        )
    }

    private fun postResponseEntity(
        url: String,
        requestBody: Any,
    ): ResponseEntity<String> {
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                accept = listOf(MediaType.APPLICATION_JSON)
            }

        val jsonBody = objectMapper.writeValueAsString(requestBody)

        val httpEntity = HttpEntity(jsonBody, headers)
        return restTemplate.exchange(
            url,
            HttpMethod.POST,
            httpEntity,
            String::class.java,
        )
    }
}
