package io.bom.makBase.dto.maker

import jakarta.validation.constraints.NotBlank

data class MakerCreateRequest(

    @field:NotBlank
    val nameKr: String,
    val nameEn: String?,

    val regionId: Long,
)
