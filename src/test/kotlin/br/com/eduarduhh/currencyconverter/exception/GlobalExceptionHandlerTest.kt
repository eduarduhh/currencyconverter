package br.com.eduarduhh.currencyconverter.exception


import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import java.sql.SQLException


class GlobalExceptionHandlerTest {
    @Autowired
    private lateinit var handler: GlobalExceptionHandler
    @Autowired
    private lateinit var request: HttpServletRequest

    @BeforeEach
    fun setUp() {
        handler = GlobalExceptionHandler()
        request = mockk()
        every { request.requestURI } returns "/api/convert"
    }

    @Test
    fun `handleCurrencyError should return proper response`() {
        val ex = CurrencyConversionException(
            message = "Invalid currency",
            currencyEnum = CurrencyEnum.INVALID_CURRENCY
        )

        val response = handler.handleCurrencyError(ex, request)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.statusCode)
        assertEquals("Invalid currency", response.body?.message)
        assertEquals("/api/convert", response.body?.path)
        assertEquals(CurrencyEnum.INVALID_CURRENCY.name, response.body?.errorType)
    }

    @Test
    fun `handleValidationExceptions should return validation error response`() {
        val ex = mockk<MethodArgumentNotValidException>()
        val fieldError = mockk<org.springframework.validation.FieldError>()

        every { fieldError.field } returns "amount"
        every { fieldError.defaultMessage } returns "must be positive"
        every { ex.bindingResult.fieldErrors } returns listOf(fieldError)
        every { ex.message } returns "Validation failed"

        val response = handler.handleValidationExceptions(ex, request)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Dados de entrada inválidos", response.body?.message)
        assertEquals(mapOf("amount" to "must be positive"), response.body?.details)
    }

    @Test
    fun `handleDataIntegrityViolation with unique constraint should return conflict`() {
        val sqlEx = SQLException("unique constraint violation")
        val ex = DataIntegrityViolationException("DB error", sqlEx)

        val response = handler.handleDataIntegrityViolation(ex, request)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Violação de constraint única: registro duplicado", response.body?.message)
    }

    @Test
    fun `handleDataIntegrityViolation with foreign key constraint should return bad request`() {
        val sqlEx = SQLException("foreign key constraint violation")
        val ex = DataIntegrityViolationException("DB error", sqlEx)

        val response = handler.handleDataIntegrityViolation(ex, request)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Violação de chave estrangeira: referência inválida", response.body?.message)
    }

    @Test
    fun `handleDataIntegrityViolation with not-null constraint should return bad request`() {
        val sqlEx = SQLException("not-null constraint violation")
        val ex = DataIntegrityViolationException("DB error", sqlEx)

        val response = handler.handleDataIntegrityViolation(ex, request)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Campo obrigatório não informado", response.body?.message)
    }

    @Test
    fun `handleDataIntegrityViolation with unknown SQL error should return bad request`() {
        val sqlEx = SQLException("unknown error")
        val ex = DataIntegrityViolationException("DB error", sqlEx)

        val response = handler.handleDataIntegrityViolation(ex, request)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Erro de integridade de dados", response.body?.message)
    }

    @Test
    fun `handleDataIntegrityViolation with non-SQL cause should return bad request`() {
        val ex = DataIntegrityViolationException("DB error", RuntimeException("some error"))

        val response = handler.handleDataIntegrityViolation(ex, request)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Erro na operação com o banco de dados", response.body?.message)
    }

    @Test
    fun `handleGenericError should return internal server error`() {
        val ex = Exception("Something went wrong")

        val response = handler.handleGenericError(ex, request)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Erro interno no servidor", response.body?.message)
    }
}