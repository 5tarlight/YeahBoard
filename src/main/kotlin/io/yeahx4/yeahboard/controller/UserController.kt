package io.yeahx4.yeahboard.controller

import io.yeahx4.yeahboard.Constants
import io.yeahx4.yeahboard.dto.UserDto
import io.yeahx4.yeahboard.entity.User
import io.yeahx4.yeahboard.role.UserRole
import io.yeahx4.yeahboard.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
        session.setAttribute("user", User(
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
        val session = req.session
        val user = session.getAttribute(Constants.userLogin) as User?

        if (user == null)
            return "Unknown"
        else
            return "Hello ${user.username}"
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
    ): ResponseEntity<UserDto.WithoutPasswordUser> {
        val user = userService.authUser(dto)

        return if (user != null) {
            val session = req.getSession(true)
            session.setAttribute(Constants.userLogin, user)
            ResponseEntity(user.removePassword(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/logout")
    fun logout(req: HttpServletRequest) {
        val session = req.getSession(false) ?: return

        session.removeAttribute(Constants.userLogin)
        session.invalidate()
    }
}
