package io.bom.makBase.dto.region

import io.bom.makBase.domain.Region

data class RegionSummaryResponse(
    val regionId: Long,
    val nameKr: String,
    val nameEn: String?,
) {
    companion object {
        fun fromEntity(e: Region): RegionSummaryResponse {
            return RegionSummaryResponse(
                regionId = e.id!!,
                nameKr = e.nameKr,
                nameEn = e.nameEn,
            )
        }
    }
}
