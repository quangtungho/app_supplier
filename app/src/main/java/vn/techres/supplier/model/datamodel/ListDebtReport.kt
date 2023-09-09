package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable

@JsonObject
class ListDebtReport : Serializable {
    @JsonField(name = ["id"])
    var id: Int = 0

    @JsonField(name = ["restaurant_id"])
    var restaurant_id: Int = 0

    @JsonField(name = ["name_restaurant"])
    var name_restaurant: String = ""

    @JsonField(name = ["name_contactor"])
    var name_contactor: String = ""

    @JsonField(name = ["debt_amount"])
    var debt_amount: String = ""

    @JsonField(name = ["phone"])
    var phone: String = ""

    @JsonField(name = ["address"])
    var address: String = ""

    @JsonField(name = ["email"])
    var email: String = ""

    @JsonField(name = ["info"])
    var info: String = ""

    @JsonField(name = ["tax_number"])
    var tax_number: String = ""

    @JsonField(name = ["debt"])
    var debt: Float = 0f

    @JsonField(name = ["waiting_payment"])
    var waiting_payment: Float = 0f

    @JsonField(name = ["done"])
    var done: Float = 0f

    @JsonField(name = ["sum_orders"])
    var sum_orders: Float = 0f

    @JsonField(name = ["quantity_orders"])
    var quantity_orders: Int = 0

    @JsonField(name = ["quantity_done"])
    var quantity_done: Int = 0

    @JsonField(name = ["quantity_waiting_payment"])
    var quantity_waiting_payment: Int = 0

    @JsonField(name = ["quantity_debt"])
    var quantity_debt: Int = 0
}