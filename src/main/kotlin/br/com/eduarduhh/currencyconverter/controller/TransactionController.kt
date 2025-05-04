package br.com.eduarduhh.currencyconverter.controller

import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transaction")
class TransactionController(
    private val transactionService: TransactionService
) {

   @GetMapping("{userId}")
   fun findAllByUserId(@PathVariable userId: Long): ResponseEntity<List<Transaction>> {
        val transactions = transactionService.findAllByUserId(userId)
        return ResponseEntity.ok(transactions)
    }
}