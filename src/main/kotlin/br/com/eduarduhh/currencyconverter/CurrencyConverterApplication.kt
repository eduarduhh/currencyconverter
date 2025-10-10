package br.com.eduarduhh.currencyconverter

import br.com.eduarduhh.currencyconverter.config.ApiProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(ApiProperties::class)
@SpringBootApplication(scanBasePackages = ["br.com.eduarduhh.currencyconverter"])
class CurrencyConverterApplication

fun main(args: Array<String>) {
    runApplication<CurrencyConverterApplication>(*args)
}
