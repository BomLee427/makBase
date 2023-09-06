package io.bom.makBase.dto.region

import io.bom.makBase.domain.Region

data class RegionFilterResponse(
    val regionList: List<RegionFilterByRegion>
) {
    companion object {
        fun fromEntity(e: List<Region>, existOnly: Boolean): RegionFilterResponse {
            val results = e.map {
                RegionFilterByRegion(
                    it.id!!, it.nameKr, it.nameEn, it.children.size.toLong())
            }.takeIf { existOnly }?.filter { it.childrenCount > 0 } ?: listOf()
            return RegionFilterResponse(results)
        }
    }

    data class RegionFilterByRegion(
        val regionId: Long,
        val nameKr: String,
        val nameEn: String?,
        val childrenCount: Long,
    )

}