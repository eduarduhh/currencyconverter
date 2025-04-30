package br.com.eduarduhh.currencyconverter.repository

import br.com.eduarduhh.currencyconverter.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, Long> {

    fun findAllByUserId(userId: Long): List<Transaction>
}
