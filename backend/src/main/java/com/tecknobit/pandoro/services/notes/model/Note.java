package com.tecknobit.pandoro.services.notes.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.environment.models.EquinoxItem;
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser;
import com.tecknobit.pandoro.services.projects.models.ProjectUpdate;
import com.tecknobit.pandoro.services.users.models.PandoroUser;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
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
    @Column(
            name = CONTENT_NOTE_KEY,
            nullable = false
    )
    private final String content;

    /**
     * {@code creationDate} when the note has been created
     */
    @Column(name = CREATION_DATE_KEY)
    private final long creationDate;

    /**
     * {@code markedAsDone} whether the note is marked as done
     */
    @Column(name = MARKED_AS_DONE_KEY)
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
    @Column(name = MARKED_AS_DONE_DATE_KEY)
    private final long markAsDoneDate;

    /**
     * {@code project_update} the project update owner
     *
     * @apiNote usage in SQL scopes
     */
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = UPDATE_KEY, referencedColumnName = IDENTIFIER_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectUpdate project_update;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public Note() {
        this(null, null, null, -1, false, null, -1);
    }

    /**
     * Constructor to init a {@link Note} object
     *
     * @param id:               the identifier of the note
     * @param content:          the content of the note
     * @param creationDate:when the note has been created
     * @param markedAsDone:     whether the note is marked as done
     * @param markedAsDoneBy:   who marked the note as done
     * @param markAsDoneDate:{@code markedAsDoneDate} when the note has been marked as done
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
     * Method to get {@link #author} instance <br>
     * No-any params required
     *
     * @return {@link #author} instance as {@link PandoroUser}
     */
    public PandoroUser getAuthor() {
        return author;
    }

    /**
     * Method to get {@link #content} instance <br>
     * No-any params required
     *
     * @return {@link #content} instance as {@link String}
     */
    @JsonGetter(CONTENT_NOTE_KEY)
    public String getContent() {
        return content;
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
     * Method to get {@link #markedAsDone} instance <br>
     * No-any params required
     *
     * @return {@link #markedAsDone} instance as boolean
     */
    @JsonGetter(MARKED_AS_DONE_KEY)
    public boolean isMarkedAsDone() {
        return markedAsDone;
    }

    /**
     * Method to get {@link #markedAsDoneBy} instance <br>
     * No-any params required
     *
     * @return {@link #markedAsDoneBy} instance as {@link PandoroUser}
     */
    @JsonGetter(MARKED_AS_DONE_BY_KEY)
    public EquinoxUser getMarkedAsDoneBy() {
        return markedAsDoneBy;
    }

    /**
     * Method to get {@link #markAsDoneDate} instance <br>
     * No-any params required
     *
     * @return {@link #markAsDoneDate} instance as long
     */
    @JsonGetter(MARKED_AS_DONE_DATE_KEY)
    public long getMarkAsDoneDate() {
        return markAsDoneDate;
    }

}
