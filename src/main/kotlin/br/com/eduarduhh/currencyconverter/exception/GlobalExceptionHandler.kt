package br.com.eduarduhh.currencyconverter.exception

import br.com.eduarduhh.currencyconverter.dto.ApiError
import br.com.eduarduhh.currencyconverter.util.log
import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException
import java.time.LocalDateTime

@RestControllerAdvice
@Hidden
class GlobalExceptionHandler {
    private val log = log()

    @ExceptionHandler(CurrencyConversionException::class)
    fun handleCurrencyError(
        ex: CurrencyConversionException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiError> {
        log.info("Erro de negocio $ex")

        return buildResponse(
            status = ex.currencyEnum.httpStatus,
            message = ex.message,
            path = request.requestURI,
            errorType = ex.currencyEnum.name,
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiError> {
        val errors =
            ex.bindingResult.fieldErrors.associate { error ->
                error.field to (error.defaultMessage ?: "Erro de validação")
            }

        log.info("Erro de validação $ex")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiError(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Erro de validação",
                message = "Dados de entrada inválidos",
                path = request.requestURI,
                details = errors,
            ),
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(
        ex: DataIntegrityViolationException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiError> {
        val (status, message) =
            when (val rootCause = ex.rootCause) {
                is SQLException -> {
                    when {
                        rootCause.message?.contains("unique constraint") == true ->
                            HttpStatus.CONFLICT to "Violação de constraint única: registro duplicado"
                        rootCause.message?.contains("foreign key constraint") == true ->
                            HttpStatus.BAD_REQUEST to "Violação de chave estrangeira: referência inválida"
                        rootCause.message?.contains("not-null constraint") == true ->
                            HttpStatus.BAD_REQUEST to "Campo obrigatório não informado"
                        else -> HttpStatus.BAD_REQUEST to "Erro de integridade de dados"
                    }
                }
                else -> HttpStatus.BAD_REQUEST to "Erro na operação com o banco de dados"
            }

        return buildResponse(
            status = status,
            message = message,
            path = request.requestURI,
            errorType = "DATA_INTEGRITY_VIOLATION",
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ApiError> {
        return buildResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = "Erro interno no servidor",
            path = request.requestURI,
            errorType = "INTERNAL_SERVER_ERROR",
        )

        log.error("Erro interno não tratado: ${ex.message}", ex)
    }

    private fun buildResponse(
        status: HttpStatus,
        message: String?,
        path: String,
        errorType: String? = null,
    ): ResponseEntity<ApiError> {
        val error =
            ApiError(
                timestamp = LocalDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = message,
                path = path,
                errorType = errorType,
            )
        return ResponseEntity(error, status)
    }
}
