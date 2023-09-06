package io.bom.makBase.repository.makkoli.condition

import io.bom.makBase.util.getSortMap
import org.springframework.data.domain.Sort

data class MakkoliSort(
    val sortMap: Map<String, Sort.Direction>,
) {
    companion object {
        private val sortConditions: List<String> = listOf(
            "nameKr", "nameEn", "alcohol", "acidity", "sweetness", "aspartame", "price")

        fun of(list: List<String>?): MakkoliSort {
            return MakkoliSort(getSortMap(list, sortConditions))
        }
    }
}
