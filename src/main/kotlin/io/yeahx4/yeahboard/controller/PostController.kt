package io.yeahx4.yeahboard.controller

import io.yeahx4.yeahboard.dto.PostDto
import io.yeahx4.yeahboard.service.PostService
import io.yeahx4.yeahboard.util.PayloadResponse
import io.yeahx4.yeahboard.util.getLoginSession
import io.yeahx4.yeahboard.util.unwrap
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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

    @PatchMapping("/update")
    @Transactional
    fun updatePost(
        req: HttpServletRequest,
        @RequestBody dto: PostDto.EditPostDto
    ): ResponseEntity<PayloadResponse<PostDto.WithoutPasswordPost>> {
        val user = getLoginSession(req)
            ?: return ResponseEntity(PayloadResponse("Login First"), HttpStatus.FORBIDDEN)
        val post = unwrap(this.postService.findPostById(dto.id))
            ?: return ResponseEntity(PayloadResponse("Post Not Found"), HttpStatus.NOT_FOUND)

        val isAuthor = this.postService.checkAuthor(user, post)

        if (!isAuthor)
            return ResponseEntity(PayloadResponse("No Permission"), HttpStatus.FORBIDDEN)

        if (dto.title != null)
            post.title = dto.title
        if (dto.content != null)
            post.content = dto.content

        return ResponseEntity(PayloadResponse("Ok", post.removeAuthorPassword()), HttpStatus.OK)
    }

    @GetMapping("/list")
    fun postList(
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["id"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ResponseEntity<PayloadResponse<Page<PostDto.WithoutPasswordPost>>> {
        val page = this.postService.lostPosts(pageable)

        return ResponseEntity(
            PayloadResponse("Ok", this.postService.mapPostPage(page)),
            HttpStatus.OK
        )
    }

    @PostMapping("/mock")
    fun mockPosts(req: HttpServletRequest): ResponseEntity<String> {
        val user = getLoginSession(req)
            ?: return ResponseEntity("Login First", HttpStatus.FORBIDDEN)

        for (i in 1..100)
            this.postService.writePost(
                PostDto.WritePostDto("title $i", "content $i"),
                user
            )

        return ResponseEntity("Ok", HttpStatus.OK)
    }

    @DeleteMapping("/delete")
    fun deletePost(req: HttpServletRequest, @RequestBody dto: PostDto.DeletePostDto): ResponseEntity<String> {
        val user = getLoginSession(req)
            ?: return ResponseEntity("Login First", HttpStatus.FORBIDDEN)
        val post = unwrap(this.postService.findPostById(dto.id))
            ?: return ResponseEntity("Post Not Found", HttpStatus.NOT_FOUND)

        val isAuthor = this.postService.checkAuthor(user, post)

        if (!isAuthor)
            return ResponseEntity("No Permission", HttpStatus.FORBIDDEN)

        this.postService.deletePost(dto.id)
        return ResponseEntity("Ok", HttpStatus.OK)
    }
}
