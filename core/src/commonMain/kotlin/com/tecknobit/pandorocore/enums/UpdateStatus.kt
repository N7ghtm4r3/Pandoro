package com.tecknobit.pandorocore.enums

import kotlinx.serialization.Serializable

/**
 * `UpdateStatus` list of available statuses for an update
 */
@Serializable
enum class UpdateStatus {

    /**
     * `SCHEDULED` status for an update
     */
    SCHEDULED,

    /**
     * `IN_DEVELOPMENT` status for an update
     */
    IN_DEVELOPMENT,

    /**
     * `PUBLISHED` status for an update
     */
    PUBLISHED

}