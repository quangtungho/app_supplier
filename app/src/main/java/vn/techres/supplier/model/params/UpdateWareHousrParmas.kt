package vn.techres.supplier.model.params

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import vn.techres.supplier.model.request.BaseRequest
import vn.techres.supplier.model.request.UpdateWareHouseRequest

/**
 * Created by Nhan on 26/08/2021.
 */
@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class UpdateWareHouseParams : BaseRequest() {
    @JsonField(name = ["params"])
    var params = UpdateWareHouseRequest()
}
