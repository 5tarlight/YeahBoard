package io.yeahx4.yeahboard.util

data class PayloadResponse<T>(
    val message: String?,
    val data: T? = null
)
