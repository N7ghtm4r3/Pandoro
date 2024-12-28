package com.tecknobit.pandoro.services.projects.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.environment.models.EquinoxItem;
import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandorocore.enums.UpdateStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.*;

/**
 * The {@code ProjectUpdate} class is useful to create a <b>Pandoro's update</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 */
@Entity
@Table(name = UPDATES_KEY)
public class ProjectUpdate extends EquinoxItem {

    /**
     * {@code author} the author of the update
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
    private final PandoroUser author;

    /**
     * {@code createDate} when the update has been created
     */
    @Column(name = UPDATE_CREATE_DATE_KEY)
    private final long createDate;

    /**
     * {@code targetVersion} the target project_version of the update
     */
    @Column(name = UPDATE_TARGET_VERSION_KEY)
    private final String targetVersion;

    /**
     * {@code status} the status of the update
     */
    @Enumerated(EnumType.STRING)
    @Column(name = UPDATE_STATUS_KEY)
    private final UpdateStatus status;

    /**
     * {@code startedBy} who created the update
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = UPDATE_STARTED_BY_KEY)
    @JsonIgnoreProperties({
            TOKEN_KEY,
            PASSWORD_KEY,
            CHANGELOGS_KEY,
            GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    private final PandoroUser startedBy;

    /**
     * {@code startDate} when the update has been started
     */
    @Column(name = UPDATE_START_DATE_KEY)
    private final long startDate;

    /**
     * {@code publishedBy} who published the update
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = UPDATE_PUBLISHED_BY_KEY)
    @JsonIgnoreProperties({
            TOKEN_KEY,
            PASSWORD_KEY,
            CHANGELOGS_KEY,
            GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    private final PandoroUser publishedBy;

    /**
     * {@code publishDate} when the update has been published
     */
    @Column(name = UPDATE_PUBLISH_DATE_KEY)
    private final long publishDate;

    /**
     * {@code developmentDuration} how many days have been required to publish the update
     */
    @Transient
    private final int developmentDuration;

    /**
     * {@code notes} the notes for the update to be done
     */
    @OneToMany(
            mappedBy = UPDATE_KEY,
            cascade = CascadeType.ALL
    )
    private final List<Note> notes;

    /**
     * {@code project} the project owner
     *
     * @apiNote usage in SQL scopes
     */
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = PROJECT_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public ProjectUpdate() {
        this(null, null, -1, null, null, -1, null, -1, null);
    }

    /**
     * Constructor to init a {@link ProjectUpdate} object
     *
     * @param id:            identifier of the update
     * @param author:        the author of the update
     * @param createDate:    when the update has been created
     * @param targetVersion:{@code targetVersion} the target project_version of the update
     * @param startedBy:     who created the update
     * @param startDate:     when the update has been started
     * @param publishedBy:   who published the update
     * @param publishDate:   when the update has been published
     * @param notes:         the notes for the update to be done
     */
    public ProjectUpdate(String id, PandoroUser author, long createDate, String targetVersion, PandoroUser startedBy,
                         long startDate, PandoroUser publishedBy, long publishDate, ArrayList<Note> notes) {
        super(id);
        this.author = author;
        this.createDate = createDate;
        this.targetVersion = targetVersion;
        this.startedBy = startedBy;
        this.startDate = startDate;
        this.publishedBy = publishedBy;
        this.publishDate = publishDate;
        if (publishDate != -1) {
            developmentDuration = (int) Math.ceil(((publishDate - startDate) / 86400f) / 1000);
            status = PUBLISHED;
        } else if (startDate == -1) {
            status = SCHEDULED;
            developmentDuration = 0;
        } else {
            developmentDuration = 0;
            status = IN_DEVELOPMENT;
        }
        this.notes = notes;
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
     * Method to get {@link #createDate} instance
     *
     * @return {@link #createDate} instance as long
     */
    @JsonGetter(UPDATE_CREATE_DATE_KEY)
    public long getCreateTimestamp() {
        return createDate;
    }

    /**
     * Method to get {@link #targetVersion} instance
     *
     * @return {@link #targetVersion} instance as {@link String}
     */
    @JsonGetter(UPDATE_TARGET_VERSION_KEY)
    public String getTargetVersion() {
        return targetVersion;
    }

    /**
     * Method to get {@link #startedBy} instance
     *
     * @return {@link #startedBy} instance as {@link PandoroUser}
     */
    @JsonGetter(UPDATE_STARTED_BY_KEY)
    public PandoroUser getStartedBy() {
        return startedBy;
    }

    /**
     * Method to get {@link #startDate} instance
     *
     * @return {@link #startDate} instance as long
     */
    @JsonGetter(UPDATE_START_DATE_KEY)
    public long getStartTimestamp() {
        return startDate;
    }

    /**
     * Method to get {@link #publishedBy} instance
     *
     * @return {@link #publishedBy} instance as {@link PandoroUser}
     */
    @JsonGetter(UPDATE_PUBLISHED_BY_KEY)
    public PandoroUser getPublishedBy() {
        return publishedBy;
    }

    /**
     * Method to get {@link #publishDate} instance
     *
     * @return {@link #publishDate} instance as long
     */
    @JsonGetter(UPDATE_PUBLISH_DATE_KEY)
    public long getPublishTimestamp() {
        return publishDate;
    }

    /**
     * Method to get {@link #developmentDuration} instance
     *
     * @return {@link #developmentDuration} instance as int
     */
    @JsonIgnore
    public int getDevelopmentDuration() {
        return developmentDuration;
    }

    /**
     * Method to get {@link #notes} instance
     *
     * @return {@link #notes} instance as {@link ArrayList} of {@link Note}
     */
    public ArrayList<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    /**
     * Method to get {@link #status} instance
     *
     * @return {@link #status} instance as {@link UpdateStatus}
     */
    public UpdateStatus getStatus() {
        return status;
    }

}
