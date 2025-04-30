package br.com.eduarduhh.currencyconverter.service

import br.com.eduarduhh.currencyconverter.exception.CurrencyConversionException
import br.com.eduarduhh.currencyconverter.exception.CurrencyEnum
import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService (
    private val transactionRepository: TransactionRepository
){
    fun findAllByUserId (id: Long): List<Transaction> {

        var findAllByUserId = transactionRepository.findAllByUserId(id)
        if(findAllByUserId.isEmpty()){
            throw CurrencyConversionException(CurrencyEnum.TRANSACTION_NOT_FOUND, "Transaction not found user id : $id")
        }

        return findAllByUserId
    }
}