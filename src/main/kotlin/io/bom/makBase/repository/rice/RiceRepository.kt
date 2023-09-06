package io.bom.makBase.repository.rice

import io.bom.makBase.domain.rice.Rice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RiceRepository : JpaRepository<Rice, Long> {

    // TODO: MultipleBagFetchException
    @Query("""
        select r from Rice r 
        join fetch r.regionList rs 
        join fetch rs.region rg 
        join fetch r.makkoliList mr 
        join fetch mr.makkoli m
        where r.deletedAt is null
    """)
    fun findByIdAndDeletedAtNull(id: Long): Rice?

    @Query("select r from Rice r where r.id in :ids")
    fun findAllRiceIdIn(ids: List<Long>): List<Rice>
}
