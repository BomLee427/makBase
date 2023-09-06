package io.bom.makBase.domain

import io.bom.makBase.domain.rice.RiceShare
import io.bom.makBase.domain.makkoli.Makkoli
import io.bom.makBase.dto.region.RegionSummaryResponse
import jakarta.persistence.*

@Entity
@Cacheable
class Region(

    @Column(name = "region_name_kr")
    var nameKr: String,

    @Column(name = "region_name_en")
    var nameEn: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_region_id", referencedColumnName = "region_id")
    var parent: Region? = null,

    @OneToMany(mappedBy = "parent")
    val children: MutableList<Region> = mutableListOf(),

    @OneToMany(mappedBy = "region")
    val riceShareList: MutableList<RiceShare> = mutableListOf(),

    @OneToMany(mappedBy = "region")
    val makerList: MutableList<Maker> = mutableListOf(),

    @OneToMany(mappedBy = "region")
    val makkoliList: MutableList<Makkoli> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    val id: Long? = null,

    ) : Auditing() {

    val parentName: String?
        get() = this.parent?.nameKr

    val topParentName: String
        get() {
            var result: String? = null
            var p = this.parent
            while (p != null) {
                result = p.nameKr
                p = p.parent
            }
            return result ?: this.nameKr
        }

    val allParentRegions: List<RegionSummaryResponse>
        get() {
            var _parent = if (this.parent != null) this.parent else return listOf()
            val _result = mutableListOf(_parent)
            while (_parent != null) {
                _result.add(parent)
                _parent = _parent.parent
            }
            return _result.filterNotNull().map { RegionSummaryResponse.fromEntity(it) }.distinct()
        }

    fun delete(): Region {
        super.softDelete()
        return this
    }
}
