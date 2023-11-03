package com.tecknobit.pandoro.records;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.services.ChangelogsHelper.CHANGELOG_OWNER_KEY;
import static com.tecknobit.pandoro.services.NotesHelper.AUTHOR_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.*;

/**
 * The {@code User} class is useful to create a <b>Pandoro's user</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItem
 * @see Serializable
 */

@Entity
@Table(name = USERS_TABLE)
public class User extends PandoroItem {

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
     * {@code PASSWORD_MIN_LENGTH} the min length of the password for a user
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * {@code PASSWORD_MAX_LENGTH} the max length of the password for a user
     */
    public static final int PASSWORD_MAX_LENGTH = 32;

    /**
     * token of the user
     */
    @Column(
            name = TOKEN_KEY,
            unique = true
    )
    private final String token;

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
    @Column(name = EMAIL_KEY, unique = true)
    private final String email;

    /**
     * {@code password} the password of the user
     */
    @Column(name = PASSWORD_KEY)
    private final String password;

    /**
     * {@code changelogs} list of action messages for the user
     */
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = CHANGELOG_OWNER_KEY,
            cascade = CascadeType.ALL
    )
    private final List<Changelog> changelogs;

    /**
     * {@code groups} list of the groups of the user
     */
    @Column(name = GROUPS_KEY)
    private final ArrayList<Group> groups;

    /**
     * {@code projects} list of the projects of the user
     */
    @Column(name = PROJECTS_KEY)
    private final List<Project> projects;

    /**
     * {@code notes} list of the notes of the user
     */
    @OneToMany(
            mappedBy = AUTHOR_KEY,
            cascade = CascadeType.ALL
    )
    private final List<Note> notes;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public User() {
        this(null, null, null, null, null, null, null, null,
                null, null, null);
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param token:token of the user
     * @param surname : the surname of the user
     * @param email   :   the email of the user
     * @param password   :   the password of the user
     *
     */
    public User(String id, String name, String token, String surname, String email, String password) {
        this(id, name, token, null, surname, email, password, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param surname : the surname of the user
     * @param token:token of the user
     */
    // TODO: 19/08/2023 TO REMOVE
    public User(String id, String name, String surname, String token) {
        this(id, name, token, "", surname, "maurizio.manuel2003@gmail.com", "pass", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param name:    name of the user
     * @param surname:{@code surname} the surname of the user
     */
    // TODO: 19/08/2023 TO REMOVE
    public User(String name, String surname) {
        this("", name, "token", "", surname, "maurizio.manuel2003@gmail.com", "pass", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param token:token of the user
     * @param profilePic : the profile picture of the user
     * @param surname    :    the surname of the user
     * @param email      :      the email of the user
     * @param password   :   the password of the user
     * @param changelogs : list of action messages for the user
     * @param groups     :     list of the groups of the user
     * @param projects   :   list of the projects of the user
     * @param notes      :      list of the notes of the user
     */
    public User(String id, String name, String token, String profilePic, String surname, String email, String password,
                ArrayList<Changelog> changelogs, ArrayList<Group> groups, ArrayList<Project> projects,
                ArrayList<Note> notes) {
        super(id, name);
        this.token = token;
        this.profilePic = profilePic;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.groups = groups;
        this.changelogs = changelogs;
        this.projects = projects;
        this.notes = notes;
    }

    /**
     * Method to get {@link #token} instance <br>
     * No-any params required
     *
     * @return {@link #token} instance as {@link String}
     */
    public String getToken() {
        return token;
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
     * Method to get {@link #password} instance <br>
     * No-any params required
     *
     * @return {@link #password} instance as {@link String}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to get {@link #groups} instance <br>
     * No-any params required
     *
     * @return {@link #groups} instance as {@link ArrayList} of {@link Group}
     */
    public ArrayList<Group> getGroups() {
        return groups;
    }

    /**
     * Method to get {@link #changelogs} instance <br>
     * No-any params required
     *
     * @return {@link #changelogs} instance as {@link ArrayList} of {@link Changelog}
     */
    public ArrayList<Changelog> getChangelogs() {
        return new ArrayList<>(changelogs);
    }

    /**
     * Method to get the number of {@link #changelogs} unread <br>
     * No-any params required
     *
     * @return the number of {@link #changelogs} unread as int
     */
    public int getUnreadChangelogsNumber() {
        int unread = 0;
        for (Changelog changelog : changelogs)
            if (!changelog.isRed())
                unread++;
        return unread;
    }

    /**
     * Method to get {@link #projects} instance <br>
     * No-any params required
     *
     * @return {@link #projects} instance as {@link ArrayList} of {@link Project}
     */
    public ArrayList<Project> getProjects() {
        return new ArrayList<>(projects);
    }

    /**
     * Method to get {@link #notes} instance <br>
     * No-any params required
     *
     * @return {@link #notes} instance as {@link ArrayList} of {@link Note}
     */
    public ArrayList<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    /**
     * Method to get the groups where the user is the {@link Group.Role#ADMIN} <br>
     * No-any params required
     *
     * @return groups as {@link ArrayList} of {@link Group}
     */
    public ArrayList<Group> getAdminGroups() {
        ArrayList<Group> subGroups = new ArrayList<>();
        for (Group group : groups)
            if (group.isUserAdmin(this))
                subGroups.add(group);
        return subGroups;
    }

}

