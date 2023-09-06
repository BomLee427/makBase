package io.bom.makBase.dto.makkoli

import io.bom.makBase.domain.makkoli.Makkoli
import io.bom.makBase.domain.makkoli.IsLegallyTraditional

data class MakkoliSummaryResponse(
    val makkoliId: Long,
    val nameKr: String,
    val nameEn: String?,
    val regionName: String?,
) {
    companion object {
        fun fromEntity(e: Makkoli): MakkoliSummaryResponse {
            return MakkoliSummaryResponse(
                makkoliId = e.id!!,
                nameKr = e.nameKr,
                nameEn = e.nameEn,
                regionName = e.region.topParentName ?: e.region.nameKr
            )
        }
    }
}
