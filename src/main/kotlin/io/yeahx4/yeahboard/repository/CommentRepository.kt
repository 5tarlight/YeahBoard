package io.yeahx4.yeahboard.repository

import io.yeahx4.yeahboard.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long>