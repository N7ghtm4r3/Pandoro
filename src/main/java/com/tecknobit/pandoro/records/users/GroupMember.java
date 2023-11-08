package com.tecknobit.pandoro.records.users;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.PandoroItem;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.*;

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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = IDENTIFIER_KEY)
@IdClass(GroupMemberCompositeKey.class)
public class GroupMember {

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

    public enum InvitationStatus {

        PENDING,

        JOINED

    }

    /**
     * {@code id} identifier of the item
     */
    @Id
    @Column(name = IDENTIFIER_KEY)
    private final String id;

    /**
     * {@code name} of the item
     */
    @Column(name = NAME_KEY)
    private final String name;

    /**
     * {@code surname} the surname of the user
     */
    @Column(name = SURNAME_KEY)
    private final String surname;

    /**
     * {@code profilePic} the profile picture of the user
     */
    @Column(
            name = PROFILE_PIC_KEY,
            columnDefinition = "text default '" + DEFAULT_PROFILE_PIC + "'",
            insertable = false
    )
    private final String profilePic;

    /**
     * {@code email} the email of the user
     */
    @Column(name = EMAIL_KEY)
    private final String email;

    /**
     * {@code role} the role of the member
     */
    @Enumerated(EnumType.STRING)
    @Column(name = MEMBER_ROLE_KEY)
    private final Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = INVITATION_STATUS_KEY)
    private final InvitationStatus invitationStatus;

    @Id
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = GROUP_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Group group_member;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public GroupMember() {
        this(null, null, null, null, null, null, null);
    }

    /**
     * Constructor to init a {@link GroupMember} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param profilePic : the profile picture of the user
     * @param surname    :    the surname of the user
     * @param email      :      the email of the user
     */
    public GroupMember(String id, String name, String surname, String profilePic, String email, Role role,
                       InvitationStatus invitationStatus) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.profilePic = profilePic;
        this.email = email;
        this.role = role;
        this.invitationStatus = invitationStatus;
    }

    /**
     * Method to get {@link #id} instance <br>
     * No-any params required
     *
     * @return {@link #id} instance as {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get {@link #name} instance <br>
     * No-any params required
     *
     * @return {@link #name} instance as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get {@link #profilePic} instance <br>
     * No-any params required
     *
     * @return {@link #profilePic} instance as {@link String}
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Method to get {@link #surname} instance <br>
     * No-any params required
     *
     * @return {@link #surname} instance as {@link String}
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Method to get the complete name of the user <br>
     * No-any params required
     *
     * @return complete name of the user as {@link String}
     * @apiNote {@link #name} (and) {@link #surname}
     */
    @JsonIgnore
    public String getCompleteName() {
        return name + " " + surname;
    }

    /**
     * Method to get {@link #email} instance <br>
     * No-any params required
     *
     * @return {@link #email} instance as {@link String}
     */
    public String getEmail() {
        return email;
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
     * Method to get {@link #invitationStatus} instance <br>
     * No-any params required
     *
     * @return {@link #invitationStatus} instance as {@link InvitationStatus}
     */
    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    /**
     * Method to check if the member is a {@link Role#MAINTAINER} <br>
     * No-any params required
     *
     * @return whether the member is a {@link Role#MAINTAINER} as boolean
     */
    @JsonIgnore
    public boolean isMaintainer() {
        return isAdmin() || role == Role.MAINTAINER;
    }

    /**
     * Method to check if the member is a {@link Role#ADMIN} <br>
     * No-any params required
     *
     * @return whether the member is a {@link Role#ADMIN} as boolean
     */
    @JsonIgnore
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
