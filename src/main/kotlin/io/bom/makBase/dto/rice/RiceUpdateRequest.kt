package io.bom.makBase.dto.rice

import org.openapitools.jackson.nullable.JsonNullable

data class RiceUpdateRequest(
    var nameKr: JsonNullable<String> = JsonNullable.undefined(),
    var nameEn: JsonNullable<String?> = JsonNullable.undefined(),
    var protein: JsonNullable<Double> = JsonNullable.undefined(),
    var amylose: JsonNullable<Double> = JsonNullable.undefined(),
    var starchValue: JsonNullable<Double> = JsonNullable.undefined(),
    var gelTemperature: JsonNullable<Double> = JsonNullable.undefined(),
) {
    init {
        if (nameKr.isPresent && nameKr.get().isEmpty()) throw IllegalArgumentException()
        if (protein.isPresent && (protein.get().isInfinite() || protein.get().isNaN())) throw IllegalArgumentException()
        if (amylose.isPresent && (amylose.get().isInfinite() || amylose.get().isNaN())) throw IllegalArgumentException()
        if (starchValue.isPresent && (starchValue.get().isInfinite() || starchValue.get().isNaN())) throw IllegalArgumentException()
        if (gelTemperature.isPresent && (gelTemperature.get().isInfinite() || gelTemperature.get().isNaN())) throw IllegalArgumentException()
    }
}
