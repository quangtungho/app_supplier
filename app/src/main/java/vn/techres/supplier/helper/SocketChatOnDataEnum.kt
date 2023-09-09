package vn.techres.supplier.helper

import vn.techres.supplier.activity.SupplierApplication
import vn.techres.supplier.model.entity.CurrentUser.getCurrentUser

enum class SocketChatOnDataEnum {
    CHAT_TEXT {
        override fun toString(): String {
            return "res-chat-text-tms-supplier"
        }
    },


    CHAT_IMAGE {
        override fun toString(): String {
            return "res-chat-image-tms-supplier"
        }
    },
    CHAT_VIDEO {
        override fun toString(): String {
            return "res-chat-video-tms-supplier"
        }
    },
    USER_IS_TYPING {
        override fun toString(): String {
            return "res-user-is-typing-tms-supplier"
        }
    },
    USER_IS_NOT_TYPING {
        override fun toString(): String {
            return "res-user-is-not-typing-tms-supplier"
        }
    },
    CHAT_STICKER {
        override fun toString(): String {
            return "res-chat-sticker-tms-supplier"
        }
    },
    MESSAGE_NOT_SEEN_BY_ONE_GROUP {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "message-not-seen-by-one-group-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    MESSAGE_NOT_SEEN_BY_ONE_GROUP_ORDER {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "message-not-seen-by-one-group-order/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    MESSAGE_NOT_SEEN_BY_ALL_GROUP {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "message-not-seen-by-all-group-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    REVOKE_MESSAGE {
        override fun toString(): String {
            return "res-revoke-message-tms-supplier"
        }
    },

    REVOKE_MESSAGE_REPLY {
        override fun toString(): String {
            return "res-revoke-message-reply-tms-supplier"
        }
    },


    CHAT_REPLY {
        override fun toString(): String {
            return "res-chat-reply-tms-supplier"
        }
    },

    PINNED_MESSAGE_TEXT {
        override fun toString(): String {
            return "res-pinned-message-text-tms-supplier"
        }
    },

    PINNED_MESSAGE {
        override fun toString(): String {
            return "res-pinned-message-tms-supplier"
        }
    },

    REVOKE_PINNED_MESSAGE {
        override fun toString(): String {
            return "res-revoke-pinned-message-text-tms-supplier"
        }
    },

    RES_CHAT_ERROR {
        override fun toString(): String {
            return "res-chat-error-tms-supplier"
        }
    },
    RES_REMOVE_USER_OUT_ROOM {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-remove-user-out-room-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_REMOVE_GROUP {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-remove-group-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    CHAT_LINK {
        override fun toString(): String {
            return "res-chat-link-tms-supplier"
        }
    },
    CHAT_FILE {
        override fun toString(): String {
            return "res-chat-file-tms-supplier"
        }
    },
    NEW_GROUP_CREATE {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-new-group-created-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_ADD_NEW_USER {
        override fun toString(): String {
            return "res-add-new-user-tms-supplier"
        }
    },
    RES_REMOVE_USER {
        override fun toString(): String {
            return "res-remove-user-supplier"
        }
    },
    RES_REACTION_MESSAGE {
        override fun toString(): String {
            return "res-reaction-message-tms-supplier"
        }
    },
    RES_MESSAGE_VIEWED {
        override fun toString(): String {
            return "res-message-viewed-tms-supplier"
        }
    },
    RES_LIST_MESSAGE_VIEWED {
        override fun toString(): String {
            return "res-list-message-viewed-tms-supplier"
        }
    },
    RES_CALL_CONNECT {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-call-connect-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CHAT_VIDEO_CALL {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-chat-video-call-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CHAT_AUDIO_CALL {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-chat-audio-call-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CALL_CANCEL {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-call-cancel-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CALL_ACCEPT {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-call-accept-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CALL_REJECT {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-call-reject-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CALL_COMPLETE {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-call-complete-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CALL_NO_ANSWER {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-call-no-answer-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_ICE_CANDIDATE {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-ice-candidate-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CHANGE_CALL {
        override fun toString(): String {
            return String.format(
                "%s%s",
                "res-change-call-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_CHAT_BUSINESS_CARD {
        override fun toString(): String {
            return "res-chat-business-card-tms-supplier"
        }
    },
    RES_CHAT_ORDER {
        override fun toString(): String {
            return "res-chat-order-tms-supplier"
        }
    },
    RES_CALL_ACCEPT_TMS_SUPPLIER {
        override fun toString(): String {
            return java.lang.String.format(
                "%s%s",
                "res-call-accept-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },

    RES_CALL_REJECT_TMS_SUPPLIER {
        override fun toString(): String {
            return java.lang.String.format(
                "%s%s",
                "res-call-reject-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },

    RES_CHANGE_CALL_TMS_SUPPLIER {
        override fun toString(): String {
            return java.lang.String.format(
                "%s%s",
                "res-change-call-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },

    RES_ICE_CANDIDATE_TMS_SUPPLIER {
        override fun toString(): String {
            return java.lang.String.format(
                "%s%s",
                "res-ice-candidate-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_MAKE_ANSWER_TMS_SUPPLIER {
        override fun toString(): String {
            return java.lang.String.format(
                "%s%s",
                "res-make-answer-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_MAKE_OFFER_TMS_SUPPLIER {
        override fun toString(): String {
            return java.lang.String.format(
                "%s%s",
                "res-make-offer-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
    RES_BUSY_USER {
        override fun toString(): String {
            return java.lang.String.format(
                "%s%s",
                "res-busy-user-tms-supplier/",
                getCurrentUser(SupplierApplication.context)!!.id
            )
        }
    },
}