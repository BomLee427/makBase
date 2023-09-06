package io.bom.makBase.dto.makkoli

import io.bom.makBase.domain.makkoli.Makkoli
import io.bom.makBase.domain.makkoli.IsLegallyTraditional

data class MakkoliUpdateResponse(
    val makkoliId: Long,
    val isLegallyTraditional: IsLegallyTraditional,
    val nameKr: String,
    val nameEn: String?,
    val alcohol: Double,
    val acidity: Int,
    val sweetness: Int,
    val aspartame: Double,
    val price: Int,
    val style: String?,    // nullable
    val quantityPerBottle: Double,
    val makerId: Long,
    val regionId: Long,
) {
    companion object {
        fun fromEntity(e: Makkoli): MakkoliUpdateResponse {
            return MakkoliUpdateResponse(
                makkoliId = e.id!!,
                isLegallyTraditional = e.isLegallyTraditional,
                nameKr = e.nameKr,
                nameEn = e.nameEn,
                alcohol = e.alcohol,
                acidity = e.acidity,
                sweetness = e.sweetness,
                aspartame = e.aspartame,
                price = e.price,
                style = e.style,
                quantityPerBottle = e.quantityPerBottle,
                makerId = e.maker.id!!,
                regionId = e.region.id!!,
            )
        }
    }
}
