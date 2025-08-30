package com.tecknobit.pandorocore.enums.events

import kotlinx.serialization.Serializable

/**
 * The `UpdateEventType` enum represents the events can occur in an update lifecycle
 *
 * @since 1.2.0
 */
@Serializable
enum class UpdateEventType {

    /**
     * `SCHEDULED` this event occurs when an update has been scheduled
     */
    SCHEDULED,

    /**
     * `STARTED` this event occurs when an update has been started
     */
    STARTED,

    /**
     * `CHANGENOTE_ADDED` this event occurs when a change note has been added to an update
     */
    CHANGENOTE_ADDED,

    /**
     * `SCHEDULED` this event occurs when a change note of an update has been marked as done
     */
    CHANGENOTE_DONE,

    /**
     * `SCHEDULED` this event occurs when a change note of an update has been marked as to-do
     */
    CHANGENOTE_UNDONE,

    /**
     * `SCHEDULED` this event occurs when a change note of an update has been edited
     */
    CHANGENOTE_EDITED,

    /**
     * `SCHEDULED` this event occurs when a change note has been moved to other update
     */
    CHANGENOTE_MOVED_TO,

    /**
     * `SCHEDULED` this event occurs when a change note has been moved from an update
     */
    CHANGENOTE_MOVED_FROM,

    /**
     * `SCHEDULED` this event occurs when a change note has been removed from an update
     */
    CHANGENOTE_REMOVED,

    /**
     * `PUBLISHED` this event occurs when an update has been published
     */
    PUBLISHED

}