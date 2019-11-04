package `in`.iceberg.domain.model

data class PoiData (
    val id: Int = 0,
    val coordinate: Coordinate,
    val fleetType: String? = null,
    val heading: Double = 0.0
)