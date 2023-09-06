package io.bom.makBase.service

import io.bom.makBase.domain.Maker
import io.bom.makBase.dto.maker.*
import io.bom.makBase.exception.NotFoundException
import io.bom.makBase.repository.region.RegionRepository
import io.bom.makBase.repository.maker.MakerRepository
import io.bom.makBase.repository.maker.condition.MakerFilter
import io.bom.makBase.repository.maker.condition.MakerSearch
import io.bom.makBase.repository.maker.condition.MakerSort
import io.bom.makBase.repository.maker.customRepository.MakerCustomRepoImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MakerService(
    private val makerRepository: MakerRepository,
    private val makerCustomRepository: MakerCustomRepoImpl,
    private val regionRepository: RegionRepository,
) {
    /**
     * 다수 조회
     */
    fun getMakerList(
        page: PageRequest,
        filter: MakerFilter,
        search: MakerSearch,
        sort: MakerSort
    ): List<MakerSummaryResponse> {
        return makerCustomRepository.searchMakersByCondition(page, filter, search, sort)
    }

    /**
     * 단일 조회
     */
    fun getMaker(id: Long): MakerDetailResponse {
        return makerRepository.findByIdWithRegion(id)?.let {
            MakerDetailResponse.fromEntity(it) } ?: throw NotFoundException("No Maker")
    }

    /**
     * 필터 조회
     */
    fun getMakerFilter(existOnly: Boolean): MakerFilterResponse {
        return makerCustomRepository.getMakerFilter(existOnly)
    }

    /**
     * 작성
     */
    fun createMaker(dto: MakerCreateRequest): Long {
        val maker = Maker(
            nameKr = dto.nameKr,
            nameEn = dto.nameEn,
            region = regionRepository.findByIdWithMaker(dto.regionId) ?: throw NotFoundException("No Region"),
        )
        return makerRepository.save(maker).id!!
    }

    /**
     * 수정
     */
    fun updateMaker(id: Long, dto: MakerUpdateRequest): MakerUpdateResponse {
        val maker = makerRepository.findByIdWithRegion(id) ?: throw NotFoundException("Maker Not Found")

        if (dto.nameKr.isPresent) {
            maker.nameKr = dto.nameKr.get()
        }
        if (dto.nameEn.isPresent) {
            maker.nameEn = dto.nameEn.get()
        }
        if (dto.regionId.isPresent) {
            maker.region = regionRepository.findByIdOrNull(dto.regionId.get())
                ?: throw NotFoundException("Not Found Region")
        }
        return MakerUpdateResponse.fromEntity(maker)
    }

    /**
     * 삭제
     */
    fun deleteMaker(id: Long): Long {
        return makerRepository.findByIdWithRegion(id)?.delete()?.id ?: throw NotFoundException("No Maker")
    }

}
