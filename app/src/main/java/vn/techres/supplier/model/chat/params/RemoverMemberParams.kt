package vn.techres.supplier.model.chat.params

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import vn.techres.supplier.model.chat.request.RemoveMemberRequest
import vn.techres.supplier.model.request.BaseRequest


@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class RemoverMemberParams : BaseRequest() {
    ///GETTER
    ///SETTER
    @JsonField(name = ["params"])
    var params = RemoveMemberRequest()
}
