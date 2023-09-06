package io.bom.makBase.repository.region

import io.bom.makBase.domain.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RegionRepository : JpaRepository<Region, Long> {
    @Query(
        """
        select distinct r from Region r
        left join fetch r.makerList mk
        where r.id = :id
        and r.deletedAt is null
    """
    )
    fun findByIdWithMaker(id: Long): Region?

    fun findByIdAndDeletedAtNull(id: Long): Region?

    fun findByDeletedAtNull(): List<Region>

    @Query("""
        select r from Region r
        where r.id in :ids 
        and r.deletedAt is null
    """)
    fun findByIdIn(ids: List<Long>): List<Region>
}
