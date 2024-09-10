package app.prepmymealy.application_server.controller

import app.prepmymealy.application_server.converter.SettingsToSettingsRepresentationConverter
import app.prepmymealy.application_server.domain.Settings
import app.prepmymealy.application_server.representation.SettingsRepresentation
import app.prepmymealy.application_server.service.SettingsService
import app.prepmymealy.application_server.service.SettingsUpdateService
import app.prepmymealy.application_server.service.SettingsValidationService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.eq
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyNoMoreInteractions

import org.springframework.http.HttpStatus
import java.util.*

@ExtendWith(MockitoExtension::class)
class SettingsControllerTest {

    private val converter: SettingsToSettingsRepresentationConverter = mock()
    private val service: SettingsService = mock()
    private val updateService: SettingsUpdateService = mock()
    private val validationService: SettingsValidationService = mock()
    private val settingsMock : Settings = mock()
    private val settingsRepresentationMock: SettingsRepresentation = mock()


    private var controller: SettingsController = SettingsController(service, converter, updateService, validationService)

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

}