package io.bom.makBase.dto.rice

data class RiceUpdateResponse(
    val riceId: Long,
    val nameKr: String,
    val nameEn: String?,
    val protein: Double,
    val amylose: Double,
    val starchValue: Double,
    val gelTemperature: Double,
)
