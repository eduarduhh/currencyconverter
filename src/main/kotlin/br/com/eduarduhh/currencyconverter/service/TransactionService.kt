package br.com.eduarduhh.currencyconverter.service

import br.com.eduarduhh.currencyconverter.exception.CurrencyConversionException
import br.com.eduarduhh.currencyconverter.exception.CurrencyEnum
import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.repository.TransactionRepository
import br.com.eduarduhh.currencyconverter.util.log
import org.springframework.stereotype.Service

@Service
class TransactionService (
    private val transactionRepository: TransactionRepository
){
    private val log = log()

    fun findAllByUserId (id: Long): List<Transaction> {

        log.info("Buscando transacoes: $id")

        var findAllByUserId = transactionRepository.findAllByUserId(id)
        if(findAllByUserId.isEmpty()){
            log.info("Id do usuário: $id não possue transação")
            throw CurrencyConversionException(CurrencyEnum.TRANSACTION_NOT_FOUND, "Transaction not found user id : $id")
        }

        return findAllByUserId
    }
}