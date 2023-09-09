package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.ListGroupOrder

interface ClickChatID {
    fun clickChatId(position: Int, dataChat: ArrayList<ListGroupOrder>)
}