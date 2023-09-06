package io.bom.makBase.domain.rice

import io.bom.makBase.domain.Auditing
import io.bom.makBase.domain.Region
import jakarta.persistence.*

@Entity
class RiceShare(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rice_id")
    val rice: Rice,

    var share: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    var region: Region,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rice_share_id")
    val id: Long? = null,

    ) : Auditing()
