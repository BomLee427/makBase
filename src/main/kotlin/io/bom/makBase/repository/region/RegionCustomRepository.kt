package io.bom.makBase.repository.region

import io.bom.makBase.dto.region.RegionFilterResponse
import io.bom.makBase.dto.region.RegionSummaryResponse
import io.bom.makBase.repository.region.condition.RegionFilter
import io.bom.makBase.repository.region.condition.RegionSearch
import io.bom.makBase.repository.region.condition.RegionSort
import org.springframework.data.domain.Pageable

interface RegionCustomRepository {

    fun getRegionFilter(existOnly: Boolean): RegionFilterResponse
    fun searchRegionsByCondition(page: Pageable, filter: RegionFilter, search: RegionSearch, sort: RegionSort
    ): List<RegionSummaryResponse>
}
