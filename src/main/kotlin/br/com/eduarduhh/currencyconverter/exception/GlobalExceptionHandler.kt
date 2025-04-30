package br.com.eduarduhh.currencyconverter.exception

import br.com.eduarduhh.currencyconverter.dto.ApiError
import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
@Hidden
class GlobalExceptionHandler  {


    @ExceptionHandler(CurrencyConversionException::class)
    fun handleCurrencyError(ex: CurrencyConversionException, request: HttpServletRequest): ResponseEntity<ApiError> {
        return buildResponse(ex.currencyEnum.httpStatus, ex.message, request.requestURI)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(ex: Exception, request: HttpServletRequest): ResponseEntity<ApiError> {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.message ?: "Unexpected error", request.requestURI)
    }


    private fun buildResponse(status: HttpStatus, message: String?, path: String): ResponseEntity<ApiError> {
        val error = ApiError(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            path = path
        )
        return ResponseEntity(error, status)
    }
}
