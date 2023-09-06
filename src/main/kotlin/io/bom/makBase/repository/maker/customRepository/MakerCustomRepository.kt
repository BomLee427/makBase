package io.bom.makBase.repository.maker.customRepository

import io.bom.makBase.dto.maker.MakerFilterResponse
import io.bom.makBase.dto.maker.MakerSummaryResponse
import io.bom.makBase.repository.maker.condition.MakerFilter
import io.bom.makBase.repository.maker.condition.MakerSearch
import io.bom.makBase.repository.maker.condition.MakerSort
import org.springframework.data.domain.PageRequest

interface MakerCustomRepository {
    fun searchMakersByCondition(
        page: PageRequest, filter: MakerFilter, search: MakerSearch, sort: MakerSort): List<MakerSummaryResponse>

    fun getMakerFilter(existOnly: Boolean): MakerFilterResponse

}
