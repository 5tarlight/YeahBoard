package io.yeahx4.yeahboard.service

import io.yeahx4.yeahboard.entity.Comment
import io.yeahx4.yeahboard.entity.Post
import io.yeahx4.yeahboard.entity.User
import io.yeahx4.yeahboard.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService(private val commentRepository: CommentRepository) {
    fun writeComment(post: Post, user: User, content: String) {
        commentRepository.save(
            Comment(
                -1,
                post,
                content,
                user
            )
        )
    }
}
