package io.bom.makBase.repository.region.condition

import io.bom.makBase.util.getSortMap
import org.springframework.data.domain.Sort

data class RegionSort(
    val sortMap: Map<String, Sort.Direction>,
) {
    companion object {
        private val sortConditions: List<String> = listOf("nameKr", "nameEn")

        fun of(list: List<String>?): RegionSort {
            return RegionSort(getSortMap(list, sortConditions))
        }
    }
}
