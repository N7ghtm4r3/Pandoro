package com.tecknobit.pandorocore.enums.events

import kotlinx.serialization.Serializable

// TODO: 26/08/2025 TO DOCU 1.2.0
@Serializable
enum class UpdateEventType {

    SCHEDULED,

    CHANGENOTE_ADDED,

    CHANGENOTE_DONE,

    CHANGENOTE_UNDONE,

    CHANGENOTE_EDITED,

    CHANGENOTE_REMOVED,

    PUBLISHED

}