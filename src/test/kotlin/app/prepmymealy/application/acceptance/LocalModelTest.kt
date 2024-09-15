package app.prepmymealy.application.acceptance

import app.prepmymealy.application.ai.model.MenuGenerationModel
import app.prepmymealy.application.service.SettingsService
import app.prepmymealy.application.service.UserInitializationService
import app.prepmymealy.application.testsupport.AbstractSpringTest
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

class LocalModelTest: AbstractSpringTest() {
    @Autowired
    private lateinit var userInitializationService: UserInitializationService

    @Autowired
    private lateinit var menuGenerationModel: MenuGenerationModel

    @Autowired
    private lateinit var settingsService: SettingsService

    @Test(enabled = false)
    fun `should create default menu`() {
        // given
        val userId = "someUserId"
        userInitializationService.initializeUser(userId)
        val settings = settingsService.getSettingsById(userId)
        // when
        val result = menuGenerationModel.generateMenu(settings.get())
        // then
        println(result)
    }
}