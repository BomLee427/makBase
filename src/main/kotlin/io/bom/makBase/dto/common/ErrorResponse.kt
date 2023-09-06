package io.bom.makBase.dto.common

import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val time: LocalDateTime = LocalDateTime.now(),
    val debugMessage: String? = null,
)
