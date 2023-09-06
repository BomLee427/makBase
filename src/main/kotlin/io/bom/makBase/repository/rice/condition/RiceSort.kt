package io.bom.makBase.repository.rice.condition

import io.bom.makBase.util.getSortMap
import org.springframework.data.domain.Sort

data class RiceSort(
    val sortMap: Map<String, Sort.Direction>,
) {
    companion object {
        private val sortConditions: List<String> = listOf(
            "nameKr", "nameEn", "protein", "amylose", "starchValue", "gelTemperature")

        fun of(sortList: List<String>?): RiceSort {
            return RiceSort(getSortMap(sortList, sortConditions))
        }
    }
}
