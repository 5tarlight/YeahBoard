package io.yeahx4.yeahboard.repository

import io.yeahx4.yeahboard.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}
