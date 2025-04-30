package br.com.eduarduhh.currencyconverter.service

import org.mockito.kotlin.verify


import br.com.eduarduhh.currencyconverter.exception.CurrencyConversionException
import br.com.eduarduhh.currencyconverter.exception.CurrencyEnum
import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.repository.TransactionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class TransactionServiceTest {

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @InjectMocks
    private lateinit var transactionService: TransactionService

    private val userId = 1L
    private val sampleTransactions = listOf(
        Transaction(
            id = 1L,
            userId = userId,
            fromCurrency = "USD",
            fromAmount = BigDecimal("100.00"),
            toCurrency = "BRL",
            toAmount = BigDecimal("500.00"),
            conversionRate = BigDecimal("5.00"),
            timestamp = LocalDateTime.now()
        ),
        Transaction(
            id = 2L,
            userId = userId,
            fromCurrency = "EUR",
            fromAmount = BigDecimal("50.00"),
            toCurrency = "BRL",
            toAmount = BigDecimal("300.00"),
            conversionRate = BigDecimal("6.00"),
            timestamp = LocalDateTime.now()
        )
    )

    @Test
    fun `findAllByUserId should return transactions when they exist`() {
        // Arrange
        whenever(transactionRepository.findAllByUserId(userId)).thenReturn(sampleTransactions)

        // Act
        val result = transactionService.findAllByUserId(userId)

        // Assert
        assertEquals(2, result.size)
        assertEquals(userId, result[0].userId)
        assertEquals("USD", result[0].fromCurrency)
        assertEquals(BigDecimal("500.00"), result[0].toAmount)
    }

    @Test
    fun `findAllByUserId should throw exception when no transactions found`() {
        // Arrange
        whenever(transactionRepository.findAllByUserId(userId)).thenReturn(emptyList())

        // Act & Assert
        val exception = assertThrows<CurrencyConversionException> {
            transactionService.findAllByUserId(userId)
        }

        assertEquals(CurrencyEnum.TRANSACTION_NOT_FOUND, exception.currencyEnum)
        assertEquals("Transaction not found user id : $userId", exception.message)
    }


    @Test
    fun `findAllByUserId should verify repository call`() {
        // Arrange
        whenever(transactionRepository.findAllByUserId(userId)).thenReturn(sampleTransactions)

        // Act
        transactionService.findAllByUserId(userId)

        // Assert
        verify(transactionRepository).findAllByUserId(userId)
    }


}