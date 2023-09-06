package io.bom.makBase.service

import io.bom.makBase.domain.rice.Rice
import io.bom.makBase.dto.rice.*
import io.bom.makBase.exception.NotFoundException
import io.bom.makBase.repository.rice.customRepository.RiceCustomRepoImpl
import io.bom.makBase.repository.rice.condition.RiceFilter
import io.bom.makBase.repository.rice.condition.RiceSearch
import io.bom.makBase.repository.rice.RiceRepository
import io.bom.makBase.repository.rice.condition.RiceSort
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RiceService(
    private val riceRepository: RiceRepository,
    private val riceCustomRepository: RiceCustomRepoImpl,
) {
    /**
     * 다수 조회
     */
    fun getRiceList(
        page: PageRequest, filter: RiceFilter, search: RiceSearch, sort: RiceSort
    ): List<RiceSummaryResponse> {
        return riceCustomRepository.searchRicesByCondition(page, filter, search, sort)
    }

    /**
     * 단일 조회
     */
    fun getRice(id: Long): RiceDetailResponse {
        val rice = riceRepository.findByIdAndDeletedAtNull(id) ?: throw NotFoundException("no rice")
        return RiceDetailResponse.fromEntity(rice)
    }

    /**
     * 필터 조회
     */
    fun getRiceFilter(existOnly: Boolean): RiceFilterResponse {
        return riceCustomRepository.getRiceFilter(existOnly)
    }

    /**
     * 작성
     */
    fun createRice(dto: RiceCreateRequest): Long {
        val rice = Rice(
            nameKr = dto.nameKr,
            nameEn = dto.nameEn,
            protein = dto.protein,
            amylose = dto.amylose,
            starchValue = dto.starchValue,
            gelTemperature = dto.gelTemperature,
        )
        return riceRepository.save(rice).id!!
    }

    /**
     * 수정
     */
    fun updateRice(id: Long, dto: RiceUpdateRequest): RiceUpdateResponse {
        val rice = riceRepository.findByIdAndDeletedAtNull(id) ?: throw NotFoundException("Rice Not Found")

        if (dto.nameKr.isPresent) {
            rice.nameKr = dto.nameKr.get()
        }
        if (dto.nameEn.isPresent) {
            rice.nameEn = dto.nameEn.get()
        }
        if (dto.protein.isPresent) {
            rice.starchValue = dto.protein.get()
        }
        if (dto.amylose.isPresent) {
            rice.amylose = dto.amylose.get()
        }
        if (dto.starchValue.isPresent) {
            rice.protein = dto.starchValue.get()
        }
        if (dto.gelTemperature.isPresent) {
            rice.gelTemperature = dto.gelTemperature.get()
        }

        return RiceUpdateResponse(
            riceId = rice.id!!,
            nameKr = rice.nameKr,
            nameEn = rice.nameEn,
            protein = rice.protein,
            amylose = rice.amylose,
            starchValue = rice.starchValue,
            gelTemperature = rice.gelTemperature,
        )
    }

    /**
     * 삭제
     */
    fun deleteRice(id: Long): Long {
        return riceRepository.findByIdAndDeletedAtNull(id)?.delete()?.id ?: throw NotFoundException("Rice Not Found")
    }

}
