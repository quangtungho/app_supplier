package vn.techres.supplier.helper

enum class SocketChatMessageTypeEnum {
    TEXT {
        override fun toString(): String {
            return "1"
        }
    },
    IMAGE {
        override fun toString(): String {
            return "2"
        }
    },
    FILE {
        override fun toString(): String {
            return "3"
        }
    },
    STICKER {
        override fun toString(): String {
            return "4"
        }
    },
    VIDEO {
        override fun toString(): String {
            return "5"
        }
    },
    AUDIO {
        override fun toString(): String {
            return "6"
        }
    },
    BACKGROUND {
        override fun toString(): String {
            return "background"
        }
    },
    AVATAR {
        override fun toString(): String {
            return "avatar"
        }
    },
    GROUP_NAME {
        override fun toString(): String {
            return "groupName"
        }
    },
    KICK {
        override fun toString(): String {
            return "19"
        }
    },
    LEAVE {
        override fun toString(): String {
            return "member_leave_group"
        }
    },
    ADD {
        override fun toString(): String {
            return "17"
        }
    },
    PINNED {
        override fun toString(): String {
            return "13"
        }
    },
    AUTHORIZE {
        override fun toString(): String {
            return "authorize"
        }
    },
    REPLY {
        override fun toString(): String {
            return "7"
        }
    },
    CHAT_ORDER {
        override fun toString(): String {
            return "25"
        }
    },
    LINK {
        override fun toString(): String {
            return "8"
        }
    },
    REVOKE {
        override fun toString(): String {
            return "9"
        }
    },
    REVOKE_REPLY {
        override fun toString(): String {
            return "10"
        }
    },
    REMOVE_PINNED {
        override fun toString(): String {
            return "11"
        }
    },
    REACTION {
        override fun toString(): String {
            return "reaction"
        }
    },
    SHARE {
        override fun toString(): String {
            return "12"
        }
    },
    MEDIA {
        override fun toString(): String {
            return "media"
        }
    },
    ERROR {
        override fun toString(): String {
            return "error"
        }
    },
    CREATE_GROUP {
        override fun toString(): String {
            return "create_group"
        }
    },
    CALL_AUDIO {
        override fun toString(): String {
            return "22"
        }
    },
    CALL_VIDEO {
        override fun toString(): String {
            return "21"
        }
    },
    CONTACT {
        override fun toString(): String {
            return "23"
        }
    },
    ORDER {
        override fun toString(): String {
            return "25"
        }
    }
}