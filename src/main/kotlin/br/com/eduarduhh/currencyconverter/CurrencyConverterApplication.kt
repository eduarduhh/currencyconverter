package br.com.eduarduhh.currencyconverter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients(basePackages = ["br.com.eduarduhh.currencyconverter.client"])
@SpringBootApplication
class TesteJayaApplication

fun main(args: Array<String>) {
	runApplication<TesteJayaApplication>(*args)
}
