package io.bom.makBase.repository.rice.customRepository

import io.bom.makBase.dto.rice.RiceDetailResponse
import io.bom.makBase.dto.rice.RiceFilterResponse
import io.bom.makBase.dto.rice.RiceSummaryResponse
import io.bom.makBase.repository.rice.condition.RiceFilter
import io.bom.makBase.repository.rice.condition.RiceSearch
import io.bom.makBase.repository.rice.condition.RiceSort
import org.springframework.data.domain.Pageable

interface RiceCustomRepository {
    fun searchRicesByCondition(
        page: Pageable, filter: RiceFilter, search: RiceSearch, sort: RiceSort): List<RiceSummaryResponse>

    fun getRiceFilter(existOnly: Boolean): RiceFilterResponse
}
