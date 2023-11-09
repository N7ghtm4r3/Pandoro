package com.tecknobit.pandoro.records.updates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.pandoro.records.Note;
import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.records.users.PublicUser;
import com.tecknobit.pandoro.records.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.*;

/**
 * The {@code ProjectUpdate} class is useful to create a <b>Pandoro's update</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Serializable
 */
@Entity
@Table(name = UPDATES_KEY)
@IdClass(UpdateCompositeKey.class)
public class ProjectUpdate implements Serializable {

    /**
     * {@code TARGET_VERSION_MAX_LENGTH} the max length of the target version for an update
     */
    public static final int TARGET_VERSION_MAX_LENGTH = 10;

    /**
     * {@code Status} list of available statuses for an update
     */
    public enum Status {

        /**
         * {@code SCHEDULED} status for an update
         */
        SCHEDULED,

        /**
         * {@code IN_DEVELOPMENT} status for an update
         */
        IN_DEVELOPMENT,

        /**
         * {@code PUBLISHED} status for an update
         */
        PUBLISHED

    }

    /**
     * {@code id} identifier of the update
     */
    @Id
    @Column(name = IDENTIFIER_KEY)
    private final String id;

    /**
     * {@code author} the author of the update
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = AUTHOR_KEY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final User author;

    /**
     * {@code createDate} when the update has been created
     */
    @Column(name = UPDATE_CREATE_DATE_KEY)
    private final long createDate;

    /**
     * {@code targetVersion} the target version of the update
     */
    @Id
    @Column(name = UPDATE_TARGET_VERSION_KEY)
    private final String targetVersion;

    /**
     * {@code status} the status of the update
     */
    @Enumerated(EnumType.STRING)
    @Column(name = UPDATE_STATUS_KEY)
    private final Status status;

    /**
     * {@code startedBy} who created the update
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = UPDATE_STARTED_BY_KEY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final PublicUser startedBy;

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final PublicUser publishedBy;

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

    @Id
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
     * @param createDate:    when the update has been created
     * @param targetVersion: the target version of the update
     * @param notes:         the notes for the update to be done
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public ProjectUpdate(String id, long createDate, String targetVersion, ArrayList<Note> notes) {
        this(id, null, createDate, targetVersion, null, -1, null, -1, notes);
    }

    /**
     * Constructor to init a {@link ProjectUpdate} object
     *
     * @param id:            identifier of the update
     * @param author:        the author of the update
     * @param createDate:    when the update has been created
     * @param targetVersion: the target version of the update
     * @param notes:         the notes for the update to be done
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public ProjectUpdate(String id, User author, long createDate, String targetVersion, ArrayList<Note> notes) {
        this(id, author, createDate, targetVersion, null, -1, null, -1, notes);
    }

    /**
     * Constructor to init a {@link ProjectUpdate} object
     *
     * @param id:            identifier of the update
     * @param createDate:    when the update has been created
     * @param targetVersion: the target version of the update
     * @param startDate:     when the update has been started
     * @param notes:         the notes for the update to be done
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public ProjectUpdate(String id, long createDate, String targetVersion, long startDate, ArrayList<Note> notes) {
        this(id, null, createDate, targetVersion, null, startDate, null, -1, notes);
    }

    /**
     * Constructor to init a {@link ProjectUpdate} object
     *
     * @param id:            identifier of the update
     * @param author:        the author of the update
     * @param createDate:    when the update has been created
     * @param targetVersion: the target version of the update
     * @param startedBy:     who created the update
     * @param startDate:     when the update has been started
     * @param notes:         the notes for the update to be doneea tee
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public ProjectUpdate(String id, User author, long createDate, String targetVersion, User startedBy, long startDate,
                         ArrayList<Note> notes) {
        this(id, author, createDate, targetVersion, startedBy, startDate, null, -1, notes);
    }

    /**
     * Constructor to init a {@link ProjectUpdate} object
     *
     * @param id:            identifier of the update
     * @param createDate:    when the update has been created
     * @param targetVersion: the target version of the update
     * @param startDate:     when the update has been started
     * @param publishDate:   when the update has been published
     * @param notes:         the notes for the update to be doneea tree
     */
    // TODO: 21/08/2023 CHECK TO REMOVE
    public ProjectUpdate(String id, long createDate, String targetVersion, long startDate, long publishDate,
                         ArrayList<Note> notes) {
        this(id, null, createDate, targetVersion, null, startDate, null, publishDate, notes);
    }

