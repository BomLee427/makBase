package io.bom.makBase.domain

import io.bom.makBase.domain.makkoli.Makkoli
import jakarta.persistence.*

@Entity
class Maker(

    @Column(name = "maker_name_kr")
    var nameKr: String,

    @Column(name = "maker_name_en")
    var nameEn: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    var region: Region,

    @OneToMany(mappedBy = "maker")
    val makkoliList: MutableList<Makkoli>? = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maker_id")
    val id: Long? = null,

    ) : Auditing() {

    fun delete(): Maker {
        super.softDelete()
        return this
    }

}
