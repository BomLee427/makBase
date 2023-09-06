package io.bom.makBase.dto.rice

import io.bom.makBase.dto.region.RegionSummaryResponse

data class RiceSummaryResponse(
    val riceId: Long,
    val nameKr: String,
    val nameEn: String?,
) {
    lateinit var regionList: List<RegionSummaryResponse>
}
