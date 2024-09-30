package app.prepmymealy.application.ai.model

import app.prepmymealy.application.ai.response.ShoppingListResponse
import app.prepmymealy.application.ai.service.OutputDeterminationService
import app.prepmymealy.application.domain.menu.Menu
import app.prepmymealy.application.service.DiscountService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat
import org.springframework.stereotype.Component

@Component
class ShoppingListGenerationModel(
    private val objectMapper: ObjectMapper,
    private val outputDeterminationService: OutputDeterminationService,
    private val openAiChatModel: OpenAiChatModel,
    private val discountService: DiscountService
) {
    fun generateShoppingList(menu: Menu): ShoppingListResponse {
        val prompt = generatePrompt(menu)
        val response: ChatResponse = openAiChatModel.call(prompt)
        val content = response.result.output.content
        return objectMapper.readValue(content, ShoppingListResponse::class.java)
    }

    private fun generatePrompt(menu: Menu): Prompt {
        val prompt =
            Prompt(
                generatePromptText(menu),
                OpenAiChatOptions.builder()
                    .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                    .withResponseFormat(
                        ResponseFormat(
                            ResponseFormat.Type.JSON_SCHEMA,
                            outputDeterminationService.generateSchemaForShoppingList().toString(),
                        ),
                    )
                    .build(),
            )
        return prompt
    }

    private fun generatePromptText(menu: Menu): String {
        return """
            Erzeuge eine Einkaufsliste für dieses Wochenmenü. Sie muss super präzise sein, damit man alles bekommst, was man brauchst.
            Außerdem ist es wichtig, dass du die richten Angebote identifizierts und diese in deiner Einkaufsliste benutzt, damit die Preise
            so akkurat wie möglich sind. Hier ist das Wochenmenü: ${objectMapper.writeValueAsString(menu)}. Hier sind die Discounts und wo sie 
            zu bekommen sind: ${objectMapper.writeValueAsString(discountService.getAllDiscounts())}. Es muss wirklich jede Zutat aufgelistet sein,
            welche für die Zubereitung der Gerichte benötigt wird. Bei Zutaten, die du nicht in den Discounts findest, musst du einen realistischen
            Preis schätzen. Achte darauf, dass keine Anteile von Zutaten gekauft werden kann. Wenn du eine Zutat in den Discounts findest musst du immer den
            ganzen Preis einrechnen und nicht den Anteil der benötigt wird. Und bei Zutaten, welche du nicht in den Discounts findest, musst du den Preis
            sehr genau schätzen. Am besten orientierst du dich bei der Schätzung an den Preisen, die du in den Dsicounts findest.
        """.trimIndent()
    }
}