package com.tecknobit.pandorocore.enums

import kotlinx.serialization.Serializable

/**
 * `InvitationStatus` list of available invitation statuses for a group's member
 */
@Serializable
enum class InvitationStatus {

    /**
     * `PENDING` invitation status
     *
     * @apiNote this invitation status means that the member has been invited, and it is not joined yet
     */
    PENDING,

    /**
     * `JOINED` invitation status
     *
     * @apiNote this invitation status means that the member has joined in the group
     */
    JOINED

}