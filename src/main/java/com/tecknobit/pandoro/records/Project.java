package com.tecknobit.pandoro.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.pandoro.records.updates.Update;
import com.tecknobit.pandoro.records.users.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.records.updates.Update.Status.PUBLISHED;
import static com.tecknobit.pandoro.services.ProjectsHelper.*;

/**
 * The {@code Project} class is useful to create a <b>Pandoro's project</b>
 *
 * @author N7ghtm4r3 - Tecknobit
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
    @ManyToMany(
            mappedBy = PROJECTS_KEY,
            cascade = CascadeType.ALL
    )
    private final List<Group> groups;

    /**
     * {@code updates} updates of the project
     */
    @OneToMany(
            mappedBy = PROJECT_KEY,
            cascade = CascadeType.ALL
    )
    private final List<Update> updates;

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
        this(null, null, null, null, null, null, null, null,
                null);
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param name: {@code name}
     */
    // TODO: 21/08/2023 TO REMOVE
    public Project(String name) {
        this("provaIdProj", name, "shortDescription", "description", "version",
                new ArrayList<>(), new ArrayList<>(), "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param projectRepo:      the repository of the project
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, String shortDescription, String description, String version, String projectRepo) {
        this(id, name, shortDescription, description, version, new ArrayList<>(), new ArrayList<>(), projectRepo);
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param updates:          updates of the project
     * @param projectRepo:      the repository of the project
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, String shortDescription, String description, String version,
                   ArrayList<Update> updates, String projectRepo) {
        this(id, name, shortDescription, description, version, new ArrayList<>(), updates, projectRepo);
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, String shortDescription, String description, String version) {
        this(id, name, shortDescription, description, version, new ArrayList<>(), new ArrayList<>(), "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param updates:          updates of the project
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, String shortDescription, String description, String version,
                   ArrayList<Update> updates) {
        this(id, name, shortDescription, description, version, updates, "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param groups:           groups where the project has been assigned
     * @param updates:          updates of the project
     * @param projectRepo:      the repository of the project
     */
    public Project(String id, String name, String shortDescription, String description, String version,
                   ArrayList<Group> groups, ArrayList<Update> updates, String projectRepo) {
        this(id, name, null, shortDescription, description, version, groups, updates, projectRepo);
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param author:           author of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param projectRepo:      the repository of the project
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, User author, String shortDescription, String description, String version,
                   String projectRepo) {
        this(id, name, author, shortDescription, description, version, new ArrayList<>(), new ArrayList<>(), projectRepo);
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param author:           author of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param updates:          updates of the project
     * @param projectRepo:      the repository of the project
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, User author, String shortDescription, String description, String version,
                   ArrayList<Update> updates, String projectRepo) {
        this(id, name, author, shortDescription, description, version, new ArrayList<>(), updates, projectRepo);
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param author:           author of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, User author, String shortDescription, String description, String version) {
        this(id, name, author, shortDescription, description, version, new ArrayList<>(), new ArrayList<>(), "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param author:           author of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param updates:          updates of the project
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public Project(String id, String name, User author, String shortDescription, String description, String version,
                   ArrayList<Update> updates) {
        this(id, name, author, shortDescription, description, version, updates, "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param author:           author of the project
     * @param shortDescription: short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param groups:           groups where the project has been assigned
     * @param updates:          updates of the project
     * @param projectRepo:      the repository of the project
     */
    public Project(String id, String name, User author, String shortDescription, String description, String version,
                   ArrayList<Group> groups, ArrayList<Update> updates, String projectRepo) {
        super(id, name);
        this.author = author;
        updatesNumber = updates.size();
        this.shortDescription = shortDescription;
        this.description = description;
        this.version = version;
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
    public String getShortDescription() {
        return shortDescription;
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
     * Method to get {@link #version} instance <br>
     * No-any params required
     *
     * @return {@link #version} instance as {@link String}
     */
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
     * @return {@link #updates} instance as {@link ArrayList} of {@link Update}
     */
    public ArrayList<Update> getUpdates() {
        return new ArrayList<>(updates);
    }

    /**
     * Method to get the published updates <br>
     * No-any params required
     *
     * @return published updates as {@link ArrayList} of {@link Update}
     */
    public ArrayList<Update> getPublishedUpdates() {
        ArrayList<Update> publishedUpdates = new ArrayList<>();
        for (Update update : updates)
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
    public int getTotalDevelopmentDays() {
        int totalDevelopmentDays = 0;
        for (Update update : getPublishedUpdates())
            totalDevelopmentDays += update.getDevelopmentDuration();
        return totalDevelopmentDays;
    }

    /**
     * Method to get the average time for an update of the project <br>
     * No-any params required
     *
     * @return average time for an update of the project as int
     */
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
    public boolean hasGroups() {
        return !groups.isEmpty();
    }

}

