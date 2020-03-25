package no.skatteetaten.aurora

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [SharedProperties::class])
class SharedPropertiesTest {
    @Autowired
    private lateinit var sharedProperties: SharedProperties

    @Value("\${spring.application.name}")
    private lateinit var applicationName: String

    @Test
    fun `load properties`() {
        assertThat(sharedProperties).isNotNull()
        assertThat(applicationName).isEqualTo("my-app")
    }
}
