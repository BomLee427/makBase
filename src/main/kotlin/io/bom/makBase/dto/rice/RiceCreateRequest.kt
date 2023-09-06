package io.bom.makBase.dto.rice

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class RiceCreateRequest(
    @field:NotBlank
    val nameKr: String,

    @field:NotBlank
    val nameEn: String?,

    val protein: Double,

    val amylose: Double,

    val starchValue: Double,

    val gelTemperature: Double,
)
