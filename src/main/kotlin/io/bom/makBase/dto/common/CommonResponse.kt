package io.bom.makBase.dto.common

import java.time.LocalDateTime

data class CommonResponse<T>(
    val message: String,
    val time: LocalDateTime = LocalDateTime.now(),
    val data: T?,
) {
    companion object {
        fun <T> of(message: String = "SUCCESS", data: T?): CommonResponse<T> {
            return CommonResponse(message = message, data = data)
        }
    }
}
