package br.com.eduarduhh.currencyconverter.service

import br.com.eduarduhh.currencyconverter.client.ExchangeRatesClient
import br.com.eduarduhh.currencyconverter.dto.ApiProperties
import br.com.eduarduhh.currencyconverter.dto.ExchangeRatesResponse
import br.com.eduarduhh.currencyconverter.exception.CurrencyConversionException
import br.com.eduarduhh.currencyconverter.exception.CurrencyEnum
import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.model.User
import br.com.eduarduhh.currencyconverter.repository.TransactionRepository
import br.com.eduarduhh.currencyconverter.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@ExtendWith(MockitoExtension::class)
class CurrencyServiceTest {

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var exchangeRatesClient: ExchangeRatesClient

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var currencyService: CurrencyService

    // Dados de teste
    private val base = "USD"
    private val apiKey = "test-api-key"
    private val userId = 1L
    private val fromCurrency = "USD"
    private val toCurrency = "BRL"
    private val amount = BigDecimal("100.00")

    private val testProperties = ApiProperties(base = base, key = apiKey)
    @BeforeEach
    fun setUp() {
        currencyService = CurrencyService(
            transactionRepository = transactionRepository,
            exchangeRatesClient = exchangeRatesClient,
            userRepository = userRepository,
            apiProperties = testProperties
        )
    }
    //bem sucedida
    @Test
    fun `convertCurrency should return transaction when conversion is successful`() {
        // Given
        val user = User(id = userId, name = "Test User")

        val rates = mapOf("BRL" to BigDecimal("6.402883"), "USD" to BigDecimal("1.13908"))
        val testResponse = ExchangeRatesResponse(
            success = true,
            timestamp = 1745964554,
            base = "EUR",
            date = "2025-04-29",
            rates = rates
        )

        val expectedTransaction = Transaction(
            id= 1L,
            userId = userId,
            fromCurrency = fromCurrency,
            fromAmount = amount,
            toCurrency = toCurrency,
            toAmount = BigDecimal("500.00"),
            conversionRate = BigDecimal("5.0")
        )

        whenever(userRepository.findById(userId)).thenReturn(Optional.of(user))
        whenever(exchangeRatesClient.getLatestRates(any(),any())).thenReturn(testResponse)
        whenever(transactionRepository.save(any<Transaction>())).thenReturn(expectedTransaction)

        // When
        val result = currencyService.convertCurrency(userId, fromCurrency, toCurrency, amount)

        // Then
        assertNotNull(result)
        assertEquals(userId, result.userId)
        assertEquals(fromCurrency, result.fromCurrency)
        assertEquals(toCurrency, result.toCurrency)
        assertEquals(BigDecimal("500.00"), result.toAmount)
        assertEquals(BigDecimal("5.0"), result.conversionRate)

        // Verify interactions
        verify(userRepository).findById(userId)
        verify(exchangeRatesClient).getLatestRates(base, apiKey)

        val transactionCaptor = argumentCaptor<Transaction>()
        verify(transactionRepository).save(transactionCaptor.capture())

        val savedTransaction = transactionCaptor.firstValue
        assertEquals(userId, savedTransaction.userId)
        assertEquals(fromCurrency, savedTransaction.fromCurrency)
        assertEquals(toCurrency, savedTransaction.toCurrency)
    }
    //usuario nao encontrado
    @Test
    fun `convertCurrency should throw exception when user not found`() {
        // Given
        whenever(userRepository.findById(userId)).thenReturn(Optional.empty())

        // When / Then
        val exception = assertThrows<CurrencyConversionException> {
            currencyService.convertCurrency(userId, fromCurrency, toCurrency, amount)
        }

        assertEquals(CurrencyEnum.USER_NOT_FOUND, exception.currencyEnum)
        assertEquals("User ID $userId not found", exception.message)

        verify(userRepository).findById(userId)
        verifyNoInteractions(exchangeRatesClient)
        verifyNoInteractions(transactionRepository)
    }
    // moeda invalida
    @Test
    fun `convertCurrency should throw exception when from currency is invalid`() {
        // Given
        val user = User(id = userId, name = "Test User")
        val invalidCurrency = "INVALID"

        val rates = mapOf("BRL" to BigDecimal(6.402883), "USD" to BigDecimal(1.13908))
        val testResponse = ExchangeRatesResponse(
            success = true,
            timestamp = 1745964554,
            base = "EUR",
            date = "2025-04-29",
            rates = rates
        )

        whenever(userRepository.findById(userId)).thenReturn(Optional.of(user))
        whenever(exchangeRatesClient.getLatestRates(base, apiKey)).thenReturn(testResponse)

        // When / Then
        val exception = assertThrows<CurrencyConversionException> {
            currencyService.convertCurrency(userId, invalidCurrency, toCurrency, amount)
        }

        assertEquals(CurrencyEnum.INVALID_CURRENCY, exception.currencyEnum)
        assertEquals("currency: $invalidCurrency", exception.message)

        verify(userRepository).findById(userId)
        verify(exchangeRatesClient).getLatestRates(base, apiKey)
        verifyNoInteractions(transactionRepository)
    }
    // moenda from invalida
    @Test
    fun `convertCurrency should throw exception when to currency is invalid`() {
        // Given
        val user = User(id = userId, name = "Test User")
        val invalidCurrency = "INVALID"

        val rates = mapOf("BRL" to BigDecimal(6.402883), "USD" to BigDecimal(1.13908))
        val testResponse = ExchangeRatesResponse(
            success = true,
            timestamp = 1745964554,
            base = "EUR",
            date = "2025-04-29",
            rates = rates
        )

        whenever(userRepository.findById(userId)).thenReturn(Optional.of(user))
        whenever(exchangeRatesClient.getLatestRates(base, apiKey)).thenReturn(testResponse)

        // When / Then
        val exception = assertThrows<CurrencyConversionException> {
            currencyService.convertCurrency(userId, fromCurrency, invalidCurrency, amount)
        }

        assertEquals(CurrencyEnum.INVALID_CURRENCY, exception.currencyEnum)
        assertEquals("currency: $invalidCurrency", exception.message)

        verify(userRepository).findById(userId)
        verify(exchangeRatesClient).getLatestRates(base, apiKey)
        verifyNoInteractions(transactionRepository)
    }

    @Test
    fun `convertCurrency should correctly calculate conversion between non-base currencies`() {
        // Given
        val user = User(id = userId, name = "Test User")


        val rates = mapOf("BRL" to BigDecimal(6.402883), "USD" to BigDecimal(1.13908))
        val testResponse = ExchangeRatesResponse(
            success = true,
            timestamp = 1745964554,
            base = "EUR",
            date = "2025-04-29",
            rates = rates
        )

        whenever(userRepository.findById(userId)).thenReturn(Optional.of(user))
        whenever(exchangeRatesClient.getLatestRates(base, apiKey)).thenReturn(testResponse)
        whenever(transactionRepository.save(any<Transaction>())).thenAnswer { it.arguments[0] as Transaction }
        // When
        val result = currencyService.convertCurrency(userId, fromCurrency, toCurrency, amount)

        // Then
        val expectedRate = BigDecimal(5.621100)
        val expectedAmount = amount.multiply(expectedRate).setScale(2, RoundingMode.HALF_UP)

        assertEquals(expectedRate.setScale(6, RoundingMode.HALF_UP), result.conversionRate)
        assertEquals(expectedAmount, result.toAmount)
    }
}