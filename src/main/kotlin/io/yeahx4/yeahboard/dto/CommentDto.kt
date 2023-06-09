package io.yeahx4.yeahboard.dto

class CommentDto {
    data class WriteCommentDto(
        val post: Long,
        val content: String
    )

    data class UpdateCommentDto(
        val post: Long,
        val content: String
    )

    data class DeleteCommentDto(
        val post: Long
    )
}
