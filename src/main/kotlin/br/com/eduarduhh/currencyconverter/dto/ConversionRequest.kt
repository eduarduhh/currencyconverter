package br.com.eduarduhh.currencyconverter.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ConversionRequest(

    @field:NotNull
    val userId: Long?,

    @field:NotBlank
    val fromCurrency: String,

    @field:NotBlank
    val toCurrency: String,

    @field:NotNull
    @field:Min(0)
    val amount: BigDecimal?
)