package io.yeahx4.yeahboard.controller

import io.yeahx4.yeahboard.Constants
import io.yeahx4.yeahboard.dto.UserDto
import io.yeahx4.yeahboard.entity.User
import io.yeahx4.yeahboard.role.UserRole
import io.yeahx4.yeahboard.service.UserService
import io.yeahx4.yeahboard.util.PayloadResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {
    @GetMapping("/test-signup")
    fun testSignup(req: HttpServletRequest): String {
        val session = req.getSession(true)
        session.setAttribute(Constants.userLogin, User(
            -1,
            "test@gmail.com",
            "test",
            "pw",
            UserRole.USER
        ))

        return "Session added"
    }

    @GetMapping("/test-login")
    fun testLogin(req: HttpServletRequest): String {
        val user = userService.getUserSession(req)

        return if (user == null)
            "Unknown"
        else
            "Hello ${user.username}"
    }

    @PostMapping("/signup")
    fun signupUser(@RequestBody dto: UserDto.SignUpDto): ResponseEntity<String> {
        return when (userService.saveUser(dto)) {
            UserDto.CreateUserResult.Success -> {
                ResponseEntity(HttpStatus.CREATED)
            }
            UserDto.CreateUserResult.Duplicated -> {
                ResponseEntity("Already exists", HttpStatus.BAD_REQUEST)
            }
        }
    }

    @PostMapping("/login")
    fun loginUser(
        req: HttpServletRequest,
        @RequestBody dto: UserDto.LoginDto
    ): ResponseEntity<PayloadResponse<UserDto.WithoutPasswordUser>> {
        val user = userService.authUser(dto)

        return if (user != null) {
            val session = req.getSession(true)
            session.setAttribute(Constants.userLogin, user)
            ResponseEntity(PayloadResponse("Ok", user.removePassword()), HttpStatus.OK)
        } else {
            ResponseEntity(PayloadResponse("Invalid Credentials"), HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/logout")
    fun logout(req: HttpServletRequest) {
        val session = req.getSession(false) ?: return

        session.removeAttribute(Constants.userLogin)
//        session.invalidate()
    }

    @GetMapping("/validate")
    fun validate(req: HttpServletRequest): UserDto.WithoutPasswordUser? {
        val user = userService.getUserSession(req)

        return user?.removePassword()
    }

    @PatchMapping("/update")
    @Transactional
    fun updateUser(
        req: HttpServletRequest,
        @RequestBody dto: UserDto.UpdateUserDto
    ): ResponseEntity<PayloadResponse<UserDto.WithoutPasswordUser>> {
        val session = userService.getUserSession(req)
            ?: return ResponseEntity(PayloadResponse("Login First"), HttpStatus.FORBIDDEN)

        val userId = session.id
        val userOptional = userService.getUser(userId)

        if (userOptional.isEmpty)
            return ResponseEntity(PayloadResponse("Invalid Session"), HttpStatus.BAD_REQUEST)

        val user = userOptional.get()

        if (dto.email != null) {
            val dupUser = userService.getUserByEmail(dto.email)
            if (dupUser.isPresent)
                return ResponseEntity(PayloadResponse("Email is already taken"), HttpStatus.BAD_REQUEST)

            user.email = dto.email
        }
        if (dto.username != null)
            user.username = dto.username
        if (dto.password != null)
            user.password = userService.encodePassword(dto.password)

        req.session.setAttribute(Constants.userLogin, user)

        return ResponseEntity(PayloadResponse("Ok", user.removePassword()), HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deleteUser(req: HttpServletRequest): ResponseEntity<String> {
        val user = userService.getUserSession(req)
            ?: return ResponseEntity("Login First", HttpStatus.FORBIDDEN)

        this.userService.deleteUser(user.id)
        this.logout(req)

        return ResponseEntity("Ok", HttpStatus.OK)
    }
}
