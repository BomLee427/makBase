package io.bom.makBase.repository.makkoli.condition

import io.bom.makBase.domain.makkoli.IsLegallyTraditional

data class MakkoliFilter(
    val isLegallyTraditional: List<IsLegallyTraditional>?,
    val alcoholMin: Int?,
    val alcoholMax: Int?,
    val priceMin: Int?,
    val priceMax: Int?,
    val style: List<String>?,
    val region: List<Long>?,
)
