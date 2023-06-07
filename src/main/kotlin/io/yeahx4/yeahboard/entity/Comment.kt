package io.yeahx4.yeahboard.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column
        val id: Long,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(nullable = false)
        val post: Post,

        @Column(length = 200, nullable = false)
        var content: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(nullable = false)
        val author: User
): BaseTimeEntity()
