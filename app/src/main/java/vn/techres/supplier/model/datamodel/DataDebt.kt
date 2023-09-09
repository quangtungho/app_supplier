package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import java.io.Serializable
import java.math.BigDecimal

@JsonObject
class DataDebt : Serializable {
    @JsonField(name = ["total_receivable_debt_amount"])
    var total_receivable_debt_amount: BigDecimal = BigDecimal.ZERO

    @JsonField(name = ["total_to_pay_debt_amount"])
    var total_to_pay_debt_amount: BigDecimal = BigDecimal.ZERO

}