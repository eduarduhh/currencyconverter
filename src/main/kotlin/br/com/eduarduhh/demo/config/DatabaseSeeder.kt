package br.com.eduarduhh.demo.config

import br.com.eduarduhh.demo.model.User
import br.com.eduarduhh.demo.repository.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DatabaseSeeder(
    private val userRepository: UserRepository
) {

    @PostConstruct
    fun seed() {
        if (userRepository.count() == 0L) {
            val users = listOf(
                User(name = "Alice"),
                User(name = "Bob"),
                User(name = "Carol"),
                User(name = "Eduardo")
            )
            userRepository.saveAll(users)
        }
    }
}