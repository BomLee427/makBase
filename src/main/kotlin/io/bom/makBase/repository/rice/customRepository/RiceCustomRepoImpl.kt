package io.bom.makBase.repository.rice.customRepository

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.ExpressionUtils.count
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import io.bom.makBase.domain.QRegion.region
import io.bom.makBase.domain.rice.QRice.rice
import io.bom.makBase.domain.rice.QRiceShare.riceShare
import io.bom.makBase.domain.makkoli.QMakkoli.makkoli
import io.bom.makBase.domain.makkoli.QMakkoliRice.makkoliRice
import io.bom.makBase.dto.rice.RiceDetailResponse
import io.bom.makBase.dto.rice.RiceFilterResponse
import io.bom.makBase.dto.rice.RiceFilterResponse.*
import io.bom.makBase.dto.rice.RiceSummaryResponse
import io.bom.makBase.dto.region.RegionSummaryResponse
import io.bom.makBase.exception.NotFoundException
import io.bom.makBase.repository.rice.condition.RiceFilter
import io.bom.makBase.repository.rice.condition.RiceSearch
import io.bom.makBase.repository.rice.condition.RiceSort
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class RiceCustomRepoImpl (
    private val queryFactory: JPAQueryFactory,
) : RiceCustomRepository {

    /**
     * 다수 조회
     */
    override fun searchRicesByCondition(
        page: Pageable, filter: RiceFilter, search: RiceSearch, sort: RiceSort): List<RiceSummaryResponse> {

        // 조건에 맞는 Rice 먼저 쿼리
        val riceQuery = queryFactory
            .select(
                Projections.constructor(
                    RiceSummaryResponse::class.java,
                    rice.id,
                    rice.nameKr,
                    rice.nameEn,
                ))
            .from(rice)
            .leftJoin(riceShare).on(riceShare.rice.id.eq(rice.id))
            .leftJoin(region).on(region.id.eq(riceShare.region.id))
            .where(
                rice.deletedAt.isNull,
                filterByRegion(filter.region),
                searchByName(search.name),
            )
            .distinct()

            // 추가 정렬 조건
            sort.sortMap["protein"]?.let { riceQuery.orderBy(sortByProtein(it)) }
            sort.sortMap["amylose"]?.let { riceQuery.orderBy(sortByAmylose(it)) }
            sort.sortMap["starchValue"]?.let { riceQuery.orderBy(sortByStarchValue(it)) }
            sort.sortMap["gelTemperature"]?.let { riceQuery.orderBy(sortByGelTemperature(it)) }
            sort.sortMap["nameKr"]?.let { riceQuery.orderBy(sortByNameKr(it)) }
            sort.sortMap["nameEn"]?.let { riceQuery.orderBy(sortByNameEn(it)) }

        val riceResults = riceQuery.orderBy(rice.id.asc())
            .limit(page.pageSize.toLong()).offset(page.offset).fetch()


        if (riceResults.size > 0) {
            // Rice에 해당하는 Region 쿼리
            val regionResults = queryFactory
                .select(
                    region.id,
                    region.nameKr,
                    region.nameEn,
                    riceShare.rice.id
                )
                .from(region)
                .innerJoin(riceShare).on(riceShare.region.id.eq(region.id))
                .where(
                    riceShare.rice.id.`in`(riceResults.map { it.riceId })
                ).fetch()

            // 각 Rice마다 Region 매핑
            for (q in riceResults) {
                q.regionList = regionResults.filter {it.get(riceShare.rice.id) == q.riceId}
                    .map {RegionSummaryResponse(
                        regionId = it.get(region.id)!!,
                        nameKr = it.get(region.nameKr)!!,
                        nameEn = it.get(region.nameEn)!!,)}
            } // for end
        } // if end

        return riceResults
    }

    /**
     * 필터 조회
     */
    override fun getRiceFilter(existOnly: Boolean): RiceFilterResponse {

        val riceCountSubquery = JPAExpressions.select(count(riceShare))
            .from(riceShare)
            .where(riceShare.region.eq(region))

        val query = queryFactory.select(
            Projections.constructor(
                RiceFilterByRegion::class.java,
                region.id,
                region.nameKr,
                region.nameEn,
                ExpressionUtils.`as`(riceCountSubquery, "riceCount"))
            )
            .from(region)
            .leftJoin(riceShare).on(riceShare.region.id.eq(region.id))
            .distinct()

        // 쌀 카운트가 0인 지역은 제외
        if (existOnly) {
            query.where(riceCountSubquery.gt(0L))
        }

        return RiceFilterResponse(query.fetch())
    }

    /**
     * 표현식 메서드
     */

    private fun filterByRegion(list: List<Long>?): BooleanExpression? {
        return list?.let { riceShare.region.id.`in`(list) }
    }

    private fun searchByName(name: String?): BooleanExpression? {
        return name?.let { rice.nameKr.contains(it).or(rice.nameEn.contains(it)) }
    }

    private fun sortByNameKr(order: Sort.Direction): OrderSpecifier<String> {
        return if (order == Sort.Direction.ASC) rice.nameKr.asc() else rice.nameKr.desc()
    }

    private fun sortByNameEn(order: Sort.Direction): OrderSpecifier<String> {
        return if (order == Sort.Direction.ASC) rice.nameEn.asc() else rice.nameEn.desc()
    }

    private fun sortByProtein(order: Sort.Direction?): OrderSpecifier<Double>? {
        return order?.let { if (it == Sort.Direction.ASC) rice.protein.asc() else rice.protein.desc() }
    }

    private fun sortByAmylose(order: Sort.Direction?): OrderSpecifier<Double>? {
        return order?.let { if (it == Sort.Direction.ASC) rice.amylose.asc() else rice.amylose.desc() }
    }

    private fun sortByStarchValue(order: Sort.Direction?): OrderSpecifier<Double>? {
        return order?.let { if (it == Sort.Direction.ASC) rice.starchValue.asc() else rice.starchValue.desc() }
    }

    private fun sortByGelTemperature(order: Sort.Direction?): OrderSpecifier<Double>? {
        return order?.let { if (it == Sort.Direction.ASC) rice.gelTemperature.asc() else rice.gelTemperature.desc() }
    }
}
