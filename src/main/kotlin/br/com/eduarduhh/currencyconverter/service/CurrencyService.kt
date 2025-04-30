package br.com.eduarduhh.currencyconverter.service


import br.com.eduarduhh.currencyconverter.client.ExchangeRatesClient
import br.com.eduarduhh.currencyconverter.config.ApiProperties
import br.com.eduarduhh.currencyconverter.exception.CurrencyConversionException
import br.com.eduarduhh.currencyconverter.exception.CurrencyEnum
import br.com.eduarduhh.currencyconverter.model.Transaction
import br.com.eduarduhh.currencyconverter.repository.TransactionRepository
import br.com.eduarduhh.currencyconverter.repository.UserRepository

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CurrencyService(
    private val transactionRepository: TransactionRepository,
    private val exchangeRatesClient: ExchangeRatesClient,
    private val userRepository: UserRepository,
    private val apiProperties: ApiProperties
) {

    fun convertCurrency(userId: Long, from: String, to: String, amount: BigDecimal): Transaction {

        val user = userRepository.findById(userId)
            .orElseThrow { CurrencyConversionException(CurrencyEnum.USER_NOT_FOUND,
                "User ID $userId not found") }

        val response = exchangeRatesClient.getLatestRates( apiProperties.base,apiProperties.key)

        val fromRate = response.rates[from.uppercase()]
            ?: throw CurrencyConversionException(CurrencyEnum.INVALID_CURRENCY,"currency: $from")
        val toRate = response.rates[to.uppercase()]
            ?: throw CurrencyConversionException(CurrencyEnum.INVALID_CURRENCY,"currency: $to")

        val rate = toRate.divide(fromRate, 6, RoundingMode.HALF_UP)
        val result = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP)

        val transaction = Transaction(
            userId = user.id,
            fromCurrency = from.uppercase(),
            fromAmount = amount,
            toCurrency = to.uppercase(),
            toAmount = result,
            conversionRate = rate
        )
        return transactionRepository.save(transaction)
    }
}

