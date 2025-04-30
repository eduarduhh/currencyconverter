package br.com.eduarduhh.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients(basePackages = ["br.com.eduarduhh.demo.client"])
@SpringBootApplication
class TesteJayaApplication

fun main(args: Array<String>) {
	runApplication<TesteJayaApplication>(*args)
}
