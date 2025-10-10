package br.com.eduarduhh.currencyconverter.config

import HttpClientExchangeRatesClient
import br.com.eduarduhh.currencyconverter.client.ExchangeRatesClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExchangeRatesConfig {

    @Bean
    fun exchangeRatesClient(@Value("\${api.url}") url: String,
                                  @Value("\${api.endpoint}") endpoint: String): ExchangeRatesClient {
        return HttpClientExchangeRatesClient(url, endpoint)
    }
}