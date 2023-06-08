package io.yeahx4.yeahboard.service

import io.yeahx4.yeahboard.dto.PostDto
import io.yeahx4.yeahboard.entity.Post
import io.yeahx4.yeahboard.entity.User
import io.yeahx4.yeahboard.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    fun checkAuthor(user: User, post: Post): Boolean {
        return user.id == post.author.id
    }

    fun lostPosts(pageable: Pageable): Page<Post> {
        return this.postRepository.findAll(pageable)
    }

    fun mapPostPage(page: Page<Post>): Page<PostDto.WithoutPasswordPost> {
        val result = page.map {
            it.removeAuthorPassword()
        }

        return result
    }
}
