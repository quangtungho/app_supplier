package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataGroupOrder

interface ShareMessage {
    fun onShare(group: DataGroupOrder?)
}