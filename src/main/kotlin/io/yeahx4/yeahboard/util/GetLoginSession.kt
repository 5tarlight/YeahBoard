package io.yeahx4.yeahboard.util

import io.yeahx4.yeahboard.Constants
import io.yeahx4.yeahboard.entity.User
import jakarta.servlet.http.HttpServletRequest

fun getLoginSession(req: HttpServletRequest): User? {
    val session = req.getSession(false)
            ?: return null

    return session.getAttribute(Constants.userLogin) as User?
}
