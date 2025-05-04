package br.com.eduarduhh.currencyconverter.controller

import br.com.eduarduhh.currencyconverter.dto.ConversionRequest
import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.service.CurrencyService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.time.LocalDateTime

@WebMvcTest(CurrencyController::class)
@Import(CurrencyControllerTest.MockConfig::class)
class CurrencyControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var currencyService: CurrencyService

    @TestConfiguration
    class MockConfig {
        @Bean
        fun currencyService(): CurrencyService = mockk()
    }

    @Test
    fun `should return 200 and transaction when converting currency`() {
        // given
        val request =
            ConversionRequest(
                userId = 1,
                fromCurrency = "BRL",
                toCurrency = "USD",
                amount = BigDecimal("100.00"),
            )

        val transaction =
            Transaction(
                id = 1,
                userId = 1,
                fromCurrency = "BRL",
                fromAmount = BigDecimal("100.00"),
                toCurrency = "USD",
                toAmount = BigDecimal("20.00"),
                conversionRate = BigDecimal("0.2"),
                timestamp = LocalDateTime.now(),
            )

        every { currencyService.convertCurrency(any(), any(), any(), any()) } returns transaction

        // when + then
        mockMvc.post("/api/v1/currency/convert") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.userId") { value(1) }
                    jsonPath("$.fromCurrency") { value("BRL") }
                    jsonPath("$.toCurrency") { value("USD") }
                    jsonPath("$.toAmount") { value(20.00) }
                }
            }
    }
}
