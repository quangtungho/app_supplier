package vn.techres.supplier.model.response

import com.bluelinelabs.logansquare.LoganSquare
import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.IOException


@JsonObject
@JsonIgnoreProperties(ignoreUnknown = true)
open class BaseResponse {
    ///GETTER
    ///SETTER
    private var replyCode: Int = 0

    @JsonField(name = ["status"])
    var status: Int = 0

    @JsonField(name = ["message"])
    var message: String? = null

    @JsonField(name = ["data"])
    var dataResponse = ArrayList<String>()

    open fun getReplyCode(): Int {
        return replyCode
    }
    open fun isSuccess(): Boolean {
        return status == 200
    }
    override fun toString(): String {
        try {
            return "response: " + LoganSquare.serialize(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}
