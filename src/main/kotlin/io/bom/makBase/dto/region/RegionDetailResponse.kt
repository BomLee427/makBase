package io.bom.makBase.dto.region

import io.bom.makBase.domain.Region

data class RegionDetailResponse(
    val regionId: Long,
    val nameKr: String,
    val nameEn: String?,
    val makkoliList: List<MakkoliSummaryByRegion>,
    val riceList: List<RiceSummaryByRegion>,
    val makerList: List<MakerSummaryByRegion>,
    val parentRegionList: List<RegionSummaryResponse>,
) {
    companion object {
        fun fromEntity(e: Region): RegionDetailResponse {
            return RegionDetailResponse(
                regionId = e.id!!,
                nameKr = e.nameKr,
                nameEn = e.nameEn,
                makkoliList = e.makkoliList.map {
                    MakkoliSummaryByRegion(it.id!!, it.nameKr, it.nameEn) },
                riceList = e.riceShareList.map {
                    RiceSummaryByRegion(it.rice.id!!, it.rice.nameKr, it.rice.nameEn) },
                makerList = e.makerList.map {
                    MakerSummaryByRegion(it.id!!, it.nameKr, it.nameEn) },
                parentRegionList = e.allParentRegions
                )
        }
    }

    data class MakkoliSummaryByRegion(
        val makkoliId: Long,
        val nameKr: String,
        val nameEn: String?,
    )


    data class MakerSummaryByRegion(
        val makerId: Long,
        val nameKr: String,
        val nameEn: String?,
    )

    data class RiceSummaryByRegion(
        val riceId: Long,
        val nameKr: String,
        val nameEn: String?,
    )
}
