package io.bom.makBase.dto.maker

import io.bom.makBase.domain.Maker
import io.bom.makBase.dto.region.RegionSummaryResponse
import io.bom.makBase.dto.makkoli.MakkoliSummaryResponse
import io.bom.makBase.exception.NotFoundException

data class MakerDetailResponse(
    val makerId: Long,
    val nameKr: String,
    val nameEn: String?,
    val region: RegionSummaryResponse,
    val makkoliList: List<MakkoliSummaryResponse>,
) {
    companion object {
        fun fromEntity(e: Maker): MakerDetailResponse {
            return MakerDetailResponse(
                makerId = e.id ?: throw NotFoundException("No Maker"),
                nameKr = e.nameKr,
                nameEn = e.nameEn,
                region = RegionSummaryResponse(
                    e.region.id!!,
                    e.region.nameKr,
                    e.region.nameEn,),
                makkoliList = e.makkoliList?.map {
                    m -> MakkoliSummaryResponse.fromEntity(m)
                } ?: listOf(),
            )
        }
    }
}
