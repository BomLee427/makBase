package io.bom.makBase.repository.maker.condition

import io.bom.makBase.util.getSortMap
import org.springframework.data.domain.Sort

data class MakerSort(
    val sortMap: Map<String, Sort.Direction>,
) {
    companion object {
        private val sortConditions: List<String> = listOf("nameKr", "nameEn")

        fun of(list: List<String>?): MakerSort {
            return MakerSort(getSortMap(list, sortConditions))
        }
    }
}
