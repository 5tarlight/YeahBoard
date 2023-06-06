package io.yeahx4.yeahboard.service

import io.yeahx4.yeahboard.Constants
import io.yeahx4.yeahboard.dto.UserDto
import io.yeahx4.yeahboard.entity.User
import org.springframework.stereotype.Service
import io.yeahx4.yeahboard.repository.UserRepository
import io.yeahx4.yeahboard.role.UserRole
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun saveUser(dto: UserDto.SignUpDto): UserDto.CreateUserResult {
        val duplicate = userRepository.findByEmail(dto.email)

        if (duplicate.isPresent)
            return UserDto.CreateUserResult.Duplicated

        val user = User(
            -1,
            dto.email,
            dto.username,
            passwordEncoder.encode(dto.password),
            UserRole.USER
        )

        userRepository.save(user)
        return UserDto.CreateUserResult.Success
    }

    fun authUser(dto: UserDto.LoginDto): User? {
        val result = userRepository.findByEmail(dto.email)

        if (result.isEmpty)
            return null

        val user = result.get()

        return if (passwordEncoder.matches(dto.password, user.password)) {
            user
        } else {
            null
        }
    }

    fun getUser(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun encodePassword(pw: String): String {
        return passwordEncoder.encode(pw)
    }

    fun getUserByEmail(email: String): Optional<User> {
        return this.userRepository.findByEmail(email)
    }

    fun getUserSession(req: HttpServletRequest): User? {
        val session = req.session ?: return null

        return session.getAttribute(Constants.userLogin) as User?
    }

    fun deleteUser(id: Long) {
        this.userRepository.deleteById(id)
    }
}
