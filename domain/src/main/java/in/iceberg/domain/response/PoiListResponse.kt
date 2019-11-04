package `in`.iceberg.domain.response

import `in`.iceberg.domain.model.PoiData

data class PoiListResponse(
    val poiList: MutableList<PoiData>
)