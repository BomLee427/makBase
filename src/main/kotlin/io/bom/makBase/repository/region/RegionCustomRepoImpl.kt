package io.bom.makBase.repository.region

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import io.bom.makBase.domain.QRegion.region
import io.bom.makBase.dto.region.RegionFilterResponse
import io.bom.makBase.dto.region.RegionFilterResponse.*
import io.bom.makBase.dto.region.RegionSummaryResponse
import io.bom.makBase.repository.region.condition.RegionFilter
import io.bom.makBase.repository.region.condition.RegionSearch
import io.bom.makBase.repository.region.condition.RegionSort
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class RegionCustomRepoImpl(
    private val queryFactory: JPAQueryFactory,
) : RegionCustomRepository {

    /**
     * 다수 조회
     */
    override fun searchRegionsByCondition(page: Pageable, filter: RegionFilter, search: RegionSearch, sort: RegionSort
    ): List<RegionSummaryResponse> {
        val query = queryFactory.select(
            Projections.constructor(
                RegionSummaryResponse::class.java,
                region.id!!,
                region.nameKr,
                region.nameEn,
            ))
            .from(region)
            .where(
                region.deletedAt.isNull,
                filterByParents(filter.parent),
                searchByName(search.name)
            )
            sort.sortMap["name"]?.let { query.orderBy(sortByNameKr(it)) }
            sort.sortMap["nameEn"]?.let { query.orderBy(sortByNameEn(it)) }

        return query.orderBy(region.id.asc())
            .limit(page.pageSize.toLong()).offset(page.offset)
            .fetch()
    }

    /**
     * 필터 조회
     */
    override fun getRegionFilter(existOnly: Boolean): RegionFilterResponse {
        val query = queryFactory.select(
                region.id,
                region.nameKr,
                region.nameEn,
                region.id.count(),
                region.parent.id,
            )
            .from(region)
            .groupBy(region.parent.id, region.id)

        // 하위 지역 카운트가 0인 지역은 제외
        if (existOnly) {
            query.having(region.id.count().gt(0L))
        }

        val queryResults = query.fetch().map {
            RegionFilterByRegion(
                regionId = it.get(region.id)!!,
                nameKr = it.get(region.nameKr)!!,
                nameEn = it.get(region.nameEn)!!,
                childrenCount = it.get(region.id.count())!!,
            )
        }
        return RegionFilterResponse(queryResults)
    }

    private fun filterByParents(list: List<Long>?): BooleanExpression? {
        return list?.let { Expressions.numberPath(Long::class.javaObjectType, "region.parent.id").`in`(list) }
    }

    private fun searchByName(name: String?): BooleanExpression? {
        return name?.let { region.nameKr.contains(it).or(region.nameEn.contains(it)) }
    }

    private fun sortByNameKr(order: Sort.Direction): OrderSpecifier<String> {
        return if (order == Sort.Direction.ASC) region.nameKr.asc() else region.nameKr.desc()
    }

    private fun sortByNameEn(order: Sort.Direction?): OrderSpecifier<String>? {
        return order?.let {  if (it == Sort.Direction.ASC) region.nameEn.asc() else region.nameEn.desc() }
    }

}
