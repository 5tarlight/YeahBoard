package io.yeahx4.yeahboard.dto

import io.yeahx4.yeahboard.entity.Comment
import java.time.LocalDateTime

class PostDto {
    data class WritePostDto(
        val title: String,
        val content: String
    )

    data class WithoutPasswordPost(
        val id: Long,
        val title: String,
        val content: String,
        val user: UserDto.WithoutPasswordUser,
        val view: Int,
        val comments: List<Comment>,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    )
}
