package vn.techres.supplier.model.datamodel

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonObject
class DataChatNotSeenGroup : Serializable {
    @JsonProperty("message_not_seen_group")
    var message_not_seen_group: Int? = 0

    @JsonProperty("message_not_seen_all")
    var message_not_seen_all: Int? = 0


}