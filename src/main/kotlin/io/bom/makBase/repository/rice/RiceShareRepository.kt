package io.bom.makBase.repository.rice

import io.bom.makBase.domain.rice.RiceShare
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RiceShareRepository : JpaRepository<RiceShare, Long> {
}
