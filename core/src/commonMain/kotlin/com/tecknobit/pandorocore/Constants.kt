package com.tecknobit.pandorocore

/**
 * `GROUP_MEMBERS_TABLE` group members table
 */
const val GROUP_MEMBERS_TABLE = "group_members"

/**
 * `PROJECTS_KEY` projects key
 */
const val PROJECTS_KEY = "projects"

/**
 * `FILTERS_KEY` filters key
 */
const val FILTERS_KEY = "filters"

/**
 * `PROJECT_IDENTIFIER_KEY` project identifier key
 */
const val PROJECT_IDENTIFIER_KEY = "project_id"

/**
 * `PROJECT_ICON_KEY` project icon key
 */
const val PROJECT_ICON_KEY = "icon"

/**
 * `PROJECTS_GROUPS_TABLE` projects groups table
 */
const val PROJECTS_GROUPS_TABLE = "projects_groups"

/**
 * `PROJECT_KEY` project key
 */
const val PROJECT_KEY = "project"

/**
 * `PROJECT_DESCRIPTION_KEY` project description key
 */
const val PROJECT_DESCRIPTION_KEY = "project_description"

/**
 * `PROJECT_VERSION_KEY` project version key
 */
const val PROJECT_VERSION_KEY = "project_version"

/**
 * `PROJECT_REPOSITORY_KEY` project repository key
 */
const val PROJECT_REPOSITORY_KEY = "project_repository"

/**
 * `UPDATES_KEY` updates key
 */
const val UPDATES_KEY = "updates"

/**
 * `UPDATE_IDENTIFIER_KEY` update identifier key
 */
const val UPDATE_IDENTIFIER_KEY = "update_id"

/**
 * `DESTINATION_UPDATE_IDENTIFIER_KEY` destination update identifier key
 */
const val DESTINATION_UPDATE_IDENTIFIER_KEY = "destination_update_id"

/**
 * `UPDATE_KEY` update key
 */
// TODO: TO WARN ABOUT ALTER:
// TODO: - ALTER TABLE `notes` CHANGE `project_update` `update` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL;
const val UPDATE_KEY = "update"

/**
 * `UPDATE_ESCAPED_KEY` `update` key, it is used in the queries
 */
const val UPDATE_ESCAPED_KEY = "`$UPDATE_KEY`"

/**
 * `UPDATE_CREATE_DATE_KEY` create date key
 */
const val UPDATE_CREATE_DATE_KEY = "create_date"

/**
 * `UPDATE_TARGET_VERSION_KEY` target version key
 */
const val UPDATE_TARGET_VERSION_KEY = "target_version"

/**
 * `UPDATE_CHANGE_NOTES_KEY` update change notes key
 */
const val UPDATE_CHANGE_NOTES_KEY = "update_change_notes"

/**
 * `UPDATE_STATUS_KEY` update status key
 */
const val UPDATE_STATUS_KEY = "status"

/**
 * `UPDATE_STARTED_BY_KEY` started by key
 */
const val UPDATE_STARTED_BY_KEY = "started_by"

/**
 * `UPDATE_START_DATE_KEY` start date key
 */
const val UPDATE_START_DATE_KEY = "start_date"

/**
 * `UPDATE_PUBLISHED_BY_KEY` published by key
 */
const val UPDATE_PUBLISHED_BY_KEY = "published_by"

/**
 * `UPDATE_PUBLISH_DATE_KEY` publish date key
 */
const val UPDATE_PUBLISH_DATE_KEY = "publish_date"

/**
 * `GROUPS_KEY` groups key
 */
const val GROUPS_KEY = "groups"

/**
 * `ONLY_AUTHORED_GROUPS` authored groups key
 */
const val ONLY_AUTHORED_GROUPS = "authored_groups"

/**
 * `DEFAULT_ROLES_FILTER_VALUE` the default value of the roles filter
 */
const val DEFAULT_ROLES_FILTER_VALUE = ""

/**
 * `ROLES_FILTER_KEY` roles filter key
 */
const val ROLES_FILTER_KEY = "roles"

/**
 * `GROUP_IDENTIFIER_KEY` the group identifier key
 */
const val GROUP_IDENTIFIER_KEY = "group_id"

/**
 * `GROUP_KEY` the group key
 */
const val GROUP_KEY = "group"

/**
 * `GROUP_LOGO_KEY` the group logo key
 */
const val GROUP_LOGO_KEY = "logo"

/**
 * `GROUP_MEMBER_KEY` the group member key
 */
const val GROUP_MEMBER_KEY = "group_member"

/**
 * `GROUP_DESCRIPTION_KEY` the group member key
 */
const val GROUP_DESCRIPTION_KEY = "group_description"

/**
 * `GROUP_MEMBERS_KEY` the group members key
 */
const val GROUP_MEMBERS_KEY = "members"

/**
 * `MEMBER_ROLE_KEY` the role of a member key
 */
const val MEMBER_ROLE_KEY = "role"

/**
 * `INVITATION_STATUS_KEY` the invitation status key
 */
const val INVITATION_STATUS_KEY = "invitation_status"

/**
 * `NOTES_KEY` notes key
 */
const val NOTES_KEY = "notes"

/**
 * `NOTE_IDENTIFIER_KEY` the note identifier key
 */
const val NOTE_IDENTIFIER_KEY = "note_id"

/**
 * `CONTENT_NOTE_KEY` the content of the note key
 */
const val CONTENT_NOTE_KEY = "content_note"

/**
 * `MARKED_AS_DONE_KEY` mark as done key
 */
const val MARKED_AS_DONE_KEY = "marked_as_done"

/**
 * `MARKED_AS_DONE_BY_KEY` mark as done author key
 */
const val MARKED_AS_DONE_BY_KEY = "marked_as_done_by"

/**
 * `MARKED_AS_DONE_DATE_KEY` marked as done date key
 */
const val MARKED_AS_DONE_DATE_KEY = "marked_as_done_date"

/**
 * `CHANGELOGS_KEY` changelogs key
 */
const val CHANGELOGS_KEY = "changelogs"

/**
 * `CHANGELOG_IDENTIFIER_KEY` changelog identifier key
 */
const val CHANGELOG_IDENTIFIER_KEY = "changelog_id"

/**
 * `CHANGELOG_EVENT_KEY` changelog event key
 */
const val CHANGELOG_EVENT_KEY = "changelog_event"

/**
 * `CHANGELOG_TIMESTAMP_KEY` changelog timestamp key
 */
const val CHANGELOG_TIMESTAMP_KEY = "timestamp"

/**
 * `CHANGELOG_EXTRA_CONTENT_KEY` extra content of the changelog key
 */
const val CHANGELOG_EXTRA_CONTENT_KEY = "extra_content"

/**
 * `CHANGELOG_READ_KEY` whether the changelog is read key
 */
const val CHANGELOG_READ_KEY = "is_read"

/**
 * `CHANGELOG_OWNER_KEY` owner of the changelog key
 */
const val CHANGELOG_OWNER_KEY = "owner"