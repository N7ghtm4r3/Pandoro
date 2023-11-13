package com.tecknobit.pandoro.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.pandoro.records.structures.PandoroItem;
import com.tecknobit.pandoro.records.structures.PandoroItemStructure;
import com.tecknobit.pandoro.records.users.GroupMember;
import com.tecknobit.pandoro.records.users.GroupMember.Role;
import com.tecknobit.pandoro.records.users.PublicUser;
import com.tecknobit.pandoro.records.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.controllers.ChangelogsController.CHANGELOGS_KEY;
import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY;
import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_DESCRIPTION_KEY;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_MEMBER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.PROJECTS_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.*;

/**
 * The {@code Group} class is useful to create a <b>Pandoro's Group</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItemStructure
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = GROUPS_KEY)
public class Group extends PandoroItem {

    /**
     * {@code GROUP_NAME_MAX_LENGTH} the max length of the name for a group
     */
    public static final int GROUP_NAME_MAX_LENGTH = 15;

    /**
     * {@code GROUP_DESCRIPTION_MAX_LENGTH} the max description of the name for a group
     */
    public static final int GROUP_DESCRIPTION_MAX_LENGTH = 30;

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
            COMPLETE_NAME_KEY,
            CHANGELOGS_KEY,
            GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            UNREAD_CHANGELOGS_KEY,
            ADMIN_GROUPS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    private final User author;

    /**
     * {@code description} the description of the group
     */
    @Column(name = GROUP_DESCRIPTION_KEY)
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
    @JsonIgnoreProperties(GROUPS_KEY)
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
        this(null, null, null, null, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link Group} object
     *
     * @param jGroup: group details as {@link JSONObject}
     */
    public Group(JSONObject jGroup) {
        super(jGroup);
        author = User.getInstance(hItem.getJSONObject(AUTHOR_KEY));
        description = hItem.getString(GROUP_DESCRIPTION_KEY);
        groupMembers = GroupMember.getInstances(hItem.getJSONArray(GROUP_MEMBERS_TABLE));
        totalMembers = groupMembers.size();
        projects = Project.getInstances(hItem.getJSONArray(PROJECTS_KEY));
        totalProjects = projects.size();
    }

    /**
     * Constructor to init a {@link Group} object
     *
     * @param id:          identifier of the group
     * @param name:        name of the group
     * @param author:      the author of the group
     * @param description:{@code description} the description of the group
     * @param groupMembers:     the list of the groupMembers of the group
     * @param projects:    the list of the projects managed by the group
     */
    public Group(String id, String name, User author, String description, ArrayList<GroupMember> groupMembers,
                 ArrayList<Project> projects) {
        super(id, name);
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
     * Method to get {@link #author} instance <br>
     * No-any params required
     *
     * @return {@link #author} instance as {@link PublicUser}
     */
    public PublicUser getAuthor() {
        return author;
    }

    /**
     * Method to get {@link #description} instance <br>
     * No-any params required
     *
     * @return {@link #description} instance as {@link String}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get {@link #groupMembers} instance <br>
     * No-any params required
     *
     * @return {@link #groupMembers} instance as {@link ArrayList} of {@link GroupMember}
     */
    public ArrayList<GroupMember> getMembers() {
        return new ArrayList<>(groupMembers);
    }

    /**
     * Method to get {@link #totalMembers} instance <br>
     * No-any params required
     *
     * @return {@link #totalMembers} instance as int
     */
    @JsonIgnore
    public int getTotalMembers() {
        return totalMembers;
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
     * Method to get {@link #totalProjects} instance <br>
     * No-any params required
     *
     * @return {@link #totalProjects} instance as int
     */
    @JsonIgnore
    public int getTotalProjects() {
        return totalProjects;
    }

    /**
     * Method to check if a user is a {@link Role#MAINTAINER} of the group
     *
     * @param user: the user to check the role
     * @return whether the user is a {@link Role#MAINTAINER} as boolean
     */
    public boolean isUserMaintainer(User user) {
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
    public boolean isUserAdmin(User user) {
        for (GroupMember groupMember : groupMembers)
            if (user.getId().equals(groupMember.getId()))
                return groupMember.isAdmin();
        return false;
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItems: items details as {@link JSONArray}
     * @return instance as {@link ArrayList} of {@link Group}
     */
    @Returner
    public static ArrayList<Group> getInstances(JSONArray jItems) {
        ArrayList<Group> groups = new ArrayList<>();
        if (jItems != null) {
            for (int j = 0; j < jItems.length(); j++)
                groups.add(new Group(jItems.getJSONObject(j)));
        }
        return groups;
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItem: item details as {@link JSONObject}
     * @return instance as {@link Group}
     */
    @Returner
    public static Group getInstance(JSONObject jItem) {
        if (jItem == null)
            return null;
        else
            return new Group(jItem);
    }

}
