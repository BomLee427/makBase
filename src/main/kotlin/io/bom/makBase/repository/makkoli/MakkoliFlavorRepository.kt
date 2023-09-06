package io.bom.makBase.repository.makkoli

import io.bom.makBase.domain.makkoli.MakkoliFlavor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MakkoliFlavorRepository : JpaRepository<MakkoliFlavor, Long> {
    @Query("select mf from MakkoliFlavor mf where mf.makkoli.id = :makkoliId and mf.deletedAt is null")
    fun findAllByMakkoliId(makkoliId: Long): List<MakkoliFlavor>
}
