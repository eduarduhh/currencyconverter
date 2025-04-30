package br.com.eduarduhh.demo.service


import br.com.eduarduhh.demo.client.ExchangeRatesClient
import br.com.eduarduhh.demo.model.Transaction
import br.com.eduarduhh.demo.repository.TransactionRepository
import br.com.eduarduhh.demo.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CurrencyService(
    private val transactionRepository: TransactionRepository,
    private val exchangeRatesClient: ExchangeRatesClient,
    private val userRepository: UserRepository
) {

    fun convertCurrency(userId: Long, from: String, to: String, amount: BigDecimal): Transaction {

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User ID $userId not found") }

        val response = exchangeRatesClient.getRates()
        val fromRate = response.rates[from.uppercase()]
            ?: throw IllegalArgumentException("Invalid currency: $from")
        val toRate = response.rates[to.uppercase()]
            ?: throw IllegalArgumentException("Invalid currency: $to")

        val rate = BigDecimal(toRate).divide(BigDecimal(fromRate), 6, RoundingMode.HALF_UP)
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

