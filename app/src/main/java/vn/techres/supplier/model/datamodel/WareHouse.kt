package vn.techres.supplier.model

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class WareHouse : Serializable {
    @JsonProperty("id")
    var id: Int? = 0

    @JsonProperty("number")
    var number: Int? = 0

    @JsonProperty("code")
    var code: String? = ""

    @JsonProperty("amount")
    var amount: Double = 0.0

    @JsonProperty("type")
    var type: Int? = 0

    @JsonProperty("status")
    var status: Int? = 0

    @JsonProperty("supplier_id")
    var supplier_id: Int? = 0

    @JsonProperty("supplier_employee_id")
    var supplier_employee_id: Int? = 0

    @JsonProperty("supplier_employee_name")
    var supplier_employee_name: String? = ""

    @JsonProperty("supplier_order_id")
    var supplier_order_id: Int? = 0

    @JsonProperty("source_of_supply_name")
    var source_of_supply_name: String = ""

    @JsonProperty("discount_amount")
    var discount_amount: Double = 0.0

    @JsonProperty("discount_percent")
    var discount_percent: Float = 0f

    @JsonProperty("discount_type")
    var discount_type: Int? = 0

    @JsonProperty("vat_amount")
    var vat_amount: Double = 0.0

    @JsonProperty("total_amount")
    var total_amount: Double = 0.0

    @JsonProperty("total_material")
    var total_material: Int? = 0

    @JsonProperty("payment_status")
    var payment_status: Int? = 0

    @JsonProperty("payment_date")
    var payment_date: String? = ""

    @JsonProperty("delivery_date")
    var delivery_date: String? = ""

    @JsonProperty("vat")
    var vat: Float? = 0f

    @JsonProperty("note")
    var note: String? = ""

    @JsonProperty("created_at")
    var created_at: String? = ""

    @JsonProperty("update_at")
    var update_at: String? = ""


}