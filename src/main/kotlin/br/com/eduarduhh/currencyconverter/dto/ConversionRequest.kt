package br.com.eduarduhh.currencyconverter.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal

data class ConversionRequest(
    @field:NotNull(message = "ID do usuário é obrigatório")
    val userId: Long,
    @field:NotBlank(message = "Moeda de origem é obrigatória")
    @field:Size(min = 3, max = 3, message = "Moeda deve ter 3 caracteres")
    val fromCurrency: String,
    @field:NotBlank(message = "Moeda de destino é obrigatória")
    @field:Size(min = 3, max = 3, message = "Moeda deve ter 3 caracteres")
    val toCurrency: String,
    @field:NotNull(message = "Valor é obrigatório")
    @field:DecimalMin(value = "0.01", message = "Valor deve ser positivo")
    val amount: BigDecimal,
)
