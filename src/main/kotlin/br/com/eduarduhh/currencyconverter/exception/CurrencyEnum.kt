package br.com.eduarduhh.currencyconverter.exception

import org.springframework.http.HttpStatus

enum class CurrencyEnum(val message: String, val httpStatus: HttpStatus) {
    USER_NOT_FOUND("User not found", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_CURRENCY("Invalid currency", HttpStatus.UNPROCESSABLE_ENTITY),
    TRANSACTION_NOT_FOUND("User not found", HttpStatus.UNPROCESSABLE_ENTITY),
}
