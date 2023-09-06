package io.bom.makBase.dto.makkoli

import io.bom.makBase.domain.makkoli.IsLegallyTraditional
import org.openapitools.jackson.nullable.JsonNullable

data class MakkoliUpdateRequest(
    val nameKr: JsonNullable<String> = JsonNullable.undefined(),
    val nameEn: JsonNullable<String?> = JsonNullable.undefined(),
    val alcohol: JsonNullable<Double> = JsonNullable.undefined(),
    val acidity: JsonNullable<Int> = JsonNullable.undefined(),
    val sweetness: JsonNullable<Int> = JsonNullable.undefined(),
    val aspartame: JsonNullable<Double> = JsonNullable.undefined(),
    val price: JsonNullable<Int> = JsonNullable.undefined(),
    val quantityPerBottle: JsonNullable<Double> = JsonNullable.undefined(),
    val style: JsonNullable<String?> = JsonNullable.undefined(),
    val isLegallyTraditional: JsonNullable<IsLegallyTraditional> = JsonNullable.undefined(),
    val makerId: JsonNullable<Long> = JsonNullable.undefined(),
    val regionId: JsonNullable<Long> = JsonNullable.undefined(),
) {

    init {
        if (nameKr.isPresent && nameKr.get().isEmpty()) throw IllegalArgumentException()
        if (alcohol.isPresent && (alcohol.get().isInfinite() || alcohol.get().isNaN())) throw IllegalArgumentException()
        if (acidity.isPresent && acidity.get() !in 1..5) throw IllegalArgumentException()
        if (sweetness.isPresent && sweetness.get() !in 1..5) throw IllegalArgumentException()
        if (aspartame.isPresent && (aspartame.get().isInfinite() || aspartame.get().isNaN())) throw IllegalArgumentException()
        if (quantityPerBottle.isPresent && (quantityPerBottle.get().isInfinite() || quantityPerBottle.get().isNaN())) throw IllegalArgumentException()
    }
}
