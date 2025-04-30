package br.com.eduarduhh.currencyconverter.dto

import java.time.LocalDateTime

class ApiError(
    val status: Int,
    val timestamp: LocalDateTime,
    val error: String,
    val message: String?,
    val path: String
)
