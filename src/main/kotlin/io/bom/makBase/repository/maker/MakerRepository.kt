package io.bom.makBase.repository.maker

import io.bom.makBase.domain.Maker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MakerRepository : JpaRepository<Maker, Long> {
    @Query(
        """
        select distinct mk from Maker mk
        left join fetch mk.makkoliList ml
        join fetch mk.region r
        where mk.id = :id
        and mk.deletedAt is null
        and r.deletedAt is null
    """
    )
    fun findByIdWithRegion(id: Long): Maker?

    fun findByIdAndDeletedAtNull(id: Long): Maker?
}
