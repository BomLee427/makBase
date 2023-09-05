package io.bom.makBase.domain.makkoli

import io.bom.makBase.domain.*
import jakarta.persistence.*

@Entity
class Makkoli(

    @Column(name = "makkoli_name_kr")
    var nameKr: String,

    @Column(name = "makkoli_name_en")
    var nameEn: String?,

    var alcohol: Double,
    var acidity: Int,
    var sweetness: Int,
    var aspartame: Double,
    var price: Int,
    var quantityPerBottle: Double,
    var style: String?,

    @Enumerated(EnumType.STRING)
    var isLegallyTraditional: IsLegallyTraditional,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id")
    var maker: Maker,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    var region: Region,

    @OneToMany(mappedBy = "makkoli")
    val makkoliFlavorList: MutableList<MakkoliFlavor>? = null,

    @OneToMany(mappedBy = "makkoli")
    val makkoliPairingList: MutableList<MakkoliPairing>? = null,

    @OneToMany(mappedBy = "makkoli")
    val makkoliRiceList: MutableList<MakkoliRice>? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "makkoli_id")
    val id: Long? = null,

    ) : Auditing() {

    fun delete(): Makkoli {
        super.softDelete()
        return this
    }
}
