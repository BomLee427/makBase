package io.bom.makBase.domain.makkoli

import io.bom.makBase.domain.Auditing
import jakarta.persistence.*

@Entity
class MakkoliFlavor(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "makkoli_id")
    val makkoli: Makkoli,

    val flavor: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "makkoli_flavor_id")
    val id: Long? = null,

    ) : Auditing() {

    fun delete(): MakkoliFlavor {
        super.softDelete()
        return this
    }
}
