package com.tecknobit.pandoro.services.groups.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
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
     * {@code project_description} the project_description of the group
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
     * {@code totalMembers} how many groupMembers the group has
     */
    @Transient
    private final int totalMembers;

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
     * {@code totalProjects} the number of the projects managed by the group
     */
    @Transient
    private final int totalProjects;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public Group() {
        this(null, null, -1, null, null, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link Group} object
     *
     * @param id:          identifier of the group
     * @param name:        name of the group
     * @param creationDate: when the project has been created
     * @param author:      the author of the group
     * @param description:{@code project_description} the project_description of the group
     * @param groupMembers:     the list of the groupMembers of the group
     * @param projects:    the list of the projects managed by the group
     */
    public Group(String id, String name, long creationDate, PandoroUser author, String description,
                 ArrayList<GroupMember> groupMembers, ArrayList<Project> projects) {
        super(id, name);
        this.creationDate = creationDate;
        this.author = author;
        this.description = description;
        this.groupMembers = groupMembers;
        if (groupMembers != null)
            totalMembers = groupMembers.size();
        else
            totalMembers = 0;
        this.projects = projects;
        if (projects != null)
            totalProjects = projects.size();
        else
            totalProjects = 0;
    }

    /**
     * Method to get {@link #creationDate} instance <br>
     * No-any params required
     *
     * @return {@link #creationDate} instance as long
     */
    @JsonGetter(CREATION_DATE_KEY)
    public long getCreation() {
        return creationDate;
    }

    /**
     * Method to get {@link #author} instance <br>
     * No-any params required
     *
     * @return {@link #author} instance as {@link PandoroUser}
     */
    public PandoroUser getAuthor() {
        return author;
    }

    /**
     * Method to get {@link #description} instance <br>
     * No-any params required
     *
     * @return {@link #description} instance as {@link String}
     */
    @JsonGetter(GROUP_DESCRIPTION_KEY)
    public String getDescription() {
        return description;
    }

    /**
     * Method to get {@link #groupMembers} instance <br>
     * No-any params required
     *
     * @return {@link #groupMembers} instance as {@link ArrayList} of {@link GroupMember}
     */
    @JsonGetter(GROUP_MEMBERS_TABLE)
    public ArrayList<GroupMember> getMembers() {
        return new ArrayList<>(groupMembers);
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
