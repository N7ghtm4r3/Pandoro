package com.tecknobit.pandoro.services.projects.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.environment.models.EquinoxItem;
import com.tecknobit.pandoro.services.PandoroItem;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.NAME_KEY;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED;

/**
 * The {@code Project} class is useful to create a <b>Pandoro's project</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(
        name = PROJECTS_KEY,
        uniqueConstraints = @UniqueConstraint(columnNames = NAME_KEY)
)
public class Project extends PandoroItem {

    /**
     * {@code icon} the icon of the project
     */
    @Column(
            name = PROJECT_ICON_KEY,
            columnDefinition = "TEXT DEFAULT NULL",
            insertable = false
    )
    protected final String icon;

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
     * {@code description} description of the project
     */
    @Lob
    @Column(
            name = PROJECT_DESCRIPTION_KEY,
            columnDefinition = "MEDIUMTEXT",
            nullable = false
    )
    private final String description;

    /**
     * {@code project_version} last update project_version
     */
    @Column(name = PROJECT_VERSION_KEY)
    private final String version;

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
    private List<ProjectUpdate> updates;

    /**
     * {@code projectRepo} the project_repository of the project
     */
    @Column(name = PROJECT_REPOSITORY_KEY)
    private final String projectRepo;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public Project() {
        this(null, null, null, -1, null, null, null, null,
                new ArrayList<>(), "");
    }

    /**
     * Constructor to init a {@link Project} object
     *
     * @param id              identifier of the project
     * @param name             name of the project
     * @param icon the icon of the project
     * @param creationDate when the project has been created
     * @param author       author of the project
     * @param description     description of the project
     * @param version          last update project_version
     * @param groups           groups where the project has been assigned
     * @param updates          updates of the project
     * @param projectRepo      the project_repository of the project
     */
    public Project(String id, String name, String icon, long creationDate, PandoroUser author, String description, String version,
                   ArrayList<Group> groups, ArrayList<ProjectUpdate> updates, String projectRepo) {
        super(id, name);
        this.icon = icon;
        this.creationDate = creationDate;
        this.author = author;
        this.description = description;
        this.version = version;
        this.groups = groups;
        this.updates = updates;
        this.projectRepo = projectRepo;
    }

    /**
     * Method to get {@link #icon} instance
     *
     * @return {@link #icon} instance as {@link String}
     */
    public String getIcon() {
        return icon;
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
    @JsonGetter(PROJECT_DESCRIPTION_KEY)
    public String getDescription() {
        return description;
    }

    /**
     * Method to get {@link #version} instance
     *
     * @return {@link #version} instance as {@link String}
     */
    @JsonGetter(PROJECT_VERSION_KEY)
    public String getVersion() {
        return version;
    }

    /**
     * Method to get {@link #groups} instance
     *
     * @return {@link #groups} instance as {@link ArrayList} of {@link Group}
     */
    public ArrayList<Group> getGroups() {
        return new ArrayList<>(groups);
    }

    /**
     * Method to set the {@link #updates} instance
     *
     * @param updates The updates list to set
     */
    public void setUpdates(List<ProjectUpdate> updates) {
        this.updates = updates;
    }

    /**
     * Method to get {@link #updates} instance
     *
     * @return {@link #updates} instance as {@link ArrayList} of {@link ProjectUpdate}
     */
    public ArrayList<ProjectUpdate> getUpdates() {
        return new ArrayList<>(updates);
    }

    /**
     * Method to get the published updates
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
     * Method to get the total development days for the project
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
     * Method to get the average time for an update of the project
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
     * Method to get {@link #projectRepo} instance
     *
     * @return {@link #projectRepo} instance as {@link String}
     */
    @JsonGetter(PROJECT_REPOSITORY_KEY)
    public String getProjectRepo() {
        return projectRepo;
    }

    /**
     * Method to whether the project is in any {@link Group}
     *
     * @return whether the project is in any {@link Group} as boolean
     */
    @JsonIgnore
    public boolean hasGroups() {
        return !groups.isEmpty();
    }

}

