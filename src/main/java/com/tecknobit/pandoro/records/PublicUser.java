package com.tecknobit.pandoro.records;

import jakarta.persistence.*;

import java.io.Serializable;

import static com.tecknobit.pandoro.services.UsersHelper.*;

/**
 * The {@code PublicUser} class is useful to create a <b>Pandoro's public user</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = PUBLIC_USERS_TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PublicUser extends PandoroItem {

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
            columnDefinition = "text default '" + DEFAULT_PROFILE_PIC + "'",
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

}
