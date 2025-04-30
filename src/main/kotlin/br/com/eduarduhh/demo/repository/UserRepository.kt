package br.com.eduarduhh.demo.repository

import br.com.eduarduhh.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>