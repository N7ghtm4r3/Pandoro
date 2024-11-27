package com.tecknobit.pandoro.services.users.models;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.pandoro.services.PandoroItem;
import com.tecknobit.pandoro.services.groups.model.Group;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

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
     * {@code Role} list of available roles for a group's member
     */
    public enum Role {

        /**
         * {@code ADMIN} role
         *
         * @apiNote this role allows to manage the members of the group, so add or remove them, and also manage projects,
         * so add or remove them
         */
        ADMIN,

        /**
         * {@code MAINTAINER} role
         *
         * @apiNote this role allows to manage the members of the group, so add or remove them
         */
        MAINTAINER,

        /**
         * {@code DEVELOPER} role
         *
         * @apiNote this role allows see the members of the group and the projects managed by the group
         */
        DEVELOPER

    }

    /**
     * {@code InvitationStatus} list of available invitation statuses for a group's member
     */
    public enum InvitationStatus {

        /**
         * {@code PENDING} invitation status
         *
         * @apiNote this invitation status means that the member has been invited, and it is not joined yet
         */
        PENDING,

        /**
         * {@code JOINED} invitation status
         *
         * @apiNote this invitation status means that the member has joined in the group
         */
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
     * @param jGroupMember: group member details as {@link JSONObject}
     */
    public GroupMember(JSONObject jGroupMember) {
        JsonHelper hItem = new JsonHelper(jGroupMember);
        id = hItem.getString(IDENTIFIER_KEY);
        name = hItem.getString(NAME_KEY);
        surname = hItem.getString(SURNAME_KEY);
        profilePic = hItem.getString(PROFILE_PIC_KEY);
        email = hItem.getString(EMAIL_KEY);
        role = Role.valueOf(hItem.getString(MEMBER_ROLE_KEY, Role.DEVELOPER.name()));
        invitationStatus = InvitationStatus.valueOf(hItem.getString(INVITATION_STATUS_KEY, InvitationStatus.PENDING.name()));
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
    @JsonGetter(PROFILE_PIC_KEY)
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
    @JsonGetter(INVITATION_STATUS_KEY)
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
    public boolean isLoggedUser(PandoroUser userLogged) {
        return userLogged.getId().equals(id);
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItems: items details as {@link JSONArray}
     * @return instance as {@link ArrayList} of {@link GroupMember}
     */
    @Returner
    public static ArrayList<GroupMember> getInstances(JSONArray jItems) {
        ArrayList<GroupMember> groupMembers = new ArrayList<>();
        if (jItems != null) {
            for (int j = 0; j < jItems.length(); j++)
                groupMembers.add(new GroupMember(jItems.getJSONObject(j)));
        }
        return groupMembers;
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItem: item details as {@link JSONObject}
     * @return instance as {@link GroupMember}
     */
    @Returner
    public static GroupMember getInstance(JSONObject jItem) {
        if (jItem == null)
            return null;
        else
            return new GroupMember(jItem);
    }

}
