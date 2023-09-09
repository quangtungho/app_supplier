package vn.techres.supplier.model.entity;


import java.util.Objects;

import vn.techres.supplier.activity.SupplierApplication;
import vn.techres.supplier.helper.SocketChatMessageTypeEnum;
import vn.techres.supplier.model.chat.data.MessageOrder;

public class MessageOrderEmit {
    private int member_id = Objects.requireNonNull(CurrentUser.INSTANCE.getCurrentUser(SupplierApplication.context)).getId();
    private String app_name = "supplier";
    private String group_id;
    private MessageOrder message_order;
    private String group_id_tms_supplier = "";
    private int message_type = Integer.parseInt(SocketChatMessageTypeEnum.ORDER.toString());

    private String os_name = "supplier_android";

    public String getOs_name() {
        return os_name;
    }

    public void setOs_name(String os_name) {
        this.os_name = os_name;
    }

    public MessageOrderEmit(String group_id, MessageOrder message_order) {
        this.group_id = group_id;
        this.message_order = message_order;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getGroup_id_tms_supplier() {
        return group_id_tms_supplier;
    }

    public void setGroup_id_tms_supplier(String group_id_tms_supplier) {
        this.group_id_tms_supplier = group_id_tms_supplier;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public MessageOrder getMessage_order() {
        return message_order;
    }

    public void setMessage_order(MessageOrder message_order) {
        this.message_order = message_order;
    }
}
