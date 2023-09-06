package io.bom.makBase.controller

import io.bom.makBase.dto.common.CommonResponse
import io.bom.makBase.dto.rice.*
import io.bom.makBase.repository.rice.condition.RiceFilter
import io.bom.makBase.repository.rice.condition.RiceSearch
import io.bom.makBase.repository.rice.condition.RiceSort
import io.bom.makBase.service.RiceService
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/rice")
class RiceController(
    private val riceService: RiceService
) {

    /**
     * 다수 조회
     */
    @GetMapping("")
    fun getRiceListV1(
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "100") size: Int,

        // 필터
        @RequestParam(value = "region", required = false) region: List<Long>?,

        // 검색
        @RequestParam(value = "name", required = false) name: String?,

        // 정렬
        @RequestParam(value = "sort", required = false) sortQuery: List<String>?,

        ): CommonResponse<List<RiceSummaryResponse>> {

        val filter = RiceFilter(region)
        val search = RiceSearch(name = name)
        val pageRequest = PageRequest.of(page, size)
        val sort = RiceSort.of(sortQuery)
        return CommonResponse.of(
            data = riceService.getRiceList(pageRequest, filter, search, sort))
    }

    /**
     * 단일 조회
     */
    @GetMapping("/{id}")
    fun getRiceV1(@PathVariable id: Long): CommonResponse<RiceDetailResponse> {
        return CommonResponse.of(data = riceService.getRice(id))
    }

    /**
     * 필터 조회
     */
    @GetMapping("/filter")
    fun getRiceFilterV1(
        @RequestParam(name = "existOnly", required = false, defaultValue = "true") existOnly: Boolean
    ): CommonResponse<RiceFilterResponse> {
        return CommonResponse.of(data = riceService.getRiceFilter(existOnly))
    }

    /**
     * 작성
     */
    @PostMapping("")
    fun createRiceV1(@RequestBody @Validated dto: RiceCreateRequest): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("riceId" to riceService.createRice(dto)))
    }

    /**
     * 수정
     */
    @PatchMapping("/{id}")
    fun updateRiceV1(
        @PathVariable id: Long, @RequestBody @Validated dto: RiceUpdateRequest
    ): CommonResponse<RiceUpdateResponse> {
        return CommonResponse.of(data = riceService.updateRice(id, dto))
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{id}")
    fun deleteRiceV1(@PathVariable id: Long): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("riceId" to riceService.deleteRice(id)))
    }
}
