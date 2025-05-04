package br.com.eduarduhh.currencyconverter.controller

import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.service.TransactionService
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
import org.springframework.test.web.servlet.get
import java.math.BigDecimal
import java.time.LocalDateTime

@WebMvcTest(TransactionController::class)
@Import(TransactionControllerTest.MockConfig::class)
class TransactionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var transactionService: TransactionService

    @TestConfiguration
    class MockConfig {
        @Bean
        fun transactionService(): TransactionService = mockk()
    }

    @Test
    fun `should return 200 and list of transactions`() {
        // given
        val transactions =
            listOf(
                Transaction(
                    id = 1,
                    userId = 1,
                    fromCurrency = "BRL",
                    fromAmount = BigDecimal("100.00"),
                    toCurrency = "USD",
                    toAmount = BigDecimal("20.00"),
                    conversionRate = BigDecimal("0.2"),
                    timestamp = LocalDateTime.now(),
                ),
            )

        every { transactionService.findAllByUserId(1L) } returns transactions

        // when + then
        mockMvc.get("/api/transaction/1") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.size()") { value(1) }
            jsonPath("$[0].userId") { value(1) }
            jsonPath("$[0].fromCurrency") { value("BRL") }
        }
    }
}
