package io.bom.makBase.service

import io.bom.makBase.domain.makkoli.Makkoli
import io.bom.makBase.domain.makkoli.MakkoliFlavor
import io.bom.makBase.domain.makkoli.MakkoliRice
import io.bom.makBase.domain.makkoli.MakkoliPairing
import io.bom.makBase.dto.makkoli.*
import io.bom.makBase.dto.makkoli.MakkoliSummaryResponse
import io.bom.makBase.repository.makkoli.condition.MakkoliFilter
import io.bom.makBase.repository.makkoli.condition.MakkoliSearch
import io.bom.makBase.exception.NotFoundException
import io.bom.makBase.repository.rice.RiceRepository
import io.bom.makBase.repository.region.RegionRepository
import io.bom.makBase.repository.makkoli.MakkoliFlavorRepository
import io.bom.makBase.repository.makkoli.MakkoliRiceRepository
import io.bom.makBase.repository.makkoli.MakkoliPairingRepository
import io.bom.makBase.repository.makkoli.MakkoliRepository
import io.bom.makBase.repository.makkoli.condition.MakkoliSort
import io.bom.makBase.repository.makkoli.customRepository.MakkoliCustomRepoImpl
import io.bom.makBase.repository.maker.MakerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MakkoliService(
    private val makkoliRepository: MakkoliRepository,
    private val makkoliCustomRepository: MakkoliCustomRepoImpl,
    private val makerRepository: MakerRepository,
    private val regionRepository: RegionRepository,
    private val makkoliFlavorRepository: MakkoliFlavorRepository,
    private val makkoliPairingRepository: MakkoliPairingRepository,
    private val makkoliRiceRepository: MakkoliRiceRepository,
    private val riceRepository: RiceRepository,
) {
    /**
     * 다수 조회
     */
    @Transactional(readOnly = true)
    fun getMakkoliList(
        page: PageRequest, makkoliFilter: MakkoliFilter, search: MakkoliSearch, sort: MakkoliSort
    ): List<MakkoliSummaryResponse> {
        return makkoliCustomRepository.searchMakkoliByCondition(page, makkoliFilter, search, sort)
    }

    /**
     * 단일 조회
     */
    @Transactional(readOnly = true)
    fun getMakkoli(id: Long): MakkoliDetailResponse {
        return makkoliRepository.findByIdWithCollection(id)?.let {
            MakkoliDetailResponse.fromEntity(it) } ?: throw NotFoundException("No Makkoli")
    }

    /**
     * 필터 조회
     */
    fun getMakkoliFilter(existOnly: Boolean): MakkoliFilterResponse {
        return makkoliCustomRepository.getMakkoliFilter(existOnly)
    }

    /**
     * 작성
     */
    fun createMakkoli(dto: MakkoliCreateRequest): Long {
        val makkoli = makkoliRepository.save(Makkoli(
            nameKr = dto.nameKr,
            nameEn = dto.nameEn,
            alcohol = dto.alcohol,
            aspartame = dto.aspartame,
            acidity = dto.acidity,
            sweetness = dto.sweetness,
            price = dto.price,
            quantityPerBottle = dto.quantityPerBottle,
            isLegallyTraditional = dto.isLegallyTraditional,
            style = dto.style,
            maker = makerRepository.findByIdAndDeletedAtNull(dto.makerId) ?: throw NotFoundException("No maker"),
            region = regionRepository.findByIdWithMaker(dto.regionId) ?: throw NotFoundException("No region"),
            )
        )
        makkoliFlavorRepository.saveAll(dto.flavorList.distinct().map { MakkoliFlavor(makkoli, it) })
        makkoliPairingRepository.saveAll(dto.pairingList.distinct().map { MakkoliPairing(makkoli, it) })
        makkoliRiceRepository.saveAll(riceRepository.findAllById(dto.riceList).map { MakkoliRice(makkoli, it) })

        return makkoli.id!!
    }

    /**
     * 수정
     */
    fun updateMakkoli(id: Long, dto: MakkoliUpdateRequest): MakkoliUpdateResponse {
        val makkoli = makkoliRepository.findByIdWithNoCollection(id) ?: throw NotFoundException("Makkoli Not Found")

        if (dto.isLegallyTraditional.isPresent) { makkoli.isLegallyTraditional = dto.isLegallyTraditional.get() }

        if (dto.nameKr.isPresent) { makkoli.nameKr = dto.nameKr.get() }

        if (dto.nameEn.isPresent) { makkoli.nameEn = dto.nameEn.get() }

        if (dto.alcohol.isPresent) { makkoli.alcohol = dto.alcohol.get() }

        if (dto.acidity.isPresent) { makkoli.acidity = dto.acidity.get() }

        if (dto.aspartame.isPresent) { makkoli.aspartame = dto.aspartame.get() }

        if (dto.sweetness.isPresent) { makkoli.sweetness = dto.sweetness.get() }

        if (dto.price.isPresent) { makkoli.price = dto.price.get() }

        if (dto.quantityPerBottle.isPresent) { makkoli.quantityPerBottle = dto.quantityPerBottle.get() }

        if (dto.style.isPresent) { makkoli.style = dto.style.get()}

        if (dto.makerId.isPresent) {
            makkoli.maker = makerRepository.findByIdWithRegion(dto.makerId.get())
                ?: throw NotFoundException("Not Found Maker")
        }

        if (dto.regionId.isPresent) {
            makkoli.region = regionRepository.findByIdWithMaker(dto.regionId.get())
                ?: throw NotFoundException("Not Found Region")
        }
        return MakkoliUpdateResponse.fromEntity(makkoli)
    }

    /**
     * 삭제
     */
    fun deleteMakkoli(id: Long): Long {
        return makkoliRepository.findByIdWithNoCollection(id)?.delete()?.id ?: throw NotFoundException("No Makkoli")
    }

    /**
     * 풍미 추가/삭제
     */
    fun updateMakkoliFlavor(id: Long, dto: MakkoliFlavorUpdateRequest): List<String> {
        val makkoli = makkoliRepository.findByIdOrNull(id) ?: throw NotFoundException("Makkoli Not Found")
        val flavorList = makkoliFlavorRepository.findAllByMakkoliId(id)

        // 추가된 풍미 저장
        makkoliFlavorRepository.saveAll(dto.flavorList.distinct().filter { it !in flavorList.map { a -> a.flavor } }
            .map { MakkoliFlavor(makkoli, it) })

        // 삭제된 풍미 삭제
        flavorList.filter { it.flavor !in dto.flavorList }.forEach { it.delete() }

        return makkoliFlavorRepository.findAllByMakkoliId(id).map{it.flavor}
    }

    /**
     * 페어링 추가/삭제
     */
    fun updateMakkoliPairing(id: Long, dto: MakkoliPairingUpdateRequest): List<String> {
        val makkoli = makkoliRepository.findByIdOrNull(id) ?: throw NotFoundException("Makkoli Not Found")
        val pairingList = makkoliPairingRepository.findAllByMakkoliId(id)

        // 추가된 페어링 저장
        makkoliPairingRepository.saveAll(dto.pairingList.distinct().filter { it !in pairingList.map { p -> p.pairing } }
            .map { MakkoliPairing(makkoli, it) })

        // 삭제된 페어링 삭제
        pairingList.filter { it.pairing !in dto.pairingList }.forEach { it.delete() }

        return makkoliPairingRepository.findAllByMakkoliId(id).map { it.pairing }
    }

    /**
     * 쌀 추가/삭제
     */
    fun updateMakkoliRice(id: Long, dto: MakkoliRiceUpdateRequest): List<Long> {
        val makkoli = makkoliRepository.findByIdOrNull(id) ?: throw NotFoundException("Makkoli Not Found")
        val makkoliRiceList = makkoliRiceRepository.findByMakkoliIdWithRice(id)

        val newRiceList =
            riceRepository.findAllRiceIdIn(dto.riceIdList.filter { it !in makkoliRiceList.map { g -> g.rice.id } })

        makkoliRiceRepository.saveAll(newRiceList.map { MakkoliRice(makkoli, it) })

        makkoliRiceList.filter { it.rice.id !in dto.riceIdList }.forEach { it.delete() }

        return makkoliRiceRepository.findByMakkoliIdWithRice(id).map { it.rice.id!! }
    }
}
