package io.bom.makBase.dto.maker

import org.openapitools.jackson.nullable.JsonNullable

data class MakerUpdateRequest(
    val nameKr: JsonNullable<String> = JsonNullable.undefined(),
    val nameEn: JsonNullable<String?> = JsonNullable.undefined(),
    val regionId: JsonNullable<Long> = JsonNullable.undefined(),
) {
    init {
        if (nameKr.isPresent && nameKr.get().isEmpty()) throw IllegalArgumentException()
    }
}
