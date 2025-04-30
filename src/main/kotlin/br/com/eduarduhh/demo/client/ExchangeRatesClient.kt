package br.com.eduarduhh.demo.client

import dto.ExchangeRatesResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "exchangeRatesClient",
    url = "https://api.exchangeratesapi.io"
)
interface ExchangeRatesClient {

    @GetMapping("/latest")
    fun getRates(
        @RequestParam("base") base: String = "EUR",
        @RequestParam("access_key") accessKey: String = "9e301f518041deb8988ff18b1b3a3f61"
    ): ExchangeRatesResponse
}