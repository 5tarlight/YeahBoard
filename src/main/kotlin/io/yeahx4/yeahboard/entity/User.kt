package io.yeahx4.yeahboard.entity

import io.yeahx4.yeahboard.dto.UserDto
import io.yeahx4.yeahboard.role.UserRole
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class User(
        @Column(name = "id")
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(nullable = false, length = 80, unique = true)
        var email: String,

        @Column(nullable = false, length = 30)
        var username: String,

        @Column(nullable = false)
        var password: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var role: UserRole,

        @OneToMany(
                mappedBy = "author",
                cascade = [CascadeType.REMOVE],
                fetch = FetchType.LAZY
        )
        val posts: List<Post> = listOf(),

        @OneToMany(
                mappedBy = "author",
                cascade = [CascadeType.REMOVE],
                fetch = FetchType.LAZY
        )
        val comments: List<Comment> = listOf()
) : BaseTimeEntity() {
    fun removePassword(): UserDto.WithoutPasswordUser {
        return UserDto.WithoutPasswordUser(
            id,
            email,
            username,
            role,
            createdAt,
            updatedAt
        )
    }
}
