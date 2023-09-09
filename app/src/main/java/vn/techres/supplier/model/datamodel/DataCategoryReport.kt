package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataCategoryReport : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("name")
    var name: String? = ""

    @JsonProperty("code")
    var code: String? = ""

    @JsonProperty("normalize_name")
    var normalize_name: String? = ""

    @JsonProperty("prefix")
    var prefix: String? = ""

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("total_import")
    var total_import: Float? = 0f

    @JsonProperty("total_export")
    var total_export: Float? = 0f

    @JsonProperty("quantity_import")
    var quantity_import: Float? = 0f

    @JsonProperty("quantity_export")
    var quantity_export: Float? = 0f

    @JsonProperty("total_remaining")
    var total_remaining: Float? = 0f

    @JsonProperty("quantity_remaining")
    var quantity_remaining: Float? = 0f

    @JsonProperty("supplier_unit_specification_exchange_name")
    var supplier_unit_specification_exchange_name: String? = ""

    @JsonProperty("supplier_unit_name")
    var supplier_unit_name: String? = ""

}