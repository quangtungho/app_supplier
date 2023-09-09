package vn.techres.supplier.model.params

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import vn.techres.supplier.model.request.BaseRequest
import vn.techres.supplier.model.request.CreateWareHouseRequest


@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class CreateWareHouseParams : BaseRequest() {
    @JsonField(name = ["params"])
    var params = CreateWareHouseRequest()
}
