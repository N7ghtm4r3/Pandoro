package com.tecknobit.pandoro.services.users.entities;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.pandoro.services.PandoroItem;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandorocore.enums.InvitationStatus;
import com.tecknobit.pandorocore.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code GroupMember} class is useful to create a <b>Pandoro's group member</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = GROUP_MEMBERS_TABLE)
@IdClass(GroupMemberCompositeKey.class)
public class GroupMember {

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
            columnDefinition = "text default '" + PandoroUser.DEFAULT_PROFILE_PIC + "'",
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

    /**
     * {@code invitationStatus} status of the invitation
     */
    @Enumerated(EnumType.STRING)
    @Column(name = INVITATION_STATUS_KEY)
    private final InvitationStatus invitationStatus;

    /**
     * {@code group_member} the group of the member
     *
     * @apiNote usage in SQL scopes
     */
    @Id
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = GROUP_MEMBER_KEY)
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
     * @param role:{@code role} the role of the member
     * @param invitationStatus:{@code invitationStatus} status of the invitation
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
     * Method to get {@link #id} instance
     *
     * @return {@link #id} instance as {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get {@link #name} instance
     *
     * @return {@link #name} instance as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get {@link #profilePic} instance
     *
     * @return {@link #profilePic} instance as {@link String}
     */
    @JsonGetter(PROFILE_PIC_KEY)
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Method to get {@link #surname} instance
     *
     * @return {@link #surname} instance as {@link String}
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Method to get the complete name of the user
     *
     * @return complete name of the user as {@link String}
     * @apiNote {@link #name} (and) {@link #surname}
     */
    @JsonIgnore
    public String getCompleteName() {
        return name + " " + surname;
    }

    /**
     * Method to get {@link #email} instance
     *
     * @return {@link #email} instance as {@link String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to get {@link #role} instance
     *
     * @return {@link #role} instance as {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * Method to get {@link #invitationStatus} instance
     *
     * @return {@link #invitationStatus} instance as {@link InvitationStatus}
     */
    @JsonGetter(INVITATION_STATUS_KEY)
    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    /**
     * Method to check if the member is a {@link Role#MAINTAINER}
     *
     * @return whether the member is a {@link Role#MAINTAINER} as boolean
     */
    @JsonIgnore
    public boolean isMaintainer() {
        return isAdmin() || role == Role.MAINTAINER;
    }

    /**
     * Method to check if the member is a {@link Role#ADMIN}
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
    public boolean isLoggedUser(PandoroUser userLogged) {
        return userLogged.getId().equals(id);
    }

}
