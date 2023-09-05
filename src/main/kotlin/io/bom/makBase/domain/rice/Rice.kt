package io.bom.makBase.domain.rice

import io.bom.makBase.domain.Auditing
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
