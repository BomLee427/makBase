package io.bom.makBase.domain.makkoli

import io.bom.makBase.domain.Auditing
import io.bom.makBase.domain.rice.Rice
import jakarta.persistence.*

@Entity
class MakkoliRice(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "makkoli_id")
    val makkoli: Makkoli,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rice_id")
    val rice: Rice,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "makkoli_rice_id")
    val id: Long? = null,

    ) : Auditing() {

    fun delete(): MakkoliRice {
        super.softDelete()
        return this
    }
}
