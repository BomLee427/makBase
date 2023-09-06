package io.bom.makBase.controller

import io.bom.makBase.dto.common.CommonResponse
import io.bom.makBase.dto.maker.*
import io.bom.makBase.repository.maker.condition.MakerFilter
import io.bom.makBase.repository.maker.condition.MakerSearch
import io.bom.makBase.repository.maker.condition.MakerSort
import io.bom.makBase.service.MakerService
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/maker")
class MakerController(
    private val makerService: MakerService
) {

    /**
     * 다수 조회
     */
    @GetMapping("")
    fun getMakerListV1(
        // 페이징
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "100") size: Int,

        // 필터링
        @RequestParam(value = "region", required = false) region: List<Long>?,

        // 검색
        @RequestParam(value = "name", required = false) name: String?,

        // 정렬
        @RequestParam(value = "sort", required = false) sortQuery: List<String>?,
        ): CommonResponse<List<MakerSummaryResponse>> {

        val filter = MakerFilter(region = region)
        val search = MakerSearch(name = name)
        val pageRequest = PageRequest.of(page, size)
        val sort = MakerSort.of(sortQuery)

        return CommonResponse.of(
            data = makerService.getMakerList(pageRequest, filter, search, sort))
    }

    /**
     * 필터 조회
     */
    @GetMapping("/filter")
    fun getMakerFilterV1(
        @RequestParam(name = "existOnly", required = false, defaultValue = "true") existOnly: Boolean
    ): CommonResponse<MakerFilterResponse> {
        return CommonResponse.of(data = makerService.getMakerFilter(existOnly))
    }

    /**
     * 단일 조회
     */
    @GetMapping("/{id}")
    fun getMakerV1(@PathVariable id: Long): CommonResponse<MakerDetailResponse> {
        return CommonResponse.of(data = makerService.getMaker(id))
    }

    /**
     * 작성
     */
    @PostMapping("")
    fun createMakerV1(@RequestBody @Validated dto: MakerCreateRequest): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("makerId" to makerService.createMaker(dto)))
    }

    /**
     * 수정
     */
    @PatchMapping("/{id}")
    fun updateMakerV1(
        @PathVariable id: Long, @RequestBody @Validated dto: MakerUpdateRequest): CommonResponse<MakerUpdateResponse> {
        return CommonResponse.of(data = makerService.updateMaker(id, dto))
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{id}")
    fun deleteMakerV1(@PathVariable id: Long): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("makerId" to makerService.deleteMaker(id)))
    }
}