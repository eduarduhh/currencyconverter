package br.com.eduarduhh.currencyconverter.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long,
    val fromCurrency: String,
    val fromAmount: BigDecimal,
    val toCurrency: String,
    val toAmount: BigDecimal,
    val conversionRate: BigDecimal,
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
)