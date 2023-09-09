package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable
import java.math.BigDecimal


@JsonObject
class DataReceipts : Serializable {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("supplier_employee_create_id")
    var supplier_employee_create_id: Int = 0

    @JsonProperty("supplier_employee_create_full_name")
    var supplier_employee_create_full_name: String = ""

    @JsonProperty("code")
    var code: String = ""

    @JsonProperty("restaurant_name")
    var restaurant_name: String = ""

    @JsonProperty("supplier_employee_name")
    var supplier_employee_name: String = ""


    @JsonProperty("amount")
    var amount: BigDecimal = BigDecimal.ZERO

    @JsonProperty("type")
    var type: Int = 0

    @JsonProperty("note")
    var note: String = ""

    @JsonProperty("status")
    var status: Int = 0

    @JsonProperty("object_type")
    var object_type: Int = 0

    @JsonProperty("supplier_addition_fee_reason_id")
    var supplier_addition_fee_reason_id: Int = 0

    @JsonProperty("supplier_addition_fee_reason_name")
    var supplier_addition_fee_reason_name: String = ""

    @JsonProperty("fee_month")
    var fee_month: String = ""
}
