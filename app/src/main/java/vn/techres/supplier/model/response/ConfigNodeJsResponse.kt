package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import vn.techres.supplier.model.entity.ConfigNodeJs

@JsonObject
class ConfigNodeJsResponse : BaseResponse() {
    @JsonField(name = ["data"])
    var data: ConfigNodeJs?= null
}