    /**
     * Constructor to init a {@link ProjectUpdate} object
     *
     * @param id:            identifier of the update
     * @param author:        the author of the update
     * @param createDate:    when the update has been created
     * @param targetVersion: the target version of the update
     * @param startedBy:     who created the update
     * @param startDate:     when the update has been started
     * @param publishedBy:   who published the update
     * @param publishDate:   when the update has been published
     * @param notes:         the notes for the update to be done
     */
    public ProjectUpdate(String id, User author, long createDate, String targetVersion, PublicUser startedBy, long startDate,
                         PublicUser publishedBy, long publishDate, ArrayList<Note> notes) {
        this.id = id;
        this.author = author;
        this.createDate = createDate;
        this.targetVersion = targetVersion;
        this.startedBy = startedBy;
        this.startDate = startDate;
        this.publishedBy = publishedBy;
        this.publishDate = publishDate;
        if (publishDate != -1) {
            developmentDuration = (int) Math.ceil(((publishDate - startDate) / 86400f) / 1000);
            status = Status.PUBLISHED;
        } else if (startDate == -1) {
            status = Status.SCHEDULED;
            developmentDuration = -1;
        } else {
            developmentDuration = -1;
            status = Status.IN_DEVELOPMENT;
        }
        this.notes = notes;
    }

    /**
     * Method to get {@link #id} instance <br>
     * No-any params required
     *
     * @return {@link #id} instance as {@link String}
     */
    public String getId() {
        return id;
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
     * Method to get {@link #createDate} instance <br>
     * No-any params required
     *
     * @return {@link #createDate} instance as long
     */
    public long getCreateTimestamp() {
        return createDate;
    }

    /**
     * Method to get {@link #createDate} instance <br>
     * No-any params required
     *
     * @return {@link #createDate} instance as {@link String}
     */
    @JsonIgnore
    public String getCreateDate() {
        return TimeFormatter.getStringDate(createDate);
    }

    /**
     * Method to get {@link #targetVersion} instance <br>
     * No-any params required
     *
     * @return {@link #targetVersion} instance as {@link String}
     */
    public String getTargetVersion() {
        return targetVersion;
    }

    /**
     * Method to get {@link #startedBy} instance <br>
     * No-any params required
     *
     * @return {@link #startedBy} instance as {@link User}
     */
    public PublicUser getStartedBy() {
        return startedBy;
    }

    /**
     * Method to get {@link #startDate} instance <br>
     * No-any params required
     *
     * @return {@link #startDate} instance as long
     */
    public long getStartTimestamp() {
        return startDate;
    }

    /**
     * Method to get {@link #startDate} instance <br>
     * No-any params required
     *
     * @return {@link #startDate} instance as {@link String}
     */
    @JsonIgnore
    public String getStartDate() {
        if (startDate == -1)
            return "not started yet";
        return TimeFormatter.getStringDate(startDate);
    }

    /**
     * Method to get {@link #publishedBy} instance <br>
     * No-any params required
     *
     * @return {@link #publishedBy} instance as {@link User}
     */
    public PublicUser getPublishedBy() {
        return publishedBy;
    }

    /**
     * Method to get {@link #publishDate} instance <br>
     * No-any params required
     *
     * @return {@link #publishDate} instance as long
     */
    public long getPublishTimestamp() {
        return publishDate;
    }

    /**
     * Method to get {@link #publishDate} instance <br>
     * No-any params required
     *
     * @return {@link #publishDate} instance as {@link String}
     */
    @JsonIgnore
    public String getPublishDate() {
        return TimeFormatter.getStringDate(publishDate);
    }

    /**
     * Method to get {@link #developmentDuration} instance <br>
     * No-any params required
     *
     * @return {@link #developmentDuration} instance as int
     */
    @JsonIgnore
    public int getDevelopmentDuration() {
        return developmentDuration;
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
     * Method to get {@link #status} instance <br>
     * No-any params required
     *
     * @return {@link #status} instance as {@link Status}
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns a string representation of the object <br>
     * No-any params required
     *
     * @return a string representation of the object as {@link String}
     */
    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
