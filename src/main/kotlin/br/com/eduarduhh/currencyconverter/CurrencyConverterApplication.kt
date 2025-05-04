package br.com.eduarduhh.currencyconverter

import br.com.eduarduhh.currencyconverter.config.ApiProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableConfigurationProperties(ApiProperties::class)
@EnableFeignClients(basePackages = ["br.com.eduarduhh.currencyconverter.client"])
@SpringBootApplication
class CurrencyConverterApplication

fun main(args: Array<String>) {
    runApplication<CurrencyConverterApplication>(*args)
}
