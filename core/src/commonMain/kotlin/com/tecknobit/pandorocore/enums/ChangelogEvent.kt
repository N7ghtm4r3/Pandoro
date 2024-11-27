package com.tecknobit.pandorocore.enums

/**
 * `ChangelogEvent` list of available event types
 *
 * @param event:`changelogEvent` type
 */
enum class ChangelogEvent(
    val event: String,
) {

    /**
     * `INVITED_GROUP` type of the changelog event when the user have been invited to join in a group
     */
    INVITED_GROUP("Invited into a group"),

    /**
     * `JOINED_GROUP` type of the changelog event when the user joins in a group
     */
    JOINED_GROUP("Joined in a group"),

    /**
     * `ROLE_CHANGED` type of the changelog event when the role of the user in a group has been changed
     */
    ROLE_CHANGED("Role changed"),

    /**
     * `LEFT_GROUP` type of the changelog event when the user left a group
     */
    LEFT_GROUP("Left a group"),

    /**
     * `GROUP_DELETED` type of the changelog event when a group has been deleted
     */
    GROUP_DELETED("Group deleted"),

    /**
     * `PROJECT_ADDED` type of the changelog event when a new project of a group has been added
     */
    PROJECT_ADDED("Project added"),

    /**
     * `PROJECT_REMOVED` type of the changelog event when a project of a group has been removed
     */
    PROJECT_REMOVED("Project removed"),

    /**
     * `UPDATE_SCHEDULED` type of the changelog event when a new update of project of a group has been scheduled
     */
    UPDATE_SCHEDULED("Update scheduled"),

    /**
     * `UPDATE_STARTED` type of the changelog event when an update of project of a group has been started
     */
    UPDATE_STARTED("Update started"),

    /**
     * `UPDATE_PUBLISHED` type of the changelog event when an update of project of a group has been published
     */
    UPDATE_PUBLISHED("Update published"),

    /**
     * `UPDATE_DELETED` type of the changelog event when an update of project of a group has been deleted
     */
    UPDATE_DELETED("Update deleted")

}