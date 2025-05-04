package br.com.eduarduhh.currencyconverter.repository

import br.com.eduarduhh.currencyconverter.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>
