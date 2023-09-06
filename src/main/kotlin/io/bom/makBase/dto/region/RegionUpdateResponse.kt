package io.bom.makBase.dto.region

import io.bom.makBase.domain.Region

data class RegionUpdateResponse(

    val nameKr: String,
    val nameEn: String?,
    val parentId: Long?,
) {
    companion object {
        fun fromEntity(e: Region): RegionUpdateResponse {
            return RegionUpdateResponse(
                nameKr = e.nameKr,
                nameEn = e.nameEn,
                parentId = e.parent?.id
            )
        }
    }
}
