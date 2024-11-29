package com.tecknobit.pandoro.services.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser;
import com.tecknobit.pandoro.services.changelogs.entity.Changelog;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandorocore.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.AUTHOR_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.CHANGELOG_OWNER_KEY;

/**
 * The {@code PandoroUser} class is useful to create a <b>Pandoro's user</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxbackend.environment.models.EquinoxItem
 * @see EquinoxUser
 */
@Entity
@Table(name = USERS_KEY)
public class PandoroUser extends EquinoxUser {

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
    private List<Note> notes;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public PandoroUser() {
        this(null, null, null, DEFAULT_PROFILE_PIC, null, null, null, null, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link PandoroUser} object
     *
     * @param id         :         identifier of the user
     * @param token: token of the user
     * @param name       :       name of the user
     * @param surname    :    the surname of the user
     * @param email      :      the email of the user
     * @param password   :   the password of the user
     * @param language: the language of the user
     */
    public PandoroUser(String id, String name, String token, String surname, String email, String password, String language) {
        this(id, name, token, null, surname, email, password, language, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link PandoroUser} object
     *
     * @param id         :         identifier of the user
     * @param token: token of the user
     * @param name       :       name of the user
     * @param surname    :    the surname of the user
     * @param email      :      the email of the user
     * @param password   :   the password of the user
     * @param profilePic : the profile picture of the user
     * @param language:the language of the user
     * @param changelogs : list of action messages for the user
     * @param notes      :      list of the notes of the user
     * @param projects   :   list of the projects of the user
     * @param groups     :     list of the groups of the user
     */
    public PandoroUser(String id, String token, String name, String surname, String email, String password, String profilePic,
                       String language, List<Changelog> changelogs, List<Note> notes, List<Project> projects, List<Group> groups) {
        super(id, token, name, surname, email, password, profilePic, language, null);
        this.changelogs = changelogs;
        this.notes = notes;
        this.projects = projects;
        this.groups = groups;
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
    @JsonIgnore
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
     * Method to set {@link #notes} instance <br>
     *
     * @param notes:{@code notes} list of the notes of the user
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    /**
     * Method to get the groups where the user is the {@link Role#ADMIN} <br>
     * No-any params required
     *
     * @return groups as {@link ArrayList} of {@link Group}
     */
    @JsonIgnore
    public ArrayList<Group> getAdminGroups() {
        ArrayList<Group> subGroups = new ArrayList<>();
        for (Group group : groups)
            if (group.isUserAdmin(this))
                subGroups.add(group);
        return subGroups;
    }

}

