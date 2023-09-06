package io.bom.makBase.controller

import io.bom.makBase.domain.makkoli.IsLegallyTraditional
import io.bom.makBase.dto.common.CommonResponse
import io.bom.makBase.dto.makkoli.*
import io.bom.makBase.dto.makkoli.MakkoliSummaryResponse
import io.bom.makBase.repository.makkoli.condition.MakkoliFilter
import io.bom.makBase.repository.makkoli.condition.MakkoliSearch
import io.bom.makBase.repository.makkoli.condition.MakkoliSort
import io.bom.makBase.service.MakkoliService
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/makkoli")
class MakkoliController(
    private val makkoliService: MakkoliService,

    ) {

    /**
     * 다수 조회
     */
    @GetMapping("")
    fun getMakkoliListV1(
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "100") size: Int,

        // 필터링
        @RequestParam(value = "type", required = false) makkoliType: List<String>?,
        @RequestParam(value = "alcoholMin", required = false) alcoholMin: Int?,
        @RequestParam(value = "alcoholMax", required = false) alcoholMax: Int?,
        @RequestParam(value = "priceMin", required = false) priceMin: Int?,
        @RequestParam(value = "priceMax", required = false) priceMax: Int?,
        @RequestParam(value = "style", required = false) style: List<String>?,
        @RequestParam(value = "grade", required = false) grade: List<String>?,
        @RequestParam(value = "region", required = false) region: List<Long>?,

        // 검색
        @RequestParam(value = "name", required = false) name: String?,

        // 정렬
        @RequestParam(value = "sort", required = false) sortQuery: List<String>?,

        ): CommonResponse<List<MakkoliSummaryResponse>> {

        val filter = MakkoliFilter(isLegallyTraditional = makkoliType?.map{ IsLegallyTraditional.valueOf(it) },
            alcoholMin = alcoholMin, alcoholMax = alcoholMax,
            priceMin = priceMin, priceMax = priceMax,
            style = style,
            region = region,
        )
        val search = MakkoliSearch(name = name)
        val pageRequest = PageRequest.of(page, size)
        val sort = MakkoliSort.of(sortQuery)
        return CommonResponse.of(
            data = makkoliService.getMakkoliList(pageRequest, filter, search, sort))
    }

    /**
     * 단일 조회
     */
    @GetMapping("/{id}")
    fun getMakkoliV1(@PathVariable id: Long): CommonResponse<MakkoliDetailResponse> {
        return CommonResponse.of(data = makkoliService.getMakkoli(id))
    }

    /**
     * 필터 조회
     */
    @GetMapping("/filter")
    fun getMakkoliFilterV1(
        @RequestParam(name = "existOnly", required = false, defaultValue = "true") existOnly: Boolean
    ): CommonResponse<MakkoliFilterResponse> {
        return CommonResponse.of(data = makkoliService.getMakkoliFilter(existOnly))
    }

    /**
     * 작성
     */
    @PostMapping("")
    fun createMakkoliV1(@RequestBody @Validated dto: MakkoliCreateRequest): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("makkoliId" to makkoliService.createMakkoli(dto)))
    }

    /**
     * 수정
     */
    @PatchMapping("/{id}")
    fun updateMakkoliV1(
        @PathVariable id: Long, @RequestBody @Validated dto: MakkoliUpdateRequest
    ): CommonResponse<MakkoliUpdateResponse> {
        return CommonResponse.of(data = makkoliService.updateMakkoli(id, dto))
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{id}")
    fun deleteMakkoliV1(@PathVariable id: Long): CommonResponse<Map<String, Long>> {
        return CommonResponse.of(data = mapOf("makkoliId" to makkoliService.deleteMakkoli(id)))
    }

    /**
     * 막걸리 풍미 추가/삭제
     */
    @PatchMapping("/flavor/{id}")
    fun updateMakkoliFlavorV1(
        @PathVariable id: Long, @RequestBody @Validated flavorList: MakkoliFlavorUpdateRequest): CommonResponse<List<String>> {
        return CommonResponse.of(data = makkoliService.updateMakkoliFlavor(id, flavorList))
    }

    /**
     * 막걸리 페어링 추가/삭제
     */
    @PatchMapping("/pairing/{id}")
    fun updateMakkoliPairingV1(
        @PathVariable id: Long, @RequestBody @Validated pairingList: MakkoliPairingUpdateRequest): CommonResponse<List<String>> {
        return CommonResponse.of(data = makkoliService.updateMakkoliPairing(id, pairingList))
    }

    /**
     * 막걸리 쌀 추가/삭제
     */
    @PatchMapping("/rice/{id}")
    fun updateMakkoliRiceV1(
        @PathVariable id: Long, @RequestBody riceList: MakkoliRiceUpdateRequest): CommonResponse<List<Long>> {
        return CommonResponse.of(data = makkoliService.updateMakkoliRice(id, riceList))
    }
}