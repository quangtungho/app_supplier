package vn.techres.supplier.model.request

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import vn.techres.supplier.helper.AppConfig


@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class BaseRequest {
    ///GETTER
    ///SETTER
    @JsonField(name = ["project_id"])
    var project_id: Int = 0

    //get = 0
    //post = 1
    @JsonField(name = ["http_method"])
    var http_method: Int = 0

    //beta = 0
    //live = 1
    @JsonField(name = ["is_production_mode"])
    var is_production_mode: Int = AppConfig.getProductionModes()

    @JsonField(name = ["request_url"])
    var request_url: String? = ""
}
