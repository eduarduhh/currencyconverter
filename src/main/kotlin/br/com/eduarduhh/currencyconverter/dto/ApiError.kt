package br.com.eduarduhh.currencyconverter.dto

import java.time.LocalDateTime

data class ApiError(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String?,
    val path: String,
    val errorType: String? = null,
    val details: Map<String, String> = emptyMap(),
)
