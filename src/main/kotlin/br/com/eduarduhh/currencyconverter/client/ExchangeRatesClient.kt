package br.com.eduarduhh.currencyconverter.client

import br.com.eduarduhh.currencyconverter.dto.ExchangeRatesResponse

interface ExchangeRatesClient {
    fun getLatestRates(
        base: String,
        accessKey: String
    ): ExchangeRatesResponse
}
