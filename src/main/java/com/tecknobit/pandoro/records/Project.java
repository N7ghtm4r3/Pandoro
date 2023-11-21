package com.tecknobit.pandoro.records;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.pandoro.records.structures.PandoroItem;
import com.tecknobit.pandoro.records.structures.PandoroItemStructure;
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
import static com.tecknobit.pandoro.records.ProjectUpdate.Status.PUBLISHED;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.*;

/**
 * The {@code Project} class is useful to create a <b>Pandoro's project</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItemStructure
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = PROJECTS_KEY)
public class Project extends PandoroItem implements Serializable {

    /**
     * {@code RepositoryPlatform} list of available repository platforms
     */
    public enum RepositoryPlatform {

        /**
         * {@code Github} repository platform
         */
        Github,

        /**
         * {@code GitLab} repository platforms
         */
        GitLab;

        /**
         * Method to reach a platform value
         *
         * @param url: the url to fetch the platform
         * @return repository platform as {@link RepositoryPlatform}
         */
        public static RepositoryPlatform reachPlatform(String url) {
            if (isValidPlatform(url)) {
                if (url.contains(Github.name().toLowerCase()))
                    return Github;
                else
                    return GitLab;
            }
            return null;
        }

        /**
         * Method to check a repository platform validity
         *
         * @param url: the url to check the platform
         * @return whether the repository platform is valid as boolean
         */
        public static boolean isValidPlatform(String url) {
            for (RepositoryPlatform platform : RepositoryPlatform.values())
                if (url.contains(platform.name().toLowerCase()))
                    return true;
            return false;
        }

    }

    /**
     * {@code PROJECT_NAME_MAX_LENGTH} the max length of the name for a project
     */
    public static final int PROJECT_NAME_MAX_LENGTH = 15;

    /**
     * {@code PROJECT_SHORT_DESCRIPTION_MAX_LENGTH} the max length of the short description for a project
     */
    public static final int PROJECT_SHORT_DESCRIPTION_MAX_LENGTH = 15;

    /**
     * {@code PROJECT_DESCRIPTION_MAX_LENGTH} the max length of the description for a project
     */
    public static final int PROJECT_DESCRIPTION_MAX_LENGTH = 1500;

    /**
     * {@code author} author of the project
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final User author;

    /**
     * {@code shortDescription} short description of the project
     */
    @Column(name = PROJECT_SHORT_DESCRIPTION_KEY)
    private final String shortDescription;

    /**
     * {@code description} description of the project
     */
    @Column(name = PROJECT_DESCRIPTION_KEY)
    private final String description;

    /**
     * {@code version} last update version
     */
    @Column(name = PROJECT_VERSION_KEY)
    private final String version;

    /**
     * {@code lastUpdate} timestamp of the last update publish
     */
    @Transient
    private final long lastUpdate;

    /**
     * {@code updatesNumber} number of the updates of this project
     */
    @Transient
    private final int updatesNumber;

    /**
     * {@code groups} groups where the project has been assigned
     */
    @ManyToMany
    @JoinTable(
            name = PROJECTS_GROUPS_TABLE,
            joinColumns = {@JoinColumn(name = PROJECT_IDENTIFIER_KEY)},
            inverseJoinColumns = {@JoinColumn(name = GROUP_IDENTIFIER_KEY)}
    )
    @JsonIgnoreProperties(PROJECTS_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final List<Group> groups;

    /**
     * {@code updates} updates of the project
     */
    @OneToMany(
            mappedBy = PROJECT_KEY,
            cascade = CascadeType.ALL
    )
    private final List<ProjectUpdate> updates;

    /**
     * {@code projectRepo} the repository of the project
     */
    @Column(name = PROJECT_REPOSITORY_KEY)
    private final String projectRepo;

    /**
     * {@code repositoryPlatform} the platform of the repository of the project
     */
    @Transient
    private final RepositoryPlatform repositoryPlatform;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public Project() {
        this(null, null, null, null, null, null, null,
                new ArrayList<>(), "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param jProject: project details as {@link JSONObject}
     */
    public Project(JSONObject jProject) {
        super(jProject);
        author = User.getInstance(hItem.getJSONObject(AUTHOR_KEY));
        shortDescription = hItem.getString(PROJECT_SHORT_DESCRIPTION_KEY);
        description = hItem.getString(PROJECT_DESCRIPTION_KEY);
        version = hItem.getString(PROJECT_VERSION_KEY);
        groups = Group.getInstances(hItem.getJSONArray(GROUPS_KEY));
        updates = ProjectUpdate.getInstances(hItem.getJSONArray(UPDATES_KEY));
        updatesNumber = updates.size();
        if (updatesNumber > 0)
            lastUpdate = updates.get(updatesNumber - 1).getPublishTimestamp();
        else
            lastUpdate = -1;
        projectRepo = hItem.getString(PROJECT_REPOSITORY_KEY, "");
        if (!projectRepo.isEmpty())
            repositoryPlatform = RepositoryPlatform.reachPlatform(projectRepo);
        else
            repositoryPlatform = null;
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param author:           author of the project
     * @param shortDescription:{@code shortDescription} short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param groups:           groups where the project has been assigned
     * @param updates:          updates of the project
     * @param projectRepo:      the repository of the project
     */
    public Project(String id, String name, User author, String shortDescription, String description, String version,
                   ArrayList<Group> groups, ArrayList<ProjectUpdate> updates, String projectRepo) {
        super(id, name);
        this.author = author;
        this.shortDescription = shortDescription;
        this.description = description;
        this.version = version;
        updatesNumber = updates.size();
        if (updatesNumber > 0)
            lastUpdate = updates.get(updatesNumber - 1).getPublishTimestamp();
        else
            lastUpdate = -1;
        this.groups = groups;
        this.updates = updates;
        this.projectRepo = projectRepo;
        if (!projectRepo.isEmpty())
            repositoryPlatform = RepositoryPlatform.reachPlatform(projectRepo);
        else
            repositoryPlatform = null;
    }

    /**
     * Method to get {@link #author} instance <br>
     * No-any params required
     *
     * @return {@link #author} instance as {@link User}
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Method to get {@link #shortDescription} instance <br>
     * No-any params required
     *
     * @return {@link #shortDescription} instance as {@link String}
     */
    @JsonGetter(PROJECT_SHORT_DESCRIPTION_KEY)
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Method to get {@link #description} instance <br>
     * No-any params required
     *
     * @return {@link #description} instance as {@link String}
     */
    @JsonGetter(PROJECT_DESCRIPTION_KEY)
    public String getDescription() {
        return description;
    }

    /**
     * Method to get {@link #version} instance <br>
     * No-any params required
     *
     * @return {@link #version} instance as {@link String}
     */
    @JsonGetter(PROJECT_VERSION_KEY)
    public String getVersion() {
        return version;
    }

    /**
     * Method to get {@link #lastUpdate} instance <br>
     * No-any params required
     *
     * @return {@link #lastUpdate} instance as long
     */
    @JsonIgnore
    public long getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Method to get {@link #lastUpdate} instance <br>
     * No-any params required
     *
     * @return {@link #lastUpdate} instance as {@link String}
     */
    @JsonIgnore
    public String getLastUpdateDate() {
        if (lastUpdate == -1)
            return "no updates yet";
        return TimeFormatter.getStringDate(lastUpdate);
    }

    /**
     * Method to get {@link #updatesNumber} instance <br>
     * No-any params required
     *
     * @return {@link #updatesNumber} instance as int
     */
    @JsonIgnore
    public int getUpdatesNumber() {
        return updatesNumber;
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
     * Method to get {@link #updates} instance <br>
     * No-any params required
     *
     * @return {@link #updates} instance as {@link ArrayList} of {@link ProjectUpdate}
     */
    public ArrayList<ProjectUpdate> getUpdates() {
        return new ArrayList<>(updates);
    }

    /**
     * Method to get the published updates <br>
     * No-any params required
     *
     * @return published updates as {@link ArrayList} of {@link ProjectUpdate}
     */
    @JsonIgnore
    public ArrayList<ProjectUpdate> getPublishedUpdates() {
        ArrayList<ProjectUpdate> publishedUpdates = new ArrayList<>();
        for (ProjectUpdate update : updates)
            if (update.getStatus() == PUBLISHED)
                publishedUpdates.add(update);
        return publishedUpdates;
    }

    /**
     * Method to get the total development days for the project <br>
     * No-any params required
     *
     * @return the total development days for the project as int
     */
    @JsonIgnore
    public int getTotalDevelopmentDays() {
        int totalDevelopmentDays = 0;
        for (ProjectUpdate update : getPublishedUpdates())
            totalDevelopmentDays += update.getDevelopmentDuration();
        return totalDevelopmentDays;
    }

    /**
     * Method to get the average time for an update of the project <br>
     * No-any params required
     *
     * @return average time for an update of the project as int
     */
    @JsonIgnore
    public int getAverageDevelopmentTime() {
        int totalUpdateDays = getTotalDevelopmentDays();
        if (totalUpdateDays > 0)
            return (totalUpdateDays / (getPublishedUpdates().size()));
        else
            return 0;
    }

    /**
     * Method to get {@link #projectRepo} instance <br>
     * No-any params required
     *
     * @return {@link #projectRepo} instance as {@link String}
     */
    @JsonGetter(PROJECT_REPOSITORY_KEY)
    public String getProjectRepo() {
        return projectRepo;
    }

    /**
     * Method to get {@link #repositoryPlatform} instance <br>
     * No-any params required
     *
     * @return {@link #repositoryPlatform} instance as {@link RepositoryPlatform}
     */
    @JsonIgnore
    public RepositoryPlatform getRepositoryPlatform() {
        return repositoryPlatform;
    }

    /**
     * Method to whether the project is in any {@link Group} <br>
     * No-any params required
     *
     * @return whether the project is in any {@link Group} as boolean
     */
    @JsonIgnore
    public boolean hasGroups() {
        return !groups.isEmpty();
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItems: items details as {@link JSONArray}
     * @return instance as {@link ArrayList} of {@link Project}
     */
    @Returner
    public static ArrayList<Project> getInstances(JSONArray jItems) {
        ArrayList<Project> projects = new ArrayList<>();
        if (jItems != null) {
            for (int j = 0; j < jItems.length(); j++)
                projects.add(new Project(jItems.getJSONObject(j)));
        }
        return projects;
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItem: item details as {@link JSONObject}
     * @return instance as {@link Project}
     */
    @Returner
    public static Project getInstance(JSONObject jItem) {
        if (jItem == null)
            return null;
        else
            return new Project(jItem);
    }

}

