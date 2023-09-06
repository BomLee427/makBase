package io.bom.makBase.domain

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditing(

    @CreatedDate
    open var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    open var updatedAt: LocalDateTime? = null,

    open var deletedAt: LocalDateTime? = null,

) {
    protected fun softDelete() {
        deletedAt = LocalDateTime.now()
    }

}
