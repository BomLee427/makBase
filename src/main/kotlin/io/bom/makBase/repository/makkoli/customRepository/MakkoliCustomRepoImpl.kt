package io.bom.makBase.repository.makkoli.customRepository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import io.bom.makBase.domain.QRegion.region
import io.bom.makBase.domain.makkoli.QMakkoli.makkoli
import io.bom.makBase.domain.makkoli.IsLegallyTraditional
import io.bom.makBase.dto.makkoli.*
import io.bom.makBase.dto.makkoli.MakkoliSummaryResponse
import io.bom.makBase.dto.makkoli.MakkoliFilterResponse.*
import io.bom.makBase.repository.region.RegionRepository
import io.bom.makBase.repository.makkoli.condition.MakkoliFilter
import io.bom.makBase.repository.makkoli.condition.MakkoliSearch
import io.bom.makBase.repository.makkoli.condition.MakkoliSort
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class MakkoliCustomRepoImpl(
    private val queryFactory: JPAQueryFactory,
    private val regionRepository: RegionRepository,
) : MakkoliCustomRepository {

    /**
     * 다수 조회
     */
    override fun searchMakkoliByCondition(
        page: Pageable, filter: MakkoliFilter, search: MakkoliSearch, sort: MakkoliSort): List<MakkoliSummaryResponse> {

        val makkoliQuery = queryFactory
            .select(
                makkoli.id,
                makkoli.isLegallyTraditional,
                makkoli.nameKr,
                makkoli.nameEn,
                makkoli.region.id)
            .from(makkoli)
            .leftJoin(region).on(region.id.eq(makkoli.region.id))
            .where(
                makkoli.deletedAt.isNull,
                region.deletedAt.isNull,
                filterByType(filter.isLegallyTraditional),
                rangeByAlcohol(filter.alcoholMax, filter.alcoholMin),
                rangeByPrice(filter.priceMin, filter.priceMax),
                filterByStyle(filter.style),
                filterByRegion(filter.region),
                searchByName(search.name),
            )

            // 추가 정렬 조건
            sort.sortMap["nameKr"]?.let { makkoliQuery.orderBy(sortByNameKr(it)) }
            sort.sortMap["nameEn"]?.let { makkoliQuery.orderBy(sortByNameEn(it)) }
            sort.sortMap["alcohol"]?.let { makkoliQuery.orderBy(sortByAlcohol(it)) }
            sort.sortMap["acidity"]?.let { makkoliQuery.orderBy(sortByAcidity(it)) }
            sort.sortMap["sweetness"]?.let { makkoliQuery.orderBy(sortBySweetness(it)) }
            sort.sortMap["aspartame"]?.let { makkoliQuery.orderBy(sortByAspartame(it)) }
            sort.sortMap["price"]?.let { makkoliQuery.orderBy(sortByPrice(it)) }

        val queryResults = makkoliQuery.orderBy(makkoli.id.asc())
            .limit(page.pageSize.toLong()).offset(page.offset)
            .fetch()

        val regions = regionRepository.findByIdIn(queryResults.map { it.get(makkoli.region.id)!! })

        return queryResults.map { w ->
                MakkoliSummaryResponse(
                    makkoliId = w.get(makkoli.id)!!,
                    nameKr = w.get(makkoli.nameKr)!!,
                    nameEn = w.get(makkoli.nameEn)!!,
                    regionName = regions.find { r -> r.id == w.get(makkoli.region.id)!! }?.topParentName
                ) }
    }

    /**
     * 필터 조회
     */
    override fun getMakkoliFilter(existOnly: Boolean): MakkoliFilterResponse {

        val makkoliTraditionalQuery = queryFactory
            .select(
                Projections.constructor(
                    MakkoliFilterByTraditional::class.java,
                    makkoli.isLegallyTraditional,
                    makkoli.isLegallyTraditional.count().`as`("makkoliTraditionalCount"),
                )
            )
            .from(makkoli)
            .groupBy(makkoli.isLegallyTraditional)
        if (existOnly) {
            makkoliTraditionalQuery.having(makkoli.isLegallyTraditional.count().gt(0L),)
        }

        val styleQuery = queryFactory
            .select(
                Projections.constructor(
                    MakkoliFilterByStyle::class.java,
                    makkoli.style,
                    makkoli.style.count().`as`("makkoliStyleCount"),
            ))
            .from(makkoli)
            .groupBy(makkoli.style)
            .where(makkoli.style.isNotNull)
        if (existOnly) {
            styleQuery.having(makkoli.style.count().gt(0L),)
        }

        val regionQuery = queryFactory
            .select(
                Projections.constructor(
                    MakkoliFilterByRegion::class.java,
                makkoli.region.id,
                makkoli.region.nameKr,
                makkoli.region.nameEn,
                makkoli.region.id.count().`as`("makkoliRegionCount"),
            ))
            .from(makkoli)
            .groupBy(makkoli.region.id)
        if (existOnly) {
            regionQuery.having(makkoli.region.id.count().gt(0L),)
        }

        return MakkoliFilterResponse(
            isLegallyTraditional = makkoliTraditionalQuery.fetch(),
            style = styleQuery.fetch(),
            region = regionQuery.fetch(),
        )
    }


    /**
     * 표현식 메서드
     */

    private fun filterByType(list: List<IsLegallyTraditional>?): BooleanExpression? {
        return list?.let { makkoli.isLegallyTraditional.`in`(list) }
    }

    private fun rangeByAlcohol(min: Int?, max: Int?): BooleanExpression? {
        return if (min != null || max != null) { makkoli.alcohol.between(min, max) } else { null }
    }

    private fun rangeByPrice(min: Int?, max: Int?): BooleanExpression? {
        return if (min != null || max != null) { makkoli.price.between(min, max) } else { null }
    }

    private fun filterByStyle(list: List<String>?): BooleanExpression? {
        return list?.let { makkoli.style.`in`(list) }
    }

    private fun filterByRegion(list: List<Long>?): BooleanExpression? {
        return list?.let { makkoli.region.id.`in`(list) }
    }

    private fun searchByName(name: String?): BooleanExpression? {
        return name?.let { makkoli.nameKr.contains(it).or(makkoli.nameEn.contains(it)) }
    }

    private fun sortByNameKr(order: Sort.Direction): OrderSpecifier<String> {
        return if (order == Sort.Direction.ASC) makkoli.nameKr.asc() else makkoli.nameKr.desc()
    }

    private fun sortByNameEn(order: Sort.Direction?): OrderSpecifier<String>? {
        return order?.let {  if (it == Sort.Direction.ASC) makkoli.nameEn.asc() else makkoli.nameEn.desc() }
    }

    private fun sortByAlcohol(order: Sort.Direction?): OrderSpecifier<Int>? {
        return order?.let { if (it == Sort.Direction.ASC) makkoli.acidity.asc() else makkoli.acidity.desc() }
    }

    private fun sortByAcidity(order: Sort.Direction?): OrderSpecifier<Int>? {
        return order?.let { if (it == Sort.Direction.ASC) makkoli.acidity.asc() else makkoli.acidity.desc() }
    }

    private fun sortBySweetness(order: Sort.Direction?): OrderSpecifier<Int>? {
        return order?.let { if (it == Sort.Direction.ASC) makkoli.sweetness.asc() else makkoli.sweetness.desc() }
    }

    private fun sortByAspartame(order: Sort.Direction?): OrderSpecifier<Double>? {
        return order?.let { if (it == Sort.Direction.ASC) makkoli.aspartame.asc() else makkoli.aspartame.desc() }
    }

    private fun sortByPrice(order: Sort.Direction?): OrderSpecifier<Int>? {
        return order?.let { if (it == Sort.Direction.ASC) makkoli.price.asc() else makkoli.price.desc() }
    }
}
