package com.tecknobit.pandorocore.records;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.equinox.environment.records.EquinoxUser;
import com.tecknobit.pandorocore.records.structures.PandoroItem;
import com.tecknobit.pandorocore.records.users.GroupMember;
import com.tecknobit.pandorocore.records.users.GroupMember.Role;
import com.tecknobit.pandorocore.records.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.equinox.environment.records.EquinoxUser.PASSWORD_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.pandorocore.records.Changelog.CHANGELOGS_KEY;
import static com.tecknobit.pandorocore.records.Group.GROUPS_KEY;
import static com.tecknobit.pandorocore.records.Note.NOTES_KEY;
import static com.tecknobit.pandorocore.records.Project.PROJECTS_KEY;
import static com.tecknobit.pandorocore.records.users.User.GROUP_MEMBERS_TABLE;
import static com.tecknobit.pandorocore.records.users.User.LANGUAGE_KEY;

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
     * {@code GROUPS_KEY} groups key
     */
    public static final String GROUPS_KEY = "groups";

    /**
     * {@code GROUP_IDENTIFIER_KEY} the group identifier key
     */
    public static final String GROUP_IDENTIFIER_KEY = "group_id";

    /**
     * {@code GROUP_KEY} the group key
     */
    public static final String GROUP_KEY = "group";

    /**
     * {@code GROUP_MEMBER_KEY} the group member key
     */
    public static final String GROUP_MEMBER_KEY = "group_member";

    /**
     * {@code GROUP_DESCRIPTION_KEY} the group member key
     */
    public static final String GROUP_DESCRIPTION_KEY = "group_description";

    /**
     * {@code GROUP_MEMBERS_KEY} the group members key
     */
    public static final String GROUP_MEMBERS_KEY = "members";

    /**
     * {@code MEMBER_ROLE_KEY} the role of a member key
     */
    public static final String MEMBER_ROLE_KEY = "role";

    /**
     * {@code INVITATION_STATUS_KEY} the invitation status key
     */
    public static final String INVITATION_STATUS_KEY = "invitation_status";

    /**
     * {@code GROUP_NAME_MAX_LENGTH} the max length of the name for a group
     */
    public static final int GROUP_NAME_MAX_LENGTH = 15;

    /**
     * {@code GROUP_DESCRIPTION_MAX_LENGTH} the max description of the name for a group
     */
    public static final int GROUP_DESCRIPTION_MAX_LENGTH = 30;

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
    private final EquinoxUser author;

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
     * @param jGroup: group details as {@link JSONObject}
     */
    public Group(JSONObject jGroup) {
        super(jGroup);
        creationDate = hItem.getLong(CREATION_DATE_KEY);
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
     * @param creationDate: when the project has been created
     * @param author:      the author of the group
     * @param description:{@code description} the description of the group
     * @param groupMembers:     the list of the groupMembers of the group
     * @param projects:    the list of the projects managed by the group
     */
    public Group(String id, String name, long creationDate, User author, String description, ArrayList<GroupMember> groupMembers,
                 ArrayList<Project> projects) {
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
     * Method to get {@link #creationDate} instance <br>
     * No-any params required
     *
     * @return {@link #creationDate} instance as {@link String}
     */
    @JsonIgnore
    public String getCreationDate() {
        return TimeFormatter.getStringDate(creationDate);
    }

    /**
     * Method to get {@link #author} instance <br>
     * No-any params required
     *
     * @return {@link #author} instance as {@link User}
     */
    public EquinoxUser getAuthor() {
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
