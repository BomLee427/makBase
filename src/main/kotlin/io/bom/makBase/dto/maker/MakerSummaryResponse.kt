package io.bom.makBase.dto.maker

import io.bom.makBase.domain.Maker

data class MakerSummaryResponse(
    val makerId: Long,
    val nameKr: String,
    val nameEn: String??,
    val regionName: String,
) {
    companion object {
        fun fromEntity(e: Maker): MakerSummaryResponse {
            return MakerSummaryResponse(
                makerId = e.id!!,
                nameKr = e.nameKr,
                nameEn = e.nameEn,
                regionName = e.region.nameKr
            )
        }
    }
}
