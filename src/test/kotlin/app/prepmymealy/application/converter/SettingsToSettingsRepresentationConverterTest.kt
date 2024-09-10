package app.prepmymealy.application.converter

import app.prepmymealy.application.domain.Settings
import app.prepmymealy.application.representation.SettingsRepresentation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SettingsToSettingsRepresentationConverterTest {
    private val converter = SettingsToSettingsRepresentationConverter()

    @Test
    fun `should convert null to null`() {
        // given

        // when
        val res = converter.convert(null)

        // then
        assertThat(res).isNull()
    }

    @Test
    fun `should convert settings to settings representation`() {
        // given
        val settings = Settings(id = "someId", budget = 20L, people = 2)

        // when
        val res = converter.convert(settings)

        // then
        val expectedRepresentation = SettingsRepresentation(id = "someId", budget = 20L, people = 2)
        assertThat(res).isNotNull
        assertThat(res).isEqualTo(expectedRepresentation)
    }
}
