package app.prepmymealy.application_server.service

import app.prepmymealy.application_server.domain.Settings
import app.prepmymealy.application_server.domain.User
import app.prepmymealy.application_server.domain.UserLimits
import app.prepmymealy.application_server.domain.UserStats
import app.prepmymealy.application_server.repository.SettingsRepository
import app.prepmymealy.application_server.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.assertj.core.api.Assertions.assertThat
import org.mockito.kotlin.*
import java.util.*

@ExtendWith(MockitoExtension::class)
class SettingsUpdateServiceTest {

    private val settingsRepository: SettingsRepository = mock()
    private val userRepository: UserRepository = mock()
    private val service = SettingsUpdateService(settingsRepository, userRepository)


    @Test
    fun `should not update on no change`() {
        // given
        val id = "someId"
        val existingSettings = Settings(id = id, budget = 40L)
        val update = Settings(id = id, budget = 40L)
        whenever(settingsRepository.findById(eq(id))).thenReturn(Optional.of(existingSettings))

        // when
        val result = service.updateSettings(update)

        // then
        assertThat(result).isTrue()
        verify(settingsRepository).findById(id)
        verifyNoMoreInteractions(settingsRepository, userRepository)
    }

    @Test
    fun `should fail update when non existing settings found`() {
        // given
        val id = "someId"
        val update = Settings(id = id, budget = 40L)
        whenever(settingsRepository.findById(eq(id))).thenReturn(Optional.empty())

        // when
        val result = service.updateSettings(update)

        // then
        assertThat(result).isFalse()
        verify(settingsRepository).findById(id)
        verifyNoMoreInteractions(settingsRepository, userRepository)
    }

    @Test
    fun `should update when change and existing settings`() {
        // given
        val id = "someId"
        val existingSettings = Settings(id = id, budget = 40L)
        val update = Settings(id = id, budget = 40L, people = 2L)
        whenever(settingsRepository.findById(eq(id))).thenReturn(Optional.of(existingSettings))

        // when
        val result = service.updateSettings(update)

        // then
        val expectedSave = Settings(id = id, budget = 40L, people = 2L)
        assertThat(result).isTrue()
        verify(settingsRepository).findById(id)
        verify(settingsRepository).save(expectedSave)
        verifyNoMoreInteractions(settingsRepository, userRepository)
    }

    @Test
    fun `should fail create when settings already exists`() {
        // given
        val id = "someId"
        val existingSettings = Settings(id = id, budget = 40L)
        val update = Settings(id = id, budget = 40L, people = 2L)
        whenever(settingsRepository.findById(eq(id))).thenReturn(Optional.of(existingSettings))

        // when
        val result = service.createSettings(update)

        // then
        assertThat(result).isFalse()
        verify(settingsRepository).findById(id)
        verifyNoMoreInteractions(settingsRepository, userRepository)
    }

    @Test
    fun `should create settings and user stats on initial create`() {
        // given
        val id = "someId"
        val update = Settings(id = id, budget = 40L, people = 2L)
        whenever(settingsRepository.findById(eq(id))).thenReturn(Optional.empty())
        whenever(userRepository.findById(eq(id))).thenReturn(Optional.empty())

        // when
        val result = service.createSettings(update)

        // then
        val expectedSettings = Settings(id = id, budget = 40L, people = 2L)
        val expectedUser = User(id = id, limits = UserLimits(regenerateRequestsPerWeek = 2), stats = UserStats(weeklyRegenerateRequest = 0))
        assertThat(result).isTrue()
        verify(settingsRepository).findById(eq(id))
        verify(settingsRepository).save(expectedSettings)
        verify(userRepository).findById(eq(id))
        verify(userRepository).save(expectedUser)
        verifyNoMoreInteractions(settingsRepository, userRepository)
    }

    @Test
    fun `should create settings but not user data if they already exist`() {
        // given
        val id = "someId"
        val update = Settings(id = id, budget = 40L, people = 2L)
        val user = User(id = id, limits = UserLimits(regenerateRequestsPerWeek = 2), stats = UserStats(weeklyRegenerateRequest = 0))

        whenever(settingsRepository.findById(eq(id))).thenReturn(Optional.empty())
        whenever(userRepository.findById(eq(id))).thenReturn(Optional.of(user))

        // when
        val result = service.createSettings(update)

        // then
        val expectedSettings = Settings(id = id, budget = 40L, people = 2L)
        assertThat(result).isTrue()
        verify(settingsRepository).findById(eq(id))
        verify(settingsRepository).save(expectedSettings)
        verify(userRepository).findById(eq(id))
        verifyNoMoreInteractions(settingsRepository, userRepository)
    }

}