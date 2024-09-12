package app.prepmymealy.application.service

import app.prepmymealy.application.domain.user.User
import app.prepmymealy.application.domain.user.UserLimits
import app.prepmymealy.application.domain.user.UserStats
import app.prepmymealy.application.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    private val userRepository: UserRepository = mock()

    private val userService = UserService(userRepository)

    @Test
    fun `should check if threshold is met`() {
        // given
        val userId = "someUserId"
        val threshold = 10
        val user =
            User.builder(userId)
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(threshold)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(5)
                        .build(),
                )
                .build()
        whenever(userRepository.findById(eq(userId))).thenReturn(Optional.of(user))

        // when
        val result = userService.isUserAllowedToRecreateMenu(userId)

        // then
        assertThat(result).isTrue()
        verify(userRepository).findById(eq(userId))
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun `should not allow to recreate menu when threshold is not met`() {
        // given
        val userId = "someUserId"
        val threshold = 10
        val user =
            User.builder(userId)
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(threshold)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(10)
                        .build(),
                )
                .build()
        whenever(userRepository.findById(eq(userId))).thenReturn(Optional.of(user))

        // when
        val result = userService.isUserAllowedToRecreateMenu(userId)

        // then
        assertThat(result).isFalse()
        verify(userRepository).findById(eq(userId))
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun `should reset threshold for every user in cron`() {
        // given
        val threshold = 10
        val user =
            User.builder("someUserId")
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(threshold)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(5)
                        .build(),
                )
                .build()
        whenever(userRepository.findAllUsersAsStream()).thenReturn(listOf(user).stream())

        // when
        userService.resetWeeklyRegenerateRequest()

        // then
        val expectedUser =
            User.builder("someUserId")
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(threshold)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(0)
                        .build(),
                )
                .build()
        verify(userRepository).findAllUsersAsStream()
        verify(userRepository).save(expectedUser)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun `should set threshold to 0 when stats object is null in cron`() {
        // given
        val threshold = 10
        val user =
            User.builder("someUserId")
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(threshold)
                        .build(),
                )
                .build()
        whenever(userRepository.findAllUsersAsStream()).thenReturn(listOf(user).stream())

        // when
        userService.resetWeeklyRegenerateRequest()

        // then
        val expectedUser =
            User.builder("someUserId")
                .limits(
                    UserLimits.builder()
                        .regenerateRequestsPerWeek(threshold)
                        .build(),
                )
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(0)
                        .build(),
                )
                .build()
        verify(userRepository).findAllUsersAsStream()
        verify(userRepository).save(expectedUser)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun `should increment count and save`() {
        // given
        val userId = "someUserId"
        val user =
            User.builder(userId)
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(5)
                        .build(),
                )
                .build()
        // when
        userService.incrementUserRegenerateRequestAndSave(user)

        // then
        val expectedUser =
            User.builder(userId)
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(6)
                        .build(),
                )
                .build()
        verify(userRepository).save(expectedUser)
        verifyNoMoreInteractions(userRepository)
    }

    @Test
    fun `should set to 1 if null on increment and save`() {
        // given
        val userId = "someUserId"
        val user =
            User.builder(userId)
                .build()
        // when
        userService.incrementUserRegenerateRequestAndSave(user)

        // then
        val expectedUser =
            User.builder(userId)
                .stats(
                    UserStats.builder()
                        .weeklyRegenerateRequest(1)
                        .build(),
                )
                .build()
        verify(userRepository).save(expectedUser)
        verifyNoMoreInteractions(userRepository)
    }
}
