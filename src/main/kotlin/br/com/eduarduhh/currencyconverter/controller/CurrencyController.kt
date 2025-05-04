package br.com.eduarduhh.currencyconverter.controller

import br.com.eduarduhh.currencyconverter.dto.ConversionRequest
import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.service.CurrencyService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/currency")
class CurrencyController(
    private val currencyService: CurrencyService,
) {
    @PostMapping("/convert")
    fun convert(
        @RequestBody @Valid request: ConversionRequest,
    ): ResponseEntity<Transaction> {
        val result =
            currencyService.convertCurrency(
                request.userId,
                request.fromCurrency,
                request.toCurrency,
                request.amount,
            )
        return ResponseEntity.ok(result)
    }
}
