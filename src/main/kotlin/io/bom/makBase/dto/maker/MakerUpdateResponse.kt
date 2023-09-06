package io.bom.makBase.dto.maker

import io.bom.makBase.domain.Maker

data class MakerUpdateResponse(
    val makerId: Long,
    val nameKr: String,
    val nameEn: String?,
    val regionId: Long,
) {
    companion object {
        fun fromEntity(e: Maker): MakerUpdateResponse {
            return MakerUpdateResponse(
                makerId = e.id!!,
                nameKr = e.nameKr,
                nameEn = e.nameEn,
                regionId = e.region.id!!
            )
        }
    }
}
