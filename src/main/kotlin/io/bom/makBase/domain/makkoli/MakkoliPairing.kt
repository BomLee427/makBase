package io.bom.makBase.domain.makkoli

import io.bom.makBase.domain.Auditing
import jakarta.persistence.*

@Entity
class MakkoliPairing(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "makkoli_id")
    val makkoli: Makkoli,

    val pairing: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "makkoli_pairing_id")
    val id: Long? = null,

    ) : Auditing() {

    fun delete(): MakkoliPairing {
        super.softDelete()
        return this
    }
}
