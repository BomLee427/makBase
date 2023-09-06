package io.bom.makBase.repository.makkoli.customRepository

import io.bom.makBase.dto.makkoli.MakkoliFilterResponse
import io.bom.makBase.dto.makkoli.MakkoliSummaryResponse
import io.bom.makBase.repository.makkoli.condition.MakkoliFilter
import io.bom.makBase.repository.makkoli.condition.MakkoliSearch
import io.bom.makBase.repository.makkoli.condition.MakkoliSort
import org.springframework.data.domain.Pageable

interface MakkoliCustomRepository {
    fun searchMakkoliByCondition(
        page: Pageable, filter: MakkoliFilter, search: MakkoliSearch, sort: MakkoliSort): List<MakkoliSummaryResponse>

    fun getMakkoliFilter(existOnly: Boolean): MakkoliFilterResponse
}
