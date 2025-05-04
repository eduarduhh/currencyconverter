package br.com.eduarduhh.currencyconverter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "api")
data class ApiProperties
    @ConstructorBinding
    constructor(
        val base: String,
        val key: String,
    )
