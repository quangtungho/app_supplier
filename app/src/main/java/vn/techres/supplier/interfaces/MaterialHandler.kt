package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.MaterialManager

interface MaterialHandler {
    fun onClick(position: Int, id: Int, model: MaterialManager)
    fun inputMaterial(position: Int, model: MaterialManager, type: String)
}