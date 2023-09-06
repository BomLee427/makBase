package io.bom.makBase.dto.rice

data class RiceFilterResponse(
    val regionList: List<RiceFilterByRegion>
) {

    data class RiceFilterByRegion(
        val regionId: Long,
        val nameKr: String,
        val nameEn: String?,
        val riceCount: Long,
    )

}
