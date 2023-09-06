package io.bom.makBase.dto.makkoli

import io.bom.makBase.domain.Region
import io.bom.makBase.domain.makkoli.Makkoli
import io.bom.makBase.domain.makkoli.IsLegallyTraditional
import io.bom.makBase.dto.maker.MakerSummaryResponse
import io.bom.makBase.dto.region.RegionSummaryResponse

data class MakkoliDetailResponse(
    val makkoliId: Long,
    val nameKr: String,
    val nameEn: String?,
    val alcohol: Double,
    val acidity: Int,
    val sweetness: Int,
    val aspartame: Double,
    val price: Int,
    val quantityPerBottle: Double,
    val style: String?,
    val isLegallyTraditional: IsLegallyTraditional,
    val maker: MakerSummaryResponse,
    val region: RegionByMakkoli,
    val flavorList: List<String>?,
    val pairingList: List<String>?,
    val riceList: List<RiceByMakkoli>?,

    ) {

    companion object {
        fun fromEntity(e: Makkoli): MakkoliDetailResponse = MakkoliDetailResponse(
            makkoliId = e.id!!,
            isLegallyTraditional = e.isLegallyTraditional,
            nameKr = e.nameKr,
            nameEn = e.nameEn,
            alcohol = e.alcohol,
            acidity = e.acidity,
            aspartame = e.aspartame,
            sweetness = e.sweetness,
            maker = MakerSummaryResponse(
                e.maker.id!!,
                e.maker.nameKr,
                e.maker.nameEn,
                e.maker.region.nameKr,
            ),
            price = e.price,
            style = e.style,
            quantityPerBottle = e.quantityPerBottle,
            region = RegionByMakkoli.fromEntity(e.region),
            flavorList = e.makkoliFlavorList?.map { it.flavor }?.distinct(),
            pairingList = e.makkoliPairingList?.map { it.pairing }?.distinct(),
            riceList = e.makkoliRiceList?.map { RiceByMakkoli(
                it.rice.id!!,
                it.rice.nameKr,
                it.rice.nameEn) }?.distinct(),
        )
    }

    /**
     * region
     */
    data class RegionByMakkoli(
        val regionId: Long,
        val nameKr: String,
        val nameEn: String?,
        val parents: List<RegionSummaryResponse>?,
    ) {
        companion object {
            fun fromEntity(e: Region): RegionByMakkoli {
                return RegionByMakkoli(
                    regionId = e.id!!,
                    nameKr = e.nameKr,
                    nameEn = e.nameEn,
                    parents = e.allParentRegions
                )
            }
        }
    }

    /**
     * rice
     */
    data class RiceByMakkoli(
        val riceId: Long,
        val nameKr: String,
        val nameEn: String?,
    )
}