package vn.techres.supplier.model.params

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import vn.techres.supplier.model.request.BaseRequest
import vn.techres.supplier.model.request.CreateStaffRequest
import vn.techres.supplier.model.request.EditProfileAccountRequest


@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class CreateStaffParams : BaseRequest() {
    ///GETTER
    ///SETTER
    @JsonField(name = ["params"])
    var params = CreateStaffRequest()
}
