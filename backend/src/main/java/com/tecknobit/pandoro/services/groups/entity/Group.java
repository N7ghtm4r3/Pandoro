package com.tecknobit.pandoro.services.groups.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.annotations.EmptyConstructor;
import com.tecknobit.pandoro.services.PandoroItem;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.users.entities.GroupMember;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandorocore.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code Group} class is useful to create a <b>Pandoro's Group</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = GROUPS_KEY)
public class Group extends PandoroItem {

    /**
     * {@code logo} the logo of the group
     */
    @Column(
            name = GROUP_LOGO_KEY,
            columnDefinition = "TEXT DEFAULT '" + DEFAULT_PROFILE_PIC + "'",
            insertable = false
    )
    protected final String logo;

    /**
     * {@code creationDate} when the group has been created
     */
    @Column(name = CREATION_DATE_KEY)
    private final long creationDate;

    /**
     * {@code author} the author of the group
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = AUTHOR_KEY)
    @JsonIgnoreProperties({
            TOKEN_KEY,
            PASSWORD_KEY,
            LANGUAGE_KEY,
            CHANGELOGS_KEY,
            GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final PandoroUser author;

    /**
     * {@code description} the description of the group
     */
    @Lob
    @Column(
            name = GROUP_DESCRIPTION_KEY,
            columnDefinition = "MEDIUMTEXT",
            nullable = false
    )
    private final String description;

    /**
     * {@code groupMembers} the list of the groupMembers of the group
     */
    @OneToMany(
            mappedBy = GROUP_MEMBER_KEY,
            cascade = CascadeType.ALL
    )
    private final List<GroupMember> groupMembers;

    /**
     * {@code projects} the list of the projects managed by the group
     */
    @ManyToMany(
            fetch = FetchType.EAGER,
            mappedBy = GROUPS_KEY
    )
    @JsonIgnoreProperties({
            GROUPS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final List<Project> projects;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    @EmptyConstructor
    public Group() {
        this(null, null, null, -1, null, null, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link Group} object
     *
     * @param id         identifier of the group
     * @param name      name of the group
     * @param logo the logo of the group
     * @param creationDate when the project has been created
     * @param author    the author of the group
     * @param description the description of the group
     * @param groupMembers    the list of the groupMembers of the group
     * @param projects  the list of the projects managed by the group
     */
    public Group(String id, String name, String logo, long creationDate, PandoroUser author, String description,
                 ArrayList<GroupMember> groupMembers, ArrayList<Project> projects) {
        super(id, name);
        this.logo = logo;
        this.creationDate = creationDate;
        this.author = author;
        this.description = description;
        this.groupMembers = groupMembers;
        this.projects = projects;
    }

    /**
     * Method to get {@link #logo} instance
     *
     * @return {@link #logo} instance as {@link String}
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Method to get {@link #creationDate} instance
     *
     * @return {@link #creationDate} instance as long
     */
    @JsonGetter(CREATION_DATE_KEY)
    public long getCreation() {
        return creationDate;
    }

    /**
     * Method to get {@link #author} instance
     *
     * @return {@link #author} instance as {@link PandoroUser}
     */
    public PandoroUser getAuthor() {
        return author;
    }

    /**
     * Method to get {@link #description} instance
     *
     * @return {@link #description} instance as {@link String}
     */
    @JsonGetter(GROUP_DESCRIPTION_KEY)
    public String getDescription() {
        return description;
    }

    /**
     * Method to get {@link #groupMembers} instance
     *
     * @return {@link #groupMembers} instance as {@link ArrayList} of {@link GroupMember}
     */
    @JsonGetter(GROUP_MEMBERS_KEY)
    public ArrayList<GroupMember> getMembers() {
        return new ArrayList<>(groupMembers);
    }

    /**
     * Method to get {@link #projects} instance
     *
     * @return {@link #projects} instance as {@link ArrayList} of {@link Project}
     */
    public ArrayList<Project> getProjects() {
        return new ArrayList<>(projects);
    }

    /**
     * Method to get the list of identifier from the {@link #projects} instance
     *
     * @return {@link #projects} instance as {@link List} of {@link String}
     */
    public List<String> getProjectsIds(List<String> exclude) {
        HashSet<String> excludeIds = new HashSet<>(exclude);
        ArrayList<String> ids = new ArrayList<>();
        for (Project project : projects) {
            String projectId = project.getId();
            if (!excludeIds.contains(projectId))
                ids.add(project.getId());
        }
        return ids;
    }

    /**
     * Method to check if a user is a {@link Role#MAINTAINER} of the group
     *
     * @param user: the user to check the role
     * @return whether the user is a {@link Role#MAINTAINER} as boolean
     */
    public boolean isUserMaintainer(PandoroUser user) {
        for (GroupMember groupMember : groupMembers)
            if (user.getId().equals(groupMember.getId()))
                return groupMember.isMaintainer();
        return false;
    }

    /**
     * Method to check if a user is an {@link Role#ADMIN} of the group
     *
     * @param user: the user to check the role
     * @return whether the user is an {@link Role#ADMIN} as boolean
     */
    public boolean isUserAdmin(PandoroUser user) {
        for (GroupMember groupMember : groupMembers)
            if (user.getId().equals(groupMember.getId()))
                return groupMember.isAdmin();
        return false;
    }

}
