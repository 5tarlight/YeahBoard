package io.yeahx4.yeahboard.repository

import io.yeahx4.yeahboard.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>