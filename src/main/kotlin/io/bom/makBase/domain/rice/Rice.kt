package io.bom.makBase.domain.rice

import io.bom.makBase.domain.Auditing
import io.bom.makBase.domain.Region
import io.bom.makBase.domain.makkoli.Makkoli
import io.bom.makBase.domain.makkoli.MakkoliRice
import jakarta.persistence.*

@Entity
class Rice(

    @Column(name = "rice_name_kr")
    var nameKr: String,

    @Column(name = "rice_name_en")
    var nameEn: String?,

    var protein: Double,
    var amylose: Double,
    var starchValue: Double,
    var gelTemperature: Double,

    @OneToMany(mappedBy = "rice")
    var regionList: MutableList<RiceShare> = mutableListOf(),

    @OneToMany(mappedBy = "rice")
    val makkoliList: MutableList<MakkoliRice> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rice_id")
    val id: Long? = null,

    ) : Auditing() {

    fun delete(): Rice {
        super.softDelete()
        return this
    }
}
