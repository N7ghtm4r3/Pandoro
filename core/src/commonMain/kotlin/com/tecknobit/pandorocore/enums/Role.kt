package com.tecknobit.pandorocore.enums

import kotlinx.serialization.Serializable

/**
 * `Role` list of available roles for a group's member
 */
@Serializable
enum class Role {

    /**
     * `ADMIN` role
     *
     * @apiNote this role allows to manage the members of the group, so add or remove them, and also manage projects,
     * so add or remove them
     */
    ADMIN,

    /**
     * `MAINTAINER` role
     *
     * @apiNote this role allows to manage the members of the group, so add or remove them
     */
    MAINTAINER,

    /**
     * `DEVELOPER` role
     *
     * @apiNote this role allows see the members of the group and the projects managed by the group
     */
    DEVELOPER

}