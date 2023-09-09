package vn.techres.supplier.model.params

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import vn.techres.supplier.model.entity.Base
import vn.techres.supplier.model.request.BaseRequest


@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class BaseParams : BaseRequest() {
    ///GETTER
    ///SETTER
    @JsonField(name = ["params"])
    var params = Base.create()
}
