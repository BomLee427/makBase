package io.bom.makBase.dto.makkoli

import io.bom.makBase.domain.makkoli.IsLegallyTraditional
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class MakkoliCreateRequest(

    @field:NotBlank
    val nameKr: String,

    @field:NotBlank
    val nameEn: String?,

    val alcohol: Double,

    @field:Size(min=1, max=5)
    val acidity: Int,

    @field:Size(min=1, max=5)
    val sweetness: Int,

    val aspartame: Double,

    val price: Int,
    val quantityPerBottle: Double,
    val style: String?,

    val isLegallyTraditional: IsLegallyTraditional,

    val makerId: Long,
    val regionId: Long,

    val flavorList: List<String>,

    val pairingList: List<String>,

    val riceList: List<Long>,
)
