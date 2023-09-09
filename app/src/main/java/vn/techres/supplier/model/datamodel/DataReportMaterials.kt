package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataReportMaterials : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int = 0

    @JsonProperty("material_category_id")
    var material_category_id: Int = 0

    @JsonProperty("material_category_name")
    var material_category_name: String = ""

    @JsonProperty("supplier_material_id")
    var supplier_material_id: Int = 0

    @JsonProperty("supplier_material_name")
    var supplier_material_name: String = ""

    @JsonProperty("supplier_material_code")
    var supplier_material_code: String = ""

    @JsonProperty("unit_full_name")
    var unit_full_name: String = ""

    @JsonProperty("total_import")
    var total_import: BigDecimal = BigDecimal.ZERO

    @JsonProperty("total_export")
    var total_export: BigDecimal = BigDecimal.ZERO

    @JsonProperty("quantity_import")
    var quantity_import: Float = 0f

    @JsonProperty("quantity_export")
    var quantity_export: Float = 0f

    @JsonProperty("total_remaining")
    var total_remaining: BigDecimal = BigDecimal.ZERO

    @JsonProperty("quantity_remaining")
    var quantity_remaining: Float = 0f
}