package app.prepmymealy.application.controller

import app.prepmymealy.application.converter.SettingsToSettingsRepresentationConverter
import app.prepmymealy.application.domain.settings.Settings
import app.prepmymealy.application.representation.SettingsRepresentation
import app.prepmymealy.application.service.SettingsService
import app.prepmymealy.application.service.SettingsUpdateService
import app.prepmymealy.application.service.SettingsValidationService
import org.assertj.core.api.Assertions.assertThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.util.*

class SettingsControllerTest {
    private val converter: SettingsToSettingsRepresentationConverter = mock()
    private val service: SettingsService = mock()
    private val updateService: SettingsUpdateService = mock()
    private val validationService: SettingsValidationService = mock()
    private val settingsMock: Settings = mock()
    private val settingsRepresentationMock: SettingsRepresentation = mock()

    private var controller: SettingsController = SettingsController(service, converter, updateService, validationService)

    @BeforeMethod
    fun setUp() {
        reset(service, converter, updateService, validationService, settingsMock, settingsRepresentationMock)
    }

    @Test
    fun `should convert and send settings representation`() {
        // given
        val id = "someSettingsId"
        whenever(service.getSettingsById(eq(id))).thenReturn(Optional.of(settingsMock))
        whenever(converter.convert(eq(settingsMock))).thenReturn(settingsRepresentationMock)

        // when
        val response = controller.getSettingsForId(id)

        // then
        assertThat(response.body).isEqualTo(settingsRepresentationMock)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        verify(service).getSettingsById(eq(id))
        verify(converter).convert(eq(settingsMock))
        verifyNoMoreInteractions(service, converter, updateService, validationService)
    }

    @Test
    fun `should not convert when settings not found`() {
        // given
        val id = "someSettingsId"
        whenever(service.getSettingsById(eq(id))).thenReturn(Optional.empty())

        // when
        val response = controller.getSettingsForId(id)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        verify(service).getSettingsById(eq(id))
        verifyNoInteractions(converter, updateService, validationService)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun `should not update on validation error`() {
        // given
        val errorMap = Optional.of(mapOf("error" to "error"))
        whenever(validationService.validateSettings(eq(settingsMock))).thenReturn(errorMap)

        // when
        val response = controller.createSettings(settingsMock)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        verify(validationService).validateSettings(eq(settingsMock))
        verifyNoInteractions(service, converter, updateService)
        verifyNoMoreInteractions(validationService)
    }

    @Test
    fun `should not create on validation error`() {
        // given
        val userId = "someUserId"
        val errorMap = Optional.of(mapOf("error" to "error"))
        whenever(validationService.validateSettings(eq(settingsMock))).thenReturn(errorMap)
        whenever(settingsMock.id).thenReturn(userId)
        // when
        val response = controller.updateSettings(userId, settingsMock)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        verify(settingsMock).id
        verify(validationService).validateSettings(eq(settingsMock))
        verifyNoInteractions(service, converter, updateService)
        verifyNoMoreInteractions(validationService)
    }

    @Test
    fun `should update settings`() {
        // given
        val userId = "someUserId"
        val errorMap = Optional.empty<Map<String, String>>()
        whenever(validationService.validateSettings(eq(settingsMock))).thenReturn(errorMap)
        whenever(updateService.updateSettings(eq(settingsMock))).thenReturn(true)
        whenever(settingsMock.id).thenReturn(userId)
        // when
        val response = controller.updateSettings(userId, settingsMock)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        verify(settingsMock).id
        verify(validationService).validateSettings(eq(settingsMock))
        verify(updateService).updateSettings(eq(settingsMock))
        verifyNoMoreInteractions(validationService, updateService)
    }

    @Test
    fun `should create settings`() {
        // given
        val errorMap = Optional.empty<Map<String, String>>()
        whenever(validationService.validateSettings(eq(settingsMock))).thenReturn(errorMap)
        whenever(updateService.createSettings(eq(settingsMock))).thenReturn(true)

        // when
        val response = controller.createSettings(settingsMock)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        verify(validationService).validateSettings(eq(settingsMock))
        verify(updateService).createSettings(eq(settingsMock))
        verifyNoMoreInteractions(validationService, updateService)
    }
}
