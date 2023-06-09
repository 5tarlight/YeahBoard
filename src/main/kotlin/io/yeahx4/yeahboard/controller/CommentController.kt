package io.yeahx4.yeahboard.controller

import io.yeahx4.yeahboard.dto.CommentDto
import io.yeahx4.yeahboard.service.CommentService
import io.yeahx4.yeahboard.service.PostService
import io.yeahx4.yeahboard.util.getLoginSession
import io.yeahx4.yeahboard.util.unwrap
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/comment")
class CommentController(
    private val commentService: CommentService,
    private val postService: PostService,
) {
    @PostMapping("/write")
    fun writeComment(
        req: HttpServletRequest,
        @RequestBody dto: CommentDto.WriteCommentDto
    ): ResponseEntity<String> {
        val user = getLoginSession(req)
            ?: return ResponseEntity("Login First", HttpStatus.FORBIDDEN)
        val post = unwrap(postService.findPostById(dto.post))
            ?: return ResponseEntity("Post Not Found", HttpStatus.NOT_FOUND)

        this.commentService.writeComment(post, user, dto.content)
        return ResponseEntity("Ok", HttpStatus.OK)
    }

    @PatchMapping("/update")
    fun updateComment(
        req: HttpServletRequest,
        @RequestBody dto: CommentDto.UpdateCommentDto
    ) {

    }
}
