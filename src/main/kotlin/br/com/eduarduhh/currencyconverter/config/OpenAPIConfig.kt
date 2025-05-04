package br.com.eduarduhh.currencyconverter.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Currency Converter API")
                    .version("1.0.0")
                    .description("Converts currencies and stores transactions for users"),
            )
    }
}
