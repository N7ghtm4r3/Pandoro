package com.tecknobit.pandoro.services.projects.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.pandoro.services.PandoroItem;
import com.tecknobit.pandoro.services.groups.model.Group;
import com.tecknobit.pandoro.services.users.models.PandoroUser;
import com.tecknobit.pandorocore.enums.RepositoryPlatform;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED;

/**
 * The {@code Project} class is useful to create a <b>Pandoro's project</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxbackend.environment.models.EquinoxItem
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = PROJECTS_KEY)
public class Project extends PandoroItem {

    /**
     * {@code creationDate} when the project has been created
     */
    @Column(name = CREATION_DATE_KEY)
    private final long creationDate;

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
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = PROJECTS_GROUPS_TABLE,
            joinColumns = {@JoinColumn(name = PROJECT_IDENTIFIER_KEY)},
            inverseJoinColumns = {@JoinColumn(name = GROUP_IDENTIFIER_KEY)}
    )
    @JsonIgnoreProperties({
            PROJECTS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    private final List<Group> groups;

    /**
     * {@code updates} updates of the project
     */
    @OneToMany(
            mappedBy = PROJECT_KEY,
            cascade = CascadeType.ALL
    )
    @OrderBy(UPDATE_CREATE_DATE_KEY + " DESC")
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
        this(null, null, -1, null, null, null,
                null, null, new ArrayList<>(), "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id:               identifier of the project
     * @param name:             name of the project
     * @param creationDate: when the project has been created
     * @param author:           author of the project
     * @param shortDescription:{@code shortDescription} short description of the project
     * @param description:      description of the project
     * @param version:          last update version
     * @param groups:           groups where the project has been assigned
     * @param updates:          updates of the project
     * @param projectRepo:      the repository of the project
     */
    public Project(String id, String name, long creationDate, PandoroUser author, String shortDescription,
                   String description, String version, ArrayList<Group> groups, ArrayList<ProjectUpdate> updates,
                   String projectRepo) {
        super(id, name);
        this.creationDate = creationDate;
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
            repositoryPlatform = RepositoryPlatform.Companion.reachPlatform(projectRepo);
        else
            repositoryPlatform = null;
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

}

