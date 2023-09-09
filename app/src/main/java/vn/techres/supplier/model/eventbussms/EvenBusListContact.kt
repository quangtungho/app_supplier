package vn.techres.supplier.model.eventbussms

import vn.techres.supplier.model.datamodel.DataContact


class EvenBusListContact(contactList: ArrayList<DataContact>, status: Int) {

    var contactList: ArrayList<DataContact> = contactList
    var status: Int = status

}