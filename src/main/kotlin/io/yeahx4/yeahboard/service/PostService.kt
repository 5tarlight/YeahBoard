package io.yeahx4.yeahboard.service

import io.yeahx4.yeahboard.dto.PostDto
import io.yeahx4.yeahboard.entity.Post
import io.yeahx4.yeahboard.entity.User
import io.yeahx4.yeahboard.repository.PostRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun writePost(dto: PostDto.WritePostDto, user: User) {
        val post = Post(
            -1,
            dto.title,
            dto.content,
            user,
            0
        )
        this.postRepository.save(post)
    }

    fun findPostById(id: Long): Optional<Post> {
        return postRepository.findById(id)
    }
}
