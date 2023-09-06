package io.bom.makBase.dto.region

import org.openapitools.jackson.nullable.JsonNullable

data class RegionUpdateRequest(
    val nameKr: JsonNullable<String> = JsonNullable.undefined(),
    val nameEn: JsonNullable<String?> = JsonNullable.undefined(),
    val parentId: JsonNullable<Long?> = JsonNullable.undefined(),
) {
    init {
        if (nameKr.isPresent && nameKr.get().isEmpty()) throw IllegalArgumentException()
    }
}
