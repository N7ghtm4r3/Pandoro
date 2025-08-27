package com.tecknobit.pandoro.services.notes.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.annotations.EmptyConstructor;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code Note} class is useful to create a <b>Pandoro's note</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 */
@Entity
@Table(name = NOTES_KEY)
public class Note extends EquinoxItem {

    /**
     * {@code author} the author of the note
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
     * {@code content} the content of the note
     */
    @Lob
    @Column(
            name = CONTENT_NOTE_KEY,
            columnDefinition = "MEDIUMTEXT",
            nullable = false
    )
    private String content;

    /**
     * {@code creationDate} when the note has been created
     */
    @Column(name = CREATION_DATE_KEY)
    private final long creationDate;

    /**
     * {@code markedAsDone} whether the note is marked as done
     */
    @Column(
            name = MARKED_AS_DONE_KEY,
            columnDefinition = "BOOL DEFAULT 0",
            insertable = false
    )
    private final boolean markedAsDone;

    /**
     * {@code markedAsDoneBy} who marked the note as done
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = MARKED_AS_DONE_BY_KEY)
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final PandoroUser markedAsDoneBy;

    /**
     * {@code markedAsDoneDate} when the note has been marked as done
     */
    @Column(
            name = MARKED_AS_DONE_DATE_KEY,
            columnDefinition = "BIGINT DEFAULT -1",
            insertable = false
    )
    private final long markAsDoneDate;

    /**
     * {@code project_update} the project update owner
     *
     * @apiNote usage in SQL scopes
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = UPDATE_ESCAPED_KEY, referencedColumnName = IDENTIFIER_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Update update;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    @EmptyConstructor
    public Note() {
        this(null, null, null, -1, false, null, -1);
    }

    // TODO: 26/08/2025 TO DOCU 1.2.0
    public Note(String id, PandoroUser author, String content, long creationDate, Update update) {
        this(id, author, content, creationDate, false, null, -1);
        this.update = update;
    }

    /**
     * Constructor to init a {@link Note} object
     *
     * @param id The identifier of the note
     * @param content The content of the note
     * @param creationDate When the note has been created
     * @param markedAsDone Whether the note is marked as done
     * @param markedAsDoneBy Who marked the note as done
     * @param markAsDoneDate When the note has been marked as done
     */
    public Note(String id, PandoroUser author, String content, long creationDate, boolean markedAsDone,
                PandoroUser markedAsDoneBy, long markAsDoneDate) {
        super(id);
        this.author = author;
        this.content = content;
        this.creationDate = creationDate;
        this.markedAsDone = markedAsDone;
        this.markedAsDoneBy = markedAsDoneBy;
        this.markAsDoneDate = markAsDoneDate;
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
     * Method used to the {@link #content} value
     *
     * @param content The content of the note
     * @since 1.2.0
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Method to get {@link #content} instance
     *
     * @return {@link #content} instance as {@link String}
     */
    @JsonGetter(CONTENT_NOTE_KEY)
    public String getContent() {
        return content;
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
     * Method to get {@link #markedAsDone} instance
     *
     * @return {@link #markedAsDone} instance as boolean
     */
    @JsonGetter(MARKED_AS_DONE_KEY)
    public boolean isMarkedAsDone() {
        return markedAsDone;
    }

    /**
     * Method to get {@link #markedAsDoneBy} instance
     *
     * @return {@link #markedAsDoneBy} instance as {@link PandoroUser}
     */
    @JsonGetter(MARKED_AS_DONE_BY_KEY)
    public PandoroUser getMarkedAsDoneBy() {
        return markedAsDoneBy;
    }

    /**
     * Method to get {@link #markAsDoneDate} instance
     *
     * @return {@link #markAsDoneDate} instance as long
     */
    @JsonGetter(MARKED_AS_DONE_DATE_KEY)
    public long getMarkAsDoneDate() {
        return markAsDoneDate;
    }

}
