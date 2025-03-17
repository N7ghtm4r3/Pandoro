package com.tecknobit.pandoro.services.changelogs.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import com.tecknobit.equinoxbackend.environment.services.users.entity.EquinoxUser;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandorocore.enums.ChangelogEvent;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code Changelog} class is useful to create a <b>Pandoro's changelog</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 * @see Serializable
 */
@Entity
@Table(name = CHANGELOGS_KEY)
public class Changelog extends EquinoxItem {

    /**
     * {@code changelogEvent} the value of the changelog event
     */
    @Enumerated(EnumType.STRING)
    @Column(name = CHANGELOG_EVENT_KEY)
    private final ChangelogEvent changelogEvent;

    /**
     * {@code timestamp} when the changelog event has been created
     */
    @Column(name = CHANGELOG_TIMESTAMP_KEY)
    private final long timestamp;

    /**
     * {@code project} the project of the changelog event
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = PROJECT_IDENTIFIER_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({
            "hibernateLazyInitializer",
            "handler"
    })
    private final Project project;

    /**
     * {@code group} the group of the changelog event
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = GROUP_IDENTIFIER_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({
            "hibernateLazyInitializer",
            "handler"
    })
    private final Group group;

    /**
     * {@code extraContent} extra content data of the changelog event
     */
    @Column(name = CHANGELOG_EXTRA_CONTENT_KEY)
    private final String extraContent;

    /**
     * {@code read} whether the changelog has been read
     */
    @Column(name = CHANGELOG_READ_KEY)
    private final boolean read;

    /**
     * {@code owner} the changelog owner
     *
     * @apiNote usage in SQL scopes
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = CHANGELOG_OWNER_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private EquinoxUser owner;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public Changelog() {
        this(null, null, -1, null, null, false);
    }

    /**
     * Constructor to init a {@link Changelog} object
     *
     * @param id             :           the identifier of the changelogEvent message
     * @param changelogEvent :       the value of the changelogEvent
     * @param timestamp      :    when the changelogEvent has been created
     * @param project        :      the project of the changelogEvent project
     * @param extraContent   : extra content data of the changelogEvent
     * @param read:           whether the changelog has been read
     */
    public Changelog(String id, ChangelogEvent changelogEvent, long timestamp, Project project,
                     String extraContent, boolean read) {
        super(id);
        this.changelogEvent = changelogEvent;
        this.timestamp = timestamp;
        this.project = project;
        this.extraContent = extraContent;
        this.read = read;
        group = null;
    }

    /**
     * Method to get {@link #changelogEvent} instance
     *
     * @return {@link #changelogEvent} instance as {@link ChangelogEvent}
     */
    @Enumerated(EnumType.STRING)
    @JsonGetter(CHANGELOG_EVENT_KEY)
    public ChangelogEvent getChangelogEvent() {
        return changelogEvent;
    }

    /**
     * Method to get {@link #timestamp} instance
     *
     * @return {@link #timestamp} instance as long
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Method to get {@link #project} instance
     *
     * @return {@link #project} instance as {@link Project}
     */
    public Project getProject() {
        return project;
    }

    /**
     * Method to get {@link #group} instance
     *
     * @return {@link #group} instance as {@link Group}
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Method to get {@link #extraContent} instance
     *
     * @return {@link #extraContent} instance as {@link String}
     */
    @JsonGetter(CHANGELOG_EXTRA_CONTENT_KEY)
    public String getExtraContent() {
        return extraContent;
    }

    /**
     * Method to get {@link #read} instance
     *
     * @return {@link #read} instance as boolean
     */
    @JsonGetter(CHANGELOG_READ_KEY)
    public boolean isRead() {
        return read;
    }

}
