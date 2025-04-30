package br.com.eduarduhh.demo.repository


import br.com.eduarduhh.demo.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findAllByUserId(userId: Long): List<Transaction>
}
