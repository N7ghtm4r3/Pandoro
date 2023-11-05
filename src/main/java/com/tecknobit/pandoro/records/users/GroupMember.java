package com.tecknobit.pandoro.records.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.PandoroItem;
import jakarta.persistence.*;

import java.io.Serializable;

import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.MEMBER_ROLE_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.GROUP_MEMBERS_TABLE;

/**
 * The {@code GroupMember} class is useful to create a <b>Pandoro's group member</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItem
 * @see PublicUser
 * @see Serializable
 */
@Entity
@Table(name = GROUP_MEMBERS_TABLE)
@DiscriminatorValue(MEMBER_ROLE_KEY)
public class GroupMember extends PublicUser {

    /**
     * {@code Role} list of available roles for a group's member
     */
    public enum Role {

        /**
         * {@code ADMIN} role
         *
         * @apiNote this role allows to manage the groupMembers of the group, so add or remove them, and also manage projects,
         * so add or remove them
         */
        ADMIN,

        /**
         * {@code MAINTAINER} role
         *
         * @apiNote this role allows to manage the groupMembers of the group, so add or remove them
         */
        MAINTAINER,

        /**
         * {@code DEVELOPER} role
         *
         * @apiNote this role allows see the groupMembers of the group and the projects managed by the group
         */
        DEVELOPER

    }

    /**
     * {@code role} the role of the member
     */
    @Column(name = MEMBER_ROLE_KEY)
    private final Role role;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = GROUP_KEY)
    @JsonIgnore
    private Group groupMember;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public GroupMember() {
        this(null, null, null, null, null, null);
    }

    /**
     * Constructor to init a {@link GroupMember} object
     *
     * @param id:         identifier of the member
     * @param name:       name of the member
     * @param profilePic: the profile picture of the member
     * @param surname:    the surname of the member
     * @param email:      the email of the member
     * @param role:       the role of the member
     */
    public GroupMember(String id, String name, String profilePic, String surname, String email, Role role) {
        super(id, name, surname, profilePic, email);
        this.role = role;
    }

    /**
     * Method to get {@link #role} instance <br>
     * No-any params required
     *
     * @return {@link #role} instance as {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * Method to check if the member is a {@link Role#MAINTAINER} <br>
     * No-any params required
     *
     * @return whether the member is a {@link Role#MAINTAINER} as boolean
     */
    public boolean isMaintainer() {
        return isAdmin() || role == Role.MAINTAINER;
    }

    /**
     * Method to check if the member is a {@link Role#ADMIN} <br>
     * No-any params required
     *
     * @return whether the member is a {@link Role#ADMIN} as boolean
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    /**
     * Method to check if the user logged in the session is the current member iterated <br>
     *
     * @param userLogged: the user logged in the session
     * @return whether the user logged in the session is the current member iterated as boolean
     */
    public boolean isLoggedUser(User userLogged) {
        return userLogged.getId().equals(id);
    }

}
