package vn.techres.supplier.model.eventbussms;

public class EvenBusNewCall {
    private int message_type;

    public EvenBusNewCall(int message_type) {
        this.message_type = message_type;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }
}
