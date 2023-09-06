package io.bom.makBase.dto.rice

import io.bom.makBase.domain.rice.Rice
import io.bom.makBase.dto.region.RegionSummaryResponse
import io.bom.makBase.dto.makkoli.MakkoliSummaryResponse

data class RiceDetailResponse(
    val riceId: Long,
    val nameKr: String,
    val nameEn: String?,
    val protein: Double,
    val amylose: Double,
    val starchValue: Double,
    val gelTemperature: Double,
    val regionList: List<RegionSummaryResponse>,
    val makkoliList: List<MakkoliSummaryResponse>,
) {
    companion object {
        fun fromEntity(rice: Rice): RiceDetailResponse {
            return RiceDetailResponse(
                riceId = rice.id!!,
                nameKr = rice.nameKr,
                nameEn = rice.nameEn,
                protein = rice.protein,
                amylose = rice.amylose,
                starchValue = rice.starchValue,
                gelTemperature = rice.gelTemperature,
                regionList = rice.regionList.filter { it.region.id != null }
                    .map { RegionSummaryResponse(
                        regionId = it.region.id!!,
                        nameKr = it.region.nameKr,
                        nameEn = it.region.nameEn,
                    )},
                makkoliList = rice.makkoliList.filter{ it.makkoli.id != null }
                    .map { MakkoliSummaryResponse(
                        makkoliId = it.makkoli.id!!,
                        nameKr = it.makkoli.nameKr,
                        nameEn = it.makkoli.nameEn,
                        regionName = it.makkoli.region.topParentName)
                    }.distinct()
            )        }
    }
}
