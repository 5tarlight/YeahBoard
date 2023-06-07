package io.yeahx4.yeahboard.controller

import io.yeahx4.yeahboard.dto.PostDto
import io.yeahx4.yeahboard.entity.Post
import io.yeahx4.yeahboard.service.PostService
import io.yeahx4.yeahboard.util.PayloadResponse
import io.yeahx4.yeahboard.util.getLoginSession
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/post")
class PostController(
        private val postService: PostService
) {
    @PostMapping("/write")
    fun writePost(
        req: HttpServletRequest,
        @RequestBody dto: PostDto.WritePostDto
    ): ResponseEntity<String> {
        val user = getLoginSession(req)
            ?: return ResponseEntity("Login First", HttpStatus.FORBIDDEN)

        this.postService.writePost(dto, user)
        return ResponseEntity("Ok", HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @Transactional
    fun viewPost(
        req: HttpServletRequest,
        @PathVariable id: Long
    ): ResponseEntity<PayloadResponse<PostDto.WithoutPasswordPost>> {
        val user = getLoginSession(req)
        val postOptional = this.postService.findPostById(id)

        if (postOptional.isEmpty)
            return ResponseEntity(PayloadResponse("Post not found"), HttpStatus.NOT_FOUND)

        val post = postOptional.get()

        if (user != null)
            post.view = post.view + 1

        return ResponseEntity(
            PayloadResponse("Ok", post.removeAuthorPassword()),
            HttpStatus.OK
        )
    }
}
