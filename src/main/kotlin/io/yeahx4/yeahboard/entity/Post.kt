package io.yeahx4.yeahboard.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy

@Entity
data class Post(
        @Column
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(length = 30, nullable = false)
        val title: String,

        @Column(columnDefinition = "TEXT", nullable = false)
        val content: String,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(nullable = false)
        val author: User,

        @Column(columnDefinition = "integer default 0")
        val view: Int,

        @OneToMany(
                mappedBy = "post",
                fetch = FetchType.EAGER,
                cascade = [CascadeType.REMOVE]
        )
        @OrderBy("id asc")
        val comments: List<Comment> = listOf()
): BaseTimeEntity()
