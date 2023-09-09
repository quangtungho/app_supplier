package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataMaterialsReport : Serializable {
    @JsonField(name = ["total_import"])
    var total_import: BigDecimal = BigDecimal.ZERO

    @JsonField(name = ["total_export"])
    var total_export: BigDecimal = BigDecimal.ZERO

    @JsonField(name = ["total_remaining"])
    var total_remaining: BigDecimal = BigDecimal.ZERO

    @JsonField(name = ["list"])
    var list_material = ArrayList<DataReportMaterials>()

}