package com.tecknobit.pandoro.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.pandoro.records.users.PublicUser;
import com.tecknobit.pandoro.records.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import java.io.Serializable;

import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.NotesHelper.*;
import static com.tecknobit.pandoro.services.ProjectsHelper.UPDATE_KEY;

/**
 * The {@code Note} class is useful to create a <b>Pandoro's note</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Serializable
 */
@Entity
@Table(name = NOTES_KEY)
public class Note implements Serializable {

    /**
     * {@code NOTE_CONTENT_MAX_LENGTH} the max length of the content for a note
     */
    public static final int NOTE_CONTENT_MAX_LENGTH = 200;

    /**
     * {@code id} the identifier of the note
     */
    @Id
    @Column(name = NOTE_IDENTIFIER_KEY)
    private final String id;

    /**
     * {@code author} the author of the note
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = AUTHOR_KEY)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final User author;

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final PublicUser markedAsDoneBy;

    /**
     * {@code markedAsDoneDate} when the note has been marked as done
     */
    @Column(name = MARKED_AS_DONE_DATE_KEY)
    private final long markAsDoneDate;

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
        this(null, null, -1);
    }

    /**
     * Constructor to init a {@link Note} object
     *
     * @param id:           the identifier of the note
     * @param content:      the content of the note
     * @param creationDate: when the note has been created
     */
    public Note(String id, String content, long creationDate) {
        this(id, null, content, creationDate, false, null, -1);
    }

    /**
     * Constructor to init a {@link Note} object
     *
     * @param id:               the identifier of the note
     * @param content:          the content of the note
     * @param creationDate:     when the note has been created
     * @param markedAsDone:     whether the note is marked as done
     * @param markAsDoneDate: when the note has been marked as done
     */
    public Note(String id, String content, long creationDate, boolean markedAsDone, long markAsDoneDate) {
        this(id, null, content, creationDate, markedAsDone, null, markAsDoneDate);
    }

    /**
     * Constructor to init a {@link Note} object
     *
     * @param id:           the identifier of the note
     * @param author:       the author of the note
     * @param content:      the content of the note
     * @param creationDate: when the note has hja not the
     */
    public Note(String id, User author, String content, long creationDate) {
        this(id, author, content, creationDate, false, null, -1);
    }

    /**
     * Constructor to init a {@link Note} object
     *
     * @param id:               the identifier of the note
     * @param content:          the content of the note
     * @param creationDate:when the note has been created
     * @param markedAsDone:     whether the note is marked as done
     * @param markedAsDoneBy:   who marked the note as done
     * @param markAsDoneDate: when the note has been marked as done
     */
    public Note(String id, User author, String content, long creationDate, boolean markedAsDone,
                PublicUser markedAsDoneBy, long markAsDoneDate) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.creationDate = creationDate;
        this.markedAsDone = markedAsDone;
        this.markedAsDoneBy = markedAsDoneBy;
        this.markAsDoneDate = markAsDoneDate;
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
     * Method to get {@link #content} instance <br>
     * No-any params required
     *
     * @return {@link #content} instance as {@link String}
     */
    public String getContent() {
        return content;
    }

    /**
     * Method to get {@link #creationDate} instance <br>
     * No-any params required
     *
     * @return {@link #creationDate} instance as long
     */
    public long getCreation() {
        return creationDate;
    }

    /**
     * Method to get {@link #creationDate} instance <br>
     * No-any params required
     *
     * @return {@link #creationDate} instance as {@link String}
     */
    @JsonIgnore
    public String getCreationDate() {
        return TimeFormatter.getStringDate(creationDate);
    }

    /**
     * Method to get {@link #markedAsDone} instance <br>
     * No-any params required
     *
     * @return {@link #markedAsDone} instance as boolean
     */
    public boolean isMarkedAsDone() {
        return markedAsDone;
    }

    /**
     * Method to get {@link #markedAsDoneBy} instance <br>
     * No-any params required
     *
     * @return {@link #markedAsDoneBy} instance as {@link PublicUser}
     */
    public PublicUser getMarkedAsDoneBy() {
        return markedAsDoneBy;
    }

    /**
     * Method to get {@link #markAsDoneDate} instance <br>
     * No-any params required
     *
     * @return {@link #markAsDoneDate} instance as long
     */
    public long getMarkAsDoneDate() {
        return markAsDoneDate;
    }

    /**
     * Method to get {@link #markAsDoneDate} instance <br>
     * No-any params required
     *
     * @return {@link #markAsDoneDate} instance as {@link String}
     */
    @JsonIgnore
    public String getMarkedAsDoneDate() {
        if (markAsDoneDate == -1)
            return "not marked as done yet";
        return TimeFormatter.getStringDate(markAsDoneDate);
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
