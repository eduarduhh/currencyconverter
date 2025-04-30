package br.com.eduarduhh.currencyconverter.client

import br.com.eduarduhh.currencyconverter.dto.ExchangeRatesResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "exchangeRatesClient",
    url = "https://api.exchangeratesapi.io"
)
interface ExchangeRatesClient {

    @GetMapping("/latest")
    fun getLatestRates(
        @RequestParam("base") base: String ,
        @RequestParam("access_key") accessKey: String
    ): ExchangeRatesResponse
}