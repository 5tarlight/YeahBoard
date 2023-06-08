package io.yeahx4.yeahboard.util

import java.util.Optional

fun <T> unwrap(optional: Optional<T>): T? {
    return if (optional.isPresent)
        optional.get()
    else
        null
}
