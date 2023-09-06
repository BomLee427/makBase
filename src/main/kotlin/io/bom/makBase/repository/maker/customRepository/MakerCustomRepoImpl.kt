package io.bom.makBase.repository.maker.customRepository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import io.bom.makBase.domain.QMaker.maker
import io.bom.makBase.domain.QRegion.region
import io.bom.makBase.dto.maker.MakerFilterResponse
import io.bom.makBase.dto.maker.MakerFilterResponse.*
import io.bom.makBase.dto.maker.MakerSummaryResponse
import io.bom.makBase.repository.maker.condition.MakerFilter
import io.bom.makBase.repository.maker.condition.MakerSearch
import io.bom.makBase.repository.maker.condition.MakerSort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class MakerCustomRepoImpl(
    private val queryFactory: JPAQueryFactory
) : MakerCustomRepository {

    /**
     * 다수 조회
     */
    override fun searchMakersByCondition(
        page: PageRequest, filter: MakerFilter, search: MakerSearch, sort: MakerSort): List<MakerSummaryResponse> {
        val query = queryFactory.select(
            Projections.constructor(
                MakerSummaryResponse::class.java,
                maker.id!!,
                maker.nameKr,
                maker.nameEn,
                maker.region.nameKr,
            ))
            .from(maker)
            .leftJoin(region).on(region.id.eq(maker.region.id))
            .where(
                maker.deletedAt.isNull,
                filterByRegion(filter.region),
                searchByName(search.name),
            )

            sort.sortMap["nameKr"]?.let { query.orderBy(sortByNameKr(it)) }
            sort.sortMap["nameEn"]?.let { query.orderBy(sortByNameEn(it)) }

        return query.orderBy(maker.id.asc())
            .limit(page.pageSize.toLong()).offset(page.offset)
            .fetch()
    }

    /**
     * 필터 조회
     */
    override fun getMakerFilter(existOnly: Boolean): MakerFilterResponse {

        val query = queryFactory
            .select(
                Projections.constructor(
                    MakerFilterByRegion::class.java,
                    maker.region.id,
                    maker.region.nameKr,
                    maker.region.nameEn,
                    maker.region.id.count(),
                ))
            .from(maker)
            .groupBy(maker.region.id)

        if (existOnly) {
            query.having(maker.region.id.count().gt(0L))
        }

        return MakerFilterResponse(query.fetch())
    }

    /**
     * 표현식 메서드
     */
    private fun sortByNameKr(order: Sort.Direction): OrderSpecifier<String> {
        return if (order == Sort.Direction.ASC) maker.nameKr.asc() else maker.nameKr.desc()
    }

    private fun sortByNameEn(order: Sort.Direction?): OrderSpecifier<String>? {
        return order?.let {  if (it == Sort.Direction.ASC) maker.nameEn.asc() else maker.nameEn.desc() }
    }

    private fun searchByName(name: String?): BooleanExpression? {
        return name?.let { maker.nameKr.contains(it).or(maker.nameEn.contains(it)) }
    }

    private fun filterByRegion(list: List<Long>?): BooleanExpression? {
        return list?.let { Expressions.numberPath(Long::class.javaObjectType, "maker.region.id").`in`(list) }
    }

}
