package io.bom.makBase.dto.region

import jakarta.validation.constraints.NotBlank

data class RegionCreateRequest(

    @field:NotBlank
    val nameKr: String,

    val nameEn: String?,

    val parentId: Long?,
)
