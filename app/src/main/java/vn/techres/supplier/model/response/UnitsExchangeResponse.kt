package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import vn.techres.supplier.model.datamodel.DataUnitsExchange

open class UnitsExchangeResponse: BaseResponse()  {
    ///GETTER
    ///SETTER
    @JsonField(name = ["data"])
    var data: ArrayList<DataUnitsExchange>? = null
}
