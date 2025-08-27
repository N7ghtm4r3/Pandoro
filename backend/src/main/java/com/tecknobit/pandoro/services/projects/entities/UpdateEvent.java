package com.tecknobit.pandoro.services.projects.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.annotations.EmptyConstructor;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandorocore.enums.events.UpdateEventType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

/**
 * The {@code UpdateEvent} class represents an update occurred during the lifecycle of that update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 * @since 1.2.0
 */
@Entity
@Table(name = UPDATE_EVENTS_KEY)
public class UpdateEvent extends EquinoxItem {

    /**
     * {@code owner} represents the update where the event occurred
     */
    @ManyToOne
    @JoinColumn(name = OWNER_KEY)
    @JsonIgnoreProperties({
            EVENTS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    @OnDelete(action = CASCADE)
    private final Update owner;

    /**
     * {@code type} the type of the occurred event
     */
    @Enumerated(value = EnumType.STRING)
    private final UpdateEventType type;

    /**
     * {@code author} the user who made the action which created the event
     */
    @ManyToOne
    @JoinColumn(name = AUTHOR_KEY)
    @JsonIgnoreProperties({
            TOKEN_KEY,
            PASSWORD_KEY,
            LANGUAGE_KEY,
            CHANGELOGS_KEY,
            GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            EVENTS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    private final PandoroUser author;

    /**
     * {@code timestamp} when the event occurred
     */
    @Column(name = TIMESTAMP_KEY)
    private final long timestamp;

    /**
     * {@code noteContent} the content of the note if it is an event related to a change note
     */
    @Column(name = CONTENT_NOTE_KEY)
    private final String noteContent;

    /**
     * {@code extraContent} extra content used when it is necessary add extra information such update version, etc...
     */
    @Column(name = EXTRA_CONTENT_KEY)
    private final String extraContent;

    /**
     * Constructor used to init the entity
     */
    @EmptyConstructor
    public UpdateEvent() {
        this(null, null, null, null, -1, null, null);
    }

    /**
     * Constructor used to init the entity
     *
     * @param id The identifier of the event
     * @param owner Represents the update where the event occurred
     * @param type The type of the occurred event
     * @param author The user who made the action which created the event
     * @param timestamp When the event occurred
     * @param noteContent The content of the note if it is an event related to a change note
     * @param extraContent Extra content used when it is necessary add extra information such update version, etc...
     */
    public UpdateEvent(String id, Update owner, UpdateEventType type, PandoroUser author, long timestamp, String noteContent,
                       String extraContent) {
        super(id);
        this.owner = owner;
        this.type = type;
        this.author = author;
        this.timestamp = timestamp;
        this.noteContent = noteContent;
        this.extraContent = extraContent;
    }

    /**
     * Method to get {@link #owner} instance
     *
     * @return {@link #owner} instance as {@link PandoroUser}
     */
    public Update getOwner() {
        return owner;
    }

    /**
     * Method to get {@link #type} instance
     *
     * @return {@link #type} instance as {@link UpdateEventType}
     */
    public UpdateEventType getType() {
        return type;
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
     * Method to get {@link #timestamp} instance
     *
     * @return {@link #timestamp} instance as {@code long}
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Method to get {@link #noteContent} instance
     *
     * @return {@link #noteContent} instance as {@link String}
     */
    @JsonGetter(CONTENT_NOTE_KEY)
    public String getNoteContent() {
        return noteContent;
    }

    /**
     * Method to get {@link #extraContent} instance
     *
     * @return {@link #extraContent} instance as {@link String}
     */
    @JsonGetter(EXTRA_CONTENT_KEY)
    public String getExtraContent() {
        return extraContent;
    }

}
