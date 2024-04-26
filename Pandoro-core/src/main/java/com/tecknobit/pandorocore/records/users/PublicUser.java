package com.tecknobit.pandorocore.records.users;

import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.pandorocore.records.structures.PandoroItem;
import com.tecknobit.pandorocore.records.structures.PandoroItemStructure;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.io.Serializable;

import static com.tecknobit.pandorocore.records.users.PublicUser.PUBLIC_USERS_TABLE;

/**
 * The {@code PublicUser} class is useful to create a <b>Pandoro's public user</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItemStructure
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = PUBLIC_USERS_TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PublicUser extends PandoroItem {

    /**
     * {@code PUBLIC_USERS_TABLE} public users table
     */
    public static final String PUBLIC_USERS_TABLE = "public_users";

    /**
     * {@code USERS_TABLE} users table
     */
    public static final String USERS_TABLE = "users";

    /**
     * {@code GROUP_MEMBERS_TABLE} group members table
     */
    public static final String GROUP_MEMBERS_TABLE = "group_members";

    /**
     * {@code NAME_KEY} name key
     */
    public static final String NAME_KEY = "name";

    /**
     * {@code TOKEN_KEY} token key
     */
    public static final String TOKEN_KEY = "token";

    /**
     * {@code COMPLETE_NAME_KEY} complete name key
     */
    public static final String COMPLETE_NAME_KEY = "completeName";

    /**
     * {@code SURNAME_KEY} surname key
     */
    public static final String SURNAME_KEY = "surname";

    /**
     * {@code PROFILE_PIC_KEY} profile pic key
     */
    public static final String PROFILE_PIC_KEY = "profile_pic";

    /**
     * {@code EMAIL_KEY} email key
     */
    public static final String EMAIL_KEY = "email";

    /**
     * {@code PASSWORD_KEY} password key
     */
    public static final String PASSWORD_KEY = "password";

    /**
     * {@code UNREAD_CHANGELOGS_KEY} unread changelogs number key
     */
    public static final String UNREAD_CHANGELOGS_KEY = "unreadChangelogsNumber";

    /**
     * {@code ADMIN_GROUPS_KEY} admin groups key
     */
    public static final String ADMIN_GROUPS_KEY = "adminGroups";

    /**
     * {@code USER_NAME_MAX_LENGTH} the max length of the name for a user
     */
    public static final int USER_NAME_MAX_LENGTH = 20;

    /**
     * {@code USER_SURNAME_MAX_LENGTH} the max length of the surname for a user
     */
    public static final int USER_SURNAME_MAX_LENGTH = 30;

    /**
     * {@code EMAIL_MAX_LENGTH} the max length of the email for a user
     */
    public static final int EMAIL_MAX_LENGTH = 50;

    /**
     * {@code surname} the surname of the user
     */
    @Column(name = SURNAME_KEY)
    protected final String surname;

    /**
     * {@code profilePic} the profile picture of the user
     */
    @Column(
            name = PROFILE_PIC_KEY,
            columnDefinition = "text default '" + User.DEFAULT_PROFILE_PIC + "'",
            insertable = false
    )
    protected final String profilePic;

    /**
     * {@code email} the email of the user
     */
    @Column(name = EMAIL_KEY, unique = true)
    protected final String email;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public PublicUser() {
        this(null, null, null, null, null);
    }

    /**
     * Constructor to init a {@link PublicUser} object
     *
     * @param jPublicUser: public user details as {@link JSONObject}
     */
    public PublicUser(JSONObject jPublicUser) {
        super(jPublicUser);
        surname = hItem.getString(SURNAME_KEY);
        profilePic = hItem.getString(PROFILE_PIC_KEY, User.DEFAULT_PROFILE_PIC);
        email = hItem.getString(EMAIL_KEY);
    }

    /**
     * Constructor to init a {@link PublicUser} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param profilePic : the profile picture of the user
     * @param surname    :    the surname of the user
     * @param email      :      the email of the user
     */
    public PublicUser(String id, String name, String surname, String profilePic, String email) {
        super(id, name);
        this.surname = surname;
        this.profilePic = profilePic;
        this.email = email;
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
     * Method to get an instance of this Telegram's type
     *
     * @param jItem: item details as {@link JSONObject}
     * @return instance as {@link PublicUser}
     */
    @Returner
    public static PublicUser getInstance(JSONObject jItem) {
        if (jItem == null)
            return null;
        else
            return new PublicUser(jItem);
    }

}
