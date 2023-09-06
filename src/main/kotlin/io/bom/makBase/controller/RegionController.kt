package io.bom.makBase.controller

import io.bom.makBase.dto.common.CommonResponse
import io.bom.makBase.dto.region.*
import io.bom.makBase.repository.region.condition.RegionFilter
import io.bom.makBase.repository.region.condition.RegionSearch
import io.bom.makBase.repository.region.condition.RegionSort
import io.bom.makBase.service.RegionService
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/region")
class RegionController(
    private val regionService: RegionService
) {

    /**
     * 다수 조회
     */
    @GetMapping("")
    fun getRegionListV1(
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "100") size: Int,

        // 검색
        @RequestParam(value = "parent", required = false) parent: List<Long>?,

        // 검색
        @RequestParam(value = "name", required = false) name: String?,

        // 정렬
        @RequestParam(value = "sort", required = false) sortQuery: List<String>?,

        ): CommonResponse<List<RegionSummaryResponse>> {
        val filter = RegionFilter(parent)
        val search = RegionSearch(name = name)
        val sort = RegionSort.of(sortQuery)
        val pageRequest = PageRequest.of(page, size)

        return CommonResponse.of(
            data = regionService.getRegionList(pageRequest, filter, search, sort))
    }

    /**
     * 필터 조회
     */
    @GetMapping("/filter")
    fun getRegionFilterV1(
        @RequestParam(name = "existOnly", required = false, defaultValue = "true") existOnly: Boolean
    ): CommonResponse<RegionFilterResponse> {
        return CommonResponse.of(data = regionService.getRegionFilter(existOnly))
    }

    /**
     * 단일 조회
     */
    @GetMapping("/{id}")
    fun getRegionV1(@PathVariable id: Long): CommonResponse<RegionDetailResponse> {
        return CommonResponse.of(data = regionService.getRegion(id))
    }

    /**
     * 작성
     */
    @PostMapping("")
    fun createRegionV1(@RequestBody @Validated dto: RegionCreateRequest): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("regionId" to regionService.createRegion(dto)))
    }

    /**
     * 수정
     */
    @PatchMapping("/{id}")
    fun updateRegionV1(
        @PathVariable id: Long, @RequestBody @Validated dto: RegionUpdateRequest): CommonResponse<RegionUpdateResponse> {
        return CommonResponse.of(data = regionService.updateRegion(id, dto))
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{id}")
    fun deleteRegionV1(@PathVariable id: Long): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("regionId" to regionService.deleteRegion(id)))
    }

    /**
     * 캐싱
     */
    @GetMapping("/cache")
    fun cacheRegionV1(): CommonResponse<Unit?> {
        return CommonResponse.of(data = regionService.cacheRegion())
    }
}