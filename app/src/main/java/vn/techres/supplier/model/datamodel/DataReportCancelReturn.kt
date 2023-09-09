package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class DataReportCancelReturn : Serializable {
    ////GETTER
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("quantity")
    var quantity: Float = 0f

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

    @JsonProperty("total_amount")
    var total_amount: BigDecimal = BigDecimal.ZERO


}
