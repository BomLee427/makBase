package io.bom.makBase.dto.makkoli

import io.bom.makBase.domain.makkoli.IsLegallyTraditional

data class MakkoliFilterResponse(
    val style: List<MakkoliFilterByStyle>,
    val isLegallyTraditional: List<MakkoliFilterByTraditional>,
    val region: List<MakkoliFilterByRegion>,
) {

    data class MakkoliFilterByTraditional(
        val isLegallyTraditional: IsLegallyTraditional,
        val count: Long,
    )

    data class MakkoliFilterByStyle(
        val makkoliStyle: String,
        val count: Long,
    )


    data class MakkoliFilterByRegion(
        val regionId: Long,
        val nameKr: String,
        val nameEn: String?,
        val count: Long,
    )
}
