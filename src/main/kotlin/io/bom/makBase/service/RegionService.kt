package io.bom.makBase.service

import io.bom.makBase.domain.Region
import io.bom.makBase.dto.region.*
import io.bom.makBase.exception.NotFoundException
import io.bom.makBase.repository.region.RegionCustomRepoImpl
import io.bom.makBase.repository.region.RegionRepository
import io.bom.makBase.repository.region.condition.RegionFilter
import io.bom.makBase.repository.region.condition.RegionSearch
import io.bom.makBase.repository.region.condition.RegionSort
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RegionService(
    private val regionRepository: RegionRepository,
    private val regionCustomRepository: RegionCustomRepoImpl,
) {

    /**
     * 캐싱
     */
    fun cacheRegion() {
        regionRepository.findByDeletedAtNull()
    }

    /**
     * 다수 조회
     */
    fun getRegionList(
        page: PageRequest,
        filter: RegionFilter,
        search: RegionSearch,
        sort: RegionSort
    ): List<RegionSummaryResponse> {
        return regionCustomRepository.searchRegionsByCondition(page, filter, search, sort)
    }

    /**
     * 단일 조회
     */
    fun getRegion(id: Long): RegionDetailResponse {
        val region = regionRepository.findByIdWithMaker(id) ?: throw NotFoundException("no region")

        return RegionDetailResponse.fromEntity(region)
    }

    /**
     * 필터 조회
     */
    fun getRegionFilter(existOnly: Boolean): RegionFilterResponse {
        return RegionFilterResponse.fromEntity(regionRepository.findByDeletedAtNull(), existOnly)
    }

    fun createRegion(dto: RegionCreateRequest): Long {
        val region = Region(
            nameKr = dto.nameKr,
            nameEn = dto.nameEn,
            parent = dto.parentId?.let {
                regionRepository.findByIdAndDeletedAtNull(dto.parentId) ?: throw NotFoundException("No Parent Region")
            },
        )
        return regionRepository.save(region).id!!
    }

    fun updateRegion(id: Long, dto: RegionUpdateRequest): RegionUpdateResponse {
        val region = regionRepository.findByIdAndDeletedAtNull(id) ?: throw NotFoundException("No Region")

        if (dto.nameKr.isPresent) {
            region.nameKr = dto.nameKr.get()
        }
        if (dto.nameEn.isPresent) {
            region.nameEn = dto.nameEn.get()
        }
        if (dto.parentId.isPresent) {
            region.parent = dto.parentId.get()?.let { regionRepository.findByIdWithMaker(dto.parentId.get()!!) }
                ?: throw NotFoundException("No Parent Region")
        }
        return RegionUpdateResponse.fromEntity(region)
    }

    fun deleteRegion(id: Long): Long {
        return regionRepository.findByIdAndDeletedAtNull(id)?.delete()?.id ?: throw NotFoundException("No Region")
    }

}
