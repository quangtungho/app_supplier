package vn.techres.supplier.helper;

import org.jetbrains.annotations.NotNull;

public enum SocketChatEmitDataEnum {
    JOIN_ROOM {
        @NotNull
        @Override
        public String toString() {
            return "join-room-tms-supplier";
        }
    },
    LEAVE_ROOM {
        @NotNull
        @Override
        public String toString() {
            return "leave-room-tms-supplier";
        }
    },
    CHAT_TEXT {
        @NotNull
        @Override
        public String toString() {
            return "chat-text-tms-supplier";
        }
    },

    CHAT_IMAGE {
        @NotNull
        @Override
        public String toString() {
            return "chat-image-tms-supplier";
        }
    },
    CHAT_FILE {
        @NotNull
        @Override
        public String toString() {
            return "chat-file-tms-supplier";
        }
    },
    CHAT_VIDEO {
        @NotNull
        @Override
        public String toString() {
            return "chat-video-tms-supplier";
        }
    },
    USER_IS_TYPING {
        @NotNull
        @Override
        public String toString() {
            return "user-is-typing-tms-supplier";
        }
    },
    USER_IS_NOT_TYPING {
        @NotNull
        @Override
        public String toString() {
            return "user-is-not-typing-tms-supplier";
        }
    },
    CHAT_STICKER {
        @NotNull
        @Override
        public String toString() {
            return "chat-sticker-tms-supplier";
        }
    },
    CLIENT_CONNECTION {
        @NotNull
        @Override
        public String toString() {
            return "client-connection-tms-supplier";
        }
    },
    CLIENT_DISCONNECTION {
        @NotNull
        @Override
        public String toString() {
            return "client-disconnection-tms-supplier";
        }
    },
    REACTION_MESSAGE {
        @NotNull
        @Override
        public String toString() {
            return "reaction-message-tms-supplier";
        }
    },
    REVOKE_MESSAGE {
        @NotNull
        @Override
        public String toString() {
            return "revoke-message-tms-supplier";
        }
    },

    PINNED_MESSAGE {
        @NotNull
        @Override
        public String toString() {
            return "pinned-message-tms-supplier";
        }
    },

    CHAT_REPLY {
        @NotNull
        @Override
        public String toString() {
            return "chat-reply-tms-supplier";
        }
    },

    REVOKE_PINNED_MESSAGE {
        @NotNull
        @Override
        public String toString() {
            return "revoke-pinned-message-tms-supplier";
        }
    }, REMOVE_USER {
        @NotNull
        @Override
        public String toString() {
            return "remove-user-supplier";
        }
    }, REMOVE_GROUP {
        @NotNull
        @Override
        public String toString() {
            return "remove-group-tms-supplier";
        }
    }, CHAT_LINK {
        @NotNull
        @Override
        public String toString() {
            return "chat-link-tms-supplier";
        }
    }, NEW_GROUP_CREATED {
        @NotNull
        @Override
        public String toString() {
            return "new-group-created-tms-supplier";
        }
    }, ADD_NEW_USER {
        @NotNull
        @Override
        public String toString() {
            return "add-new-user-tms-supplier";
        }
    },
    CALL_CONNECT {
        @NotNull
        @Override
        public String toString() {
            return "call-connect-tms-supplier";
        }
    },
    CALL_CANCEL {
        @NotNull
        @Override
        public String toString() {
            return "call-cancel-tms-supplier";
        }
    }, CALL_REJECT {
        @NotNull
        @Override
        public String toString() {
            return "call-reject-tms-supplier";
        }
    }, CALL_ACCEPT {
        @NotNull
        @Override
        public String toString() {
            return "call-accept-tms-supplier";
        }
    }, CALL_COMPLETE {
        @NotNull
        @Override
        public String toString() {
            return "call-complete-tms-supplier";
        }
    },
    CALL_NO_ANSWER {
        @NotNull
        @Override
        public String toString() {
            return "call-no-answer-tms-supplier";
        }
    },
    CHANGE_CALL {
        @NotNull
        @Override
        public String toString() {
            return "change-call-tms-supplier";
        }
    },
    CHAT_CONTACT {
        @NotNull
        @Override
        public String toString() {
            return "chat-business-card-tms-supplier";
        }
    },

    CHAT_ORDER {
        @NotNull
        @Override
        public String toString() {
            return "chat-order-tms-supplier";
        }
    }


}
