package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.DataGenusItems

interface ClickGensItemsID {
    fun clickDelete(position: Int, id: Int)
    fun clickEdit(position: Int, id: Int, model: DataGenusItems)
}