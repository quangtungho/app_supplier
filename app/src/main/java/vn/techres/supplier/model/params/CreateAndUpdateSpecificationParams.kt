package vn.techres.supplier.model.params

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import vn.techres.supplier.model.request.*


@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class CreateAndUpdateSpecificationParams : BaseRequest() {
    ///GETTER
    ///SETTER
    @JsonField(name = ["params"])
    var params = CreateAndUpdateSpecificationRequest()
}
