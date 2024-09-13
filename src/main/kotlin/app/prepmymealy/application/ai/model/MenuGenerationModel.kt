package app.prepmymealy.application.ai.model

import app.prepmymealy.application.ai.response.MenuResponse
import app.prepmymealy.application.ai.service.OutputDeterminationService
import app.prepmymealy.application.domain.discount.Discount
import app.prepmymealy.application.domain.settings.Settings
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
class MenuGenerationModel(
    private val objectMapper: ObjectMapper,
    private val outputDeterminationService: OutputDeterminationService,
    private val discountService: DiscountService,
    private val openAiChatModel: OpenAiChatModel,
) {
    private var discountCache: List<Discount>? = null

    fun reloadDiscountCache() {
        discountCache = discountService.getAllDiscounts()
    }

    fun generateMenu(settings: Settings): MenuResponse {
        if (settings.includeDiscounts == true && discountCache == null) {
            discountCache = discountService.getAllDiscounts()
        }
        val prompt = generatePrompt(settings)
        val response: ChatResponse = openAiChatModel.call(prompt)
        val content = response.result.output.content
        return objectMapper.readValue(content, MenuResponse::class.java)
    }

    private fun generatePrompt(settings: Settings): Prompt {
        val prompt =
            Prompt(
                generatePromptText(settings),
                OpenAiChatOptions.builder()
                    .withModel(OpenAiApi.ChatModel.GPT_4_O_MINI)
                    .withResponseFormat(
                        ResponseFormat(
                            ResponseFormat.Type.JSON_SCHEMA,
                            outputDeterminationService.generateSchemaForSettings(settings).toString(),
                        ),
                    )
                    .build(),
            )
        return prompt
    }

    private fun generatePromptText(settings: Settings): String {
        var prompt =
            """
            Erstelle ein Menü für eine Woche. Die Zutaten sollten frisch und gesund sein. Die Gerichte sollten einfach zuzubereiten sein und nicht zu lange dauern. Wichtig ist, dass jedes Gericht ein 
            richtiges Gericht ist und die Anleitung zum Zubereiten ausführlich sind. Außerdem müssen alle Zutaten die für das Gericht gebraucht werden aufgelistet sein.
            """.trimIndent()
        if (settings.budget != null) {
            prompt += "Das Menü sollte auch zu dem spezifizierten Budget passen. Dein Budget beträgt ${settings.budget}€. Wenn du für Produkte keinen Preis hast, dann schätze einen der realistisch in einem deutschen Dsicounter dafür eingsetzt werden würde."
        }
        if (!settings.favoriteMeals.isNullOrEmpty()) {
            prompt += "Gerne werden folgende Gerichte gemocht: ${settings.favoriteMeals.joinToString(", ")}. Versuche ähnliche Gerichte einzubauen, die dem Nutzer auch schmecken könnten."
        }
        if (!settings.likedIngredients.isNullOrEmpty()) {
            prompt += "Folgende Zutaten werden gerne gegessen: ${settings.likedIngredients.joinToString(", ")}. Suche nach Gerichten in denen einzelne Zutaten vorkommen oder suche Gerichte die den Geschmack treffen könnten."
        }
        if (!settings.dislikedIngredients.isNullOrEmpty()) {
            prompt += "Folgende Zutaten werden nicht gerne gegessen: ${settings.dislikedIngredients.joinToString(", ")}. Versuche diese Zutaten zu vermeiden."
        }
        if (!settings.dietaryPreferences.isNullOrEmpty()) {
            prompt += "Folgende Ernährungspräferenzen sollten beachtet werden: ${
                settings.dietaryPreferences.joinToString(
                    ", ",
                )
            }. Suche nach Gerichten die diesen Präferenzen entsprechen."
        }
        if (!settings.allergies.isNullOrEmpty()) {
            prompt += "Folgende Allergien sollten beachtet werden: ${settings.allergies.joinToString(", ")}. Suche nach Gerichten die diese Allergien nicht enthalten."
        }
        if (!settings.kitchenEquipment.isNullOrEmpty()) {
            prompt += "Folgendes Küchenequipment ist vorhanden: ${settings.kitchenEquipment.joinToString(", ")}. Suche nach Gerichten die mit diesem Equipment zubereitet werden können."
        }
        prompt +=
            """
            Auf der Einkaufsliste soll jede Zutat vorkommen, die für die Zubereitung der Gerichte benötigt wird. Keine Sammelbegriffe,
            wie Sonsitges oder etwas in der Art. Alles muss drauf stehen, dass in den Rezepten verwendet wird und jede Zutat soll nur einmal drauf
            stehen, wenn sie öfter benutzt wird muss die Menge auf der List angepasst werden. Außerdem sollte bei der Portionsgröße pro Person pro Gericht darauf geachtet werden,
            dass es genug Nährstoffe enthält um die gesamten Personen satt zu machen. Achte darauf, dass auch Beilagen für die Gerichte dabei sind, welche sonst keine hätten. Sonst sind zu wenige Kohlenhydrate in den Gerichten.
            """.trimIndent()
        return prompt
    }
}
