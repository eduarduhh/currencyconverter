package br.com.eduarduhh.currencyconverter.dto

import java.math.BigDecimal

data class ExchangeRatesResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, BigDecimal>,
)
