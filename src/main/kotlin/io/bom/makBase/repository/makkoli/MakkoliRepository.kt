package io.bom.makBase.repository.makkoli

import io.bom.makBase.domain.makkoli.Makkoli
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MakkoliRepository : JpaRepository<Makkoli, Long> {
    @Query(
        """
        select distinct m from Makkoli m
        join fetch m.maker mk
        join fetch m.region r
        where m.id = :id
        and m.deletedAt is null
        and mk.deletedAt is null
        and r.deletedAt is null
    """
    )
    fun findByIdWithNoCollection(id: Long): Makkoli?

    @Query(
        """
        select distinct m from Makkoli m
        join fetch m.makkoliRiceList mr
        join fetch mr.rice r
        join fetch m.maker mk
        join fetch m.region rg
        where m.id = :id
        and m.deletedAt is null
        and mk.deletedAt is null
        and r.deletedAt is null
    """
    )
    fun findByIdWithCollection(id: Long): Makkoli?
}
