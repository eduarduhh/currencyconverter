package br.com.eduarduhh.demo.controller

import br.com.eduarduhh.demo.model.Transaction
import br.com.eduarduhh.demo.service.CurrencyService
import dto.ConversionRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class CurrencyController(
    private val currencyService: CurrencyService
) {

    @PostMapping("/convert")
    fun convert(@RequestBody @Valid request: ConversionRequest): ResponseEntity<Transaction> {
        val result = currencyService.convertCurrency(
            request.userId!!,
            request.fromCurrency,
            request.toCurrency,
            request.amount!!
        )
        return ResponseEntity.ok(result)
    }

   // @GetMapping("/transactions/{userId}")
   // fun getUserTransactions(@PathVariable userId: Long): ResponseEntity<List<Transaction>> {
   //     val transactions = currencyService.getUserTransactions(userId)
   //     return ResponseEntity.ok(transactions)
   // }
}