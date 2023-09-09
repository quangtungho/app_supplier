package vn.techres.supplier.helper

enum class TechResEnumVideoCall {
    /**
     * Type
     * */
    TYPE_CALL_VIDEO {
        override fun toString(): String {
            return "TYPE_CALL_VIDEO"
        }
    },
    TYPE_CALL_PHONE {
        override fun toString(): String {
            return "TYPE_CALL_PHONE"
        }
    },
}