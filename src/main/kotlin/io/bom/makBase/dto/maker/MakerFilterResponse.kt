package io.bom.makBase.dto.maker

data class MakerFilterResponse(
    val regionList: List<MakerFilterByRegion>
) {

    data class MakerFilterByRegion(
        val regionId: Long,
        val nameKr: String,
        val regionCount: Long,
    )

}
