package io.bom.makBase.util

import org.springframework.data.domain.Sort

fun getSortMap(sortList: List<String>?, sortConditions: List<String>): Map<String, Sort.Direction> {
    val _sortMap = mutableMapOf<String, Sort.Direction>()
    sortList?.map { it.split(":") }
        ?.forEach {
            if (sortConditions.indexOf(it[0]) > -1) {
                _sortMap[it[0]] = when (it[1]) {
                    "ASC" -> Sort.Direction.ASC
                    "DESC" -> Sort.Direction.DESC
                    else -> throw IllegalArgumentException(
                        "${it[0]} : Only \"ASC\" and \"DESC\" are allowed in the sort order.")
                }
            }
        }
    return _sortMap
}
