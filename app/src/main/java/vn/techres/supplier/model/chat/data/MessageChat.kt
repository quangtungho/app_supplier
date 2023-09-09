package vn.techres.supplier.model.chat.data

/**
 * ================================================
 * Created by HuuThang on 13/5/2021 10:40
 * ================================================
 */
class MessageChat {
    var member_id: Int = 0
    var app_name = "supplier"
    var group_id = ""
    var group_id_tms_supplier = ""
    var message_order = MessageOrder()
    var message_type = 0
    var code = ""
    var message = ""

    var list_tag_name: List<ChatTag> = ArrayList()
    var key_message_error = Math.random().toString()
    var files: List<FileChat> = ArrayList()
    var key_message = ""
    var sticker_id = ""
    var message_link = MessageLink()


    //chat
    constructor(
        member_id: Int,
        group_id: String,
        message: String,
        message_type: Int,
        list_tag_name: List<ChatTag>,
        message_link: MessageLink,
    ) {
        this.member_id = member_id
        this.group_id = group_id
        this.message = message
        this.message_type = message_type
        this.list_tag_name = list_tag_name
        this.message_link = message_link
    }

    //chatorder
    constructor(
        member_id: Int,
        group_id: String,
        group_id_tms_supplier: String,
        message: String,
        message_type: Int,
        list_tag_name: List<ChatTag>,
        message_link: MessageLink,
        code: String,
    ) {
        this.member_id = member_id
        this.group_id = group_id
        this.group_id_tms_supplier = group_id_tms_supplier
        this.message = message
        this.message_type = message_type
        this.list_tag_name = list_tag_name
        this.message_link = message_link
        this.code = code
    }

    //chat
    constructor() {}
    constructor(member_id: Int, group_id: String, message_type: Int, files: List<FileChat>) {
        this.member_id = member_id
        this.group_id = group_id
        this.message_type = message_type
        this.files = files
    }


    //chat
    constructor(
        member_id: Int,
        group_id: String,
        message_type: Int,
        files: List<FileChat>,
        key_message: String,
    ) {
        this.member_id = member_id
        this.group_id = group_id
        this.message_type = message_type
        this.files = files
        this.key_message = key_message
    }

    //chatorder
    constructor(
        code: String,
        member_id: Int,
        group_id: String,
        group_id_tms_supplier: String,
        message_type: Int,
        files: List<FileChat>,
        key_message: String,

        ) {
        this.code = code
        this.member_id = member_id
        this.group_id = group_id
        this.group_id_tms_supplier = group_id_tms_supplier
        this.message_type = message_type
        this.files = files
        this.key_message = key_message

    }

    //chat
    constructor(member_id: Int, group_id: String, message: String, message_type: Int) {
        this.member_id = member_id
        this.group_id = group_id
        this.message = message
        this.message_type = message_type
    }

    constructor(member_id: Int, group_id: String, message_type: Int, message: String) {
        this.member_id = member_id
        this.group_id = group_id
        this.message_type = message_type
        this.message = message
    }

    //chatorder
    constructor(
        member_id: Int,
        group_id: String,
        group_id_tms_supplier: String,
        message_type: Int,
        message: String,
        code: String,
    ) {
        this.member_id = member_id
        this.group_id = group_id
        this.group_id_tms_supplier = group_id_tms_supplier
        this.message_type = message_type
        this.message = message
        this.code = code
    }

    //chat
    constructor(
        member_id: Int,
        group_id: String,
        message: String,
        message_type: Int,
        list_tag_name: List<ChatTag>,
        files: List<FileChat>,
        message_link: MessageLink,
    ) {
        this.member_id = member_id
        this.group_id = group_id
        this.message = message
        this.message_type = message_type
        this.list_tag_name = list_tag_name
        this.files = files
        this.message_link = message_link
    }

    //chatorder
    constructor(
        member_id: Int,
        group_id: String,
        group_id_tms_supplier: String,
        message: String,
        message_type: Int,
        list_tag_name: List<ChatTag>,
        key_message_error: String,
        code: String,
    ) {
        this.member_id = member_id
        this.group_id = group_id
        this.group_id_tms_supplier = group_id_tms_supplier
        this.message = message
        this.message_type = message_type
        this.list_tag_name = list_tag_name
        this.key_message_error = key_message_error
        this.code = code
    }

}