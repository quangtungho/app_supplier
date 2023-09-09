package vn.techres.supplier.interfaces

import vn.techres.supplier.model.datamodel.MaterialManager

interface OnClickTransIdAndModelMaterial {
    fun onClick(position: Int, id: Int, model: MaterialManager)
}