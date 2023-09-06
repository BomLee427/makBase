package io.bom.makBase.repository.makkoli

import io.bom.makBase.domain.makkoli.MakkoliRice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MakkoliRiceRepository : JpaRepository<MakkoliRice, Long> {
    @Query(
        """
        select distinct mr from MakkoliRice mr 
        join fetch mr.rice r 
        where mr.makkoli.id = :id 
        and mr.deletedAt is null 
        and r.deletedAt is null
    """
    )
    fun findByMakkoliIdWithRice(id: Long): List<MakkoliRice>
}
