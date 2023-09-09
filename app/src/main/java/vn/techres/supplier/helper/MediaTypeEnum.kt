package vn.techres.supplier.helper

/**
 * ================================================
 * Created by HuuThang on 17/5/2021 10:40
 * ================================================
 */
enum class MediaTypeEnum {
    TYPE_DOCX {
        override fun toString(): String {
            return "docx"
        }
    },
    TYPE_JPG {
        override fun toString(): String {
            return "jpg"
        }
    },
    TYPE_SVG {
        override fun toString(): String {
            return "svg"
        }
    },
    TYPE_JPEG {
        override fun toString(): String {
            return "jpeg"
        }
    },
    TYPE_PNG {
        override fun toString(): String {
            return "png"
        }
    },
    TYPE_AI {
        override fun toString(): String {
            return "ai"
        }
    },
    TYPE_DMG {
        override fun toString(): String {
            return "dmg"
        }
    },
    TYPE_XLSX {
        override fun toString(): String {
            return "xlsx"
        }
    },
    TYPE_HTML {
        override fun toString(): String {
            return "html"
        }
    },
    TYPE_MP3 {
        override fun toString(): String {
            return "mp3"
        }
    },
    TYPE_PDF {
        override fun toString(): String {
            return "pdf"
        }
    },
    TYPE_PPTX {
        override fun toString(): String {
            return "pptx"
        }
    },
    TYPE_PSD {
        override fun toString(): String {
            return "psd"
        }
    },
    TYPE_REC {
        override fun toString(): String {
            return "rec"
        }
    },
    TYPE_EXE {
        override fun toString(): String {
            return "exe"
        }
    },
    TYPE_SKETCH {
        override fun toString(): String {
            return "sketch"
        }
    },
    TYPE_TXT {
        override fun toString(): String {
            return "txt"
        }
    },
    TYPE_MP4 {
        override fun toString(): String {
            return "mp4"
        }
    },
    TYPE_XML {
        override fun toString(): String {
            return "xml"
        }
    },
    TYPE_ZIP {
        override fun toString(): String {
            return "zip"
        }
    }
}