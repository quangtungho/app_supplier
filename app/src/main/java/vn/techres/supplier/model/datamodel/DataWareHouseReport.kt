package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
//import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataWareHouseReport : Serializable {

    // sl kho
    @JsonProperty("total_inventory_first")
    var total_inventory_first: Float? = 0f

    @JsonProperty("total_import")
    var total_import: Float? = 0f

    @JsonProperty("total_export")
    var total_export: Float? = 0f

    @JsonProperty("total_inventory_now")
    var total_inventory_now: Float? = 0f

    @JsonProperty("total_cancel")
    var total_cancel: Float? = 0f


    //tiên kho
    @JsonProperty("total_inventory_first_amount")
    var total_inventory_first_amount:  BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_import_amount")
    var total_import_amount:  BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_export_amount")
    var total_export_amount:  BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_inventory_now_amount")
    var total_inventory_now_amount: BigDecimal? = BigDecimal.ZERO

    @JsonProperty("total_cancel_amount")
    var total_cancel_amount:  BigDecimal? = BigDecimal.ZERO


}