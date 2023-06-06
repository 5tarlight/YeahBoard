package io.yeahx4.yeahboard.dto

import io.yeahx4.yeahboard.role.UserRole
import java.time.LocalDateTime

class UserDto {
    enum class CreateUserResult {
        Success,
        Duplicated,
    }

    data class SignUpDto(
        val email: String,
        val username: String,
        val password: String
    )

    data class LoginDto(
        val email: String,
        val password: String
    )

    data class WithoutPasswordUser(
        val id: Long,
        val email: String,
        val username: String,
        val role: UserRole,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    )

    data class UpdateUserDto(
        val email: String?,
        val username: String?,
        val password: String?
    )
}
