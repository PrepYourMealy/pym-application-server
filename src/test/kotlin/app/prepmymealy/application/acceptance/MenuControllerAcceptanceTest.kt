package app.prepmymealy.application.acceptance

import app.prepmymealy.application.domain.user.User
import app.prepmymealy.application.domain.user.UserLimits
import app.prepmymealy.application.domain.user.UserStats
import app.prepmymealy.application.repository.MenuRepository
import app.prepmymealy.application.repository.SettingsRepository
import app.prepmymealy.application.repository.ShoppingListRepository
import app.prepmymealy.application.repository.UserRepository
import app.prepmymealy.application.testsupport.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.model.Generation
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class MenuControllerAcceptanceTest : AbstractSpringTest() {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var settingsRepository: SettingsRepository

    @Autowired
    private lateinit var menuRepository: MenuRepository

    @Autowired
    private lateinit var shoppingListRepository: ShoppingListRepository

    @MockBean
    lateinit var openAiChatModel: OpenAiChatModel

    private val chatResponse: ChatResponse = mock(ChatResponse::class.java)

    private val generation: Generation = mock(Generation::class.java)

    private val message: AssistantMessage = mock(AssistantMessage::class.java)

    @BeforeMethod
    fun setUp() {
        reset(openAiChatModel, chatResponse, generation, message)
        userRepository.deleteAll()
        settingsRepository.deleteAll()
        menuRepository.deleteAll()
        shoppingListRepository.deleteAll()
    }

    @Test
    fun `should init user and create menu on post`() {
        // given
        val userId = "user_1231njaskdjnasd"
        val menu = loadResourceAsString("/menu/exampleMenu.json")
        given(openAiChatModel.call(any(Prompt::class.java))).willReturn(chatResponse)
        whenever(chatResponse.result).thenReturn(generation)
        whenever(generation.output).thenReturn(message)
        whenever(message.content).thenReturn(menu)

        // when
        val result = api.recreateMenuForId(userId)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)

        val user = userRepository.findById(userId).get()
        assertThat(user.limits).isNotNull
        assertThat(user.stats!!.weeklyRegenerateRequest).isEqualTo(1)

        val dbMenu = menuRepository.findById(userId).get()
        assertThat(dbMenu).isNotNull

        val shoppingList = shoppingListRepository.findById(userId).get()
        assertThat(shoppingList).isNotNull

        val settings = settingsRepository.findById(userId).get()
        assertThat(settings).isNotNull
    }

    @Test
    fun `should increment counter and recreate menu on post`() {
        // given
        val userId = "user_1231njaskdjnasd"
        userRepository.save(
            User.builder(userId)
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(2)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(1)
                        .build(),
                )
                .build(),
        )

        val menu = loadResourceAsString("/menu/exampleMenu.json")
        given(openAiChatModel.call(any(Prompt::class.java))).willReturn(chatResponse)
        whenever(chatResponse.result).thenReturn(generation)
        whenever(generation.output).thenReturn(message)
        whenever(message.content).thenReturn(menu)

        // when
        val result = api.recreateMenuForId(userId)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)

        val user = userRepository.findById(userId).get()
        assertThat(user.limits).isNotNull
        assertThat(user.stats!!.weeklyRegenerateRequest).isEqualTo(2)

        val dbMenu = menuRepository.findById(userId).get()
        assertThat(dbMenu).isNotNull

        val shoppingList = shoppingListRepository.findById(userId).get()
        assertThat(shoppingList).isNotNull

        val settings = settingsRepository.findById(userId).get()
        assertThat(settings).isNotNull
    }

    @Test(priority = 1)
    fun `should not recreate menu when limit is reached`() {
        // given
        val userId = "user_1231njaskdjnasd"
        userRepository.save(
            User.builder(userId)
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(2)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(2)
                        .build(),
                )
                .build(),
        )

        // when
        val result = api.recreateMenuForId(userId)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }
}
