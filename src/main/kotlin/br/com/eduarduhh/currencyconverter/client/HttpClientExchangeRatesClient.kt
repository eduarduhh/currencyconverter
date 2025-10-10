import br.com.eduarduhh.currencyconverter.client.ExchangeRatesClient
import br.com.eduarduhh.currencyconverter.dto.ExchangeRatesResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.websocket.Endpoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class HttpClientExchangeRatesClient(
    @Value("\${api.url}") private val url: String,
    @Value("\${api.endpoint}") private val endpoint: String
) : ExchangeRatesClient {
    private val httpClient: HttpClient = HttpClient.newBuilder().build()
    private val objectMapper = jacksonObjectMapper()

    override fun getLatestRates(base: String, accessKey: String): ExchangeRatesResponse {
        // Construir a URL com os parâmetros
        val url = "$url/$endpoint?base=$base&access_key=$accessKey"

        // Criar a requisição HTTP
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build()

        // Enviar a requisição e obter a resposta
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        // Verificar o status da resposta
        if (response.statusCode() != 200) {
            throw RuntimeException("Erro ao chamar API: ${response.statusCode()}")
        }

        // Desserializar a resposta JSON para o objeto ExchangeRatesResponse
        return objectMapper.readValue(response.body())
    }
}