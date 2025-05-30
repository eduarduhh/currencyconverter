package br.com.eduarduhh.currencyconverter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*") // Or specify allowed origins
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
    }

    @Bean
    fun forwardedHeaderFilter(): ForwardedHeaderFilter {
        return ForwardedHeaderFilter()
    }
}
