package io.bom.makBase.repository.makkoli

import io.bom.makBase.domain.makkoli.MakkoliPairing
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MakkoliPairingRepository : JpaRepository<MakkoliPairing, Long> {
    @Query("select mf from MakkoliPairing mf where mf.makkoli.id = :makkoliId and mf.deletedAt is null")
    fun findAllByMakkoliId(makkoliId: Long): List<MakkoliPairing>
}
