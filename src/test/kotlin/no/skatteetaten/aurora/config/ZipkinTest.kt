package no.skatteetaten.aurora.config

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

class ZipkinTest {

    @Nested
    @SpringBootTest(classes = [BaseStarterApplicationConfig::class])
    inner class Default {
        @Value("\${spring.zipkin.enabled:}")
        private var zipkinEnabled: Boolean? = null

        @Test
        fun `Default configuration`() {
            assertThat(zipkinEnabled).isNotNull().isFalse()
        }
    }

    @Nested
    @ActiveProfiles("openshift")
    @SpringBootTest(classes = [BaseStarterApplicationConfig::class])
    inner class OpenshiftProfile {
        @Value("\${spring.zipkin.enabled:}")
        private var zipkinEnabled: Boolean? = null

        @Test
        fun `Openshift profile`() {
            assertThat(zipkinEnabled).isNotNull().isTrue()
        }
    }

    @Nested
    @ActiveProfiles("openshift")
    @SpringBootTest(classes = [BaseStarterApplicationConfig::class], properties = ["spring.zipkin.enabled=false"])
    inner class OpenshiftProfileZipkinDisabled {
        @Value("\${spring.zipkin.enabled:}")
        private var zipkinEnabled: Boolean? = null

        @Test
        fun `Openshift profile with zipkin disabled`() {
            assertThat(zipkinEnabled).isNotNull().isFalse()
        }
    }
}