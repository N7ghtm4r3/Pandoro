package com.tecknobit.pandorocore.records.users;

import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.pandorocore.records.Changelog;
import com.tecknobit.pandorocore.records.Group;
import com.tecknobit.pandorocore.records.Note;
import com.tecknobit.pandorocore.records.Project;
import com.tecknobit.pandorocore.records.structures.PandoroItem;
import com.tecknobit.pandorocore.records.structures.PandoroItemStructure;
import com.tecknobit.pandorocore.records.users.GroupMember.Role;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandorocore.helpers.InputsValidatorKt.DEFAULT_USER_LANGUAGE;
import static com.tecknobit.pandorocore.records.Changelog.CHANGELOGS_KEY;
import static com.tecknobit.pandorocore.records.Changelog.CHANGELOG_OWNER_KEY;
import static com.tecknobit.pandorocore.records.Group.GROUPS_KEY;
import static com.tecknobit.pandorocore.records.Note.NOTES_KEY;
import static com.tecknobit.pandorocore.records.Project.PROJECTS_KEY;
import static com.tecknobit.pandorocore.records.users.PublicUser.TOKEN_KEY;
import static com.tecknobit.pandorocore.records.users.PublicUser.USERS_TABLE;

/**
 * The {@code User} class is useful to create a <b>Pandoro's user</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItemStructure
 * @see PandoroItem
 * @see PublicUser
 * @see Serializable
 */
@Entity
@Table(name = USERS_TABLE)
@DiscriminatorValue(TOKEN_KEY)
public class User extends PublicUser {

    /**
     * {@code LANGUAGE_KEY} language key
     */
    public static final String LANGUAGE_KEY = "language";

    /**
     * {@code PASSWORD_MIN_LENGTH} the min length of the password for a user
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * {@code PASSWORD_MAX_LENGTH} the max length of the password for a user
     */
    public static final int PASSWORD_MAX_LENGTH = 32;

    /**
     * {@code DEFAULT_PROFILE_PIC} the default profile pic path when the user has not set own image
     */
    public static final String DEFAULT_PROFILE_PIC = "profiles/defProfilePic.png";

    /**
     * token of the user
     */
    @Column(
            name = TOKEN_KEY,
            unique = true
    )
    private final String token;

    /**
     * {@code password} the password of the user
     */
    @Column(name = PASSWORD_KEY)
    private final String password;

    /**
     * {@code language} the language of the user
     */
    @Column(name = LANGUAGE_KEY)
    private final String language;

    /**
     * {@code changelogs} list of action messages for the user
     */
    @OneToMany(
            mappedBy = CHANGELOG_OWNER_KEY,
            cascade = CascadeType.ALL
    )
    private final List<Changelog> changelogs;

    /**
     * {@code groups} list of the groups of the user
     */
    @OneToMany(
            mappedBy = AUTHOR_KEY,
            cascade = CascadeType.ALL
    )
    private List<Group> groups;

    /**
     * {@code projects} list of the projects of the user
     */
    @OneToMany(
            mappedBy = AUTHOR_KEY,
            cascade = CascadeType.ALL
    )
    private List<Project> projects;

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
        this(null, null, null, DEFAULT_PROFILE_PIC, null, null, null, null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param jUser: user details as {@link JSONObject}
     */
    public User(JSONObject jUser) {
        super(jUser);
        password = hItem.getString(PASSWORD_KEY);
        language = hItem.getString(LANGUAGE_KEY, DEFAULT_USER_LANGUAGE);
        token = hItem.getString(TOKEN_KEY);
        groups = Group.getInstances(hItem.getJSONArray(GROUPS_KEY));
        changelogs = Changelog.getInstances(hItem.getJSONArray(CHANGELOGS_KEY));
        projects = Project.getInstances(hItem.getJSONArray(PROJECTS_KEY));
        notes = Note.getInstances(hItem.getJSONArray(NOTES_KEY));
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
     * @param language:the language of the user
     *
     */
    public User(String id, String name, String token, String surname, String email, String password, String language) {
        this(id, name, token, null, surname, email, password, language, new ArrayList<>(),
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
     * @param language:the language of the user
     * @param changelogs : list of action messages for the user
     * @param groups     :     list of the groups of the user
     * @param projects   :   list of the projects of the user
     * @param notes      :      list of the notes of the user
     */
    public User(String id, String name, String token, String profilePic, String surname, String email, String password,
                String language, ArrayList<Changelog> changelogs, ArrayList<Group> groups, ArrayList<Project> projects,
                ArrayList<Note> notes) {
        super(id, name, surname, profilePic, email);
        this.password = password;
        this.token = token;
        this.language = language;
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
     * Method to get {@link #password} instance <br>
     * No-any params required
     *
     * @return {@link #password} instance as {@link String}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to get {@link #language} instance <br>
     * No-any params required
     *
     * @return {@link #language} instance as {@link String}
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Method to get {@link #groups} instance <br>
     * No-any params required
     *
     * @return {@link #groups} instance as {@link ArrayList} of {@link Group}
     */
    public ArrayList<Group> getGroups() {
        return new ArrayList<>(groups);
    }

    /**
     * Method to set {@link #groups} instance <br>
     *
     * @param groups:{@code groups} list of the groups of the user
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
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
     * Method to set {@link #projects} instance <br>
     *
     * @param projects:{@code projects} list of the projects of the user
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
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
     * Method to get the groups where the user is the {@link Role#ADMIN} <br>
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

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItem: item details as {@link JSONObject}
     * @return instance as {@link User}
     */
    @Returner
    public static User getInstance(JSONObject jItem) {
        if (jItem == null)
            return null;
        else
            return new User(jItem);
    }

}

