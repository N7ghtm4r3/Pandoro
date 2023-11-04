package com.tecknobit.pandoro.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.pandoro.records.Group.Member;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.io.Serializable;

import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.services.NotesHelper.*;

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
    private final PublicUser markedAsDoneBy;

    /**
     * {@code markedAsDoneDate} when the note has been marked as done
     */
    @Column(name = MARKED_AS_DONE_DATE_KEY)
    private final long markedAsDoneDate;

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
     * @param markedAsDoneDate: when the note has been marked as done
     */
    public Note(String id, String content, long creationDate, boolean markedAsDone, long markedAsDoneDate) {
        this(id, null, content, creationDate, markedAsDone, null, markedAsDoneDate);
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
     * @param markedAsDoneDate: when the note has been marked as done
     */
    public Note(String id, User author, String content, long creationDate, boolean markedAsDone,
                PublicUser markedAsDoneBy, long markedAsDoneDate) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.creationDate = creationDate;
        this.markedAsDone = markedAsDone;
        this.markedAsDoneBy = markedAsDoneBy;
        this.markedAsDoneDate = markedAsDoneDate;
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
     * @return {@link #markedAsDoneBy} instance as {@link Member}
     */
    public PublicUser getMarkedAsDoneBy() {
        return markedAsDoneBy;
    }

    /**
     * Method to get {@link #markedAsDoneDate} instance <br>
     * No-any params required
     *
     * @return {@link #markedAsDoneDate} instance as long
     */
    public long getMarkedAsDone() {
        return markedAsDoneDate;
    }

    /**
     * Method to get {@link #markedAsDoneDate} instance <br>
     * No-any params required
     *
     * @return {@link #markedAsDoneDate} instance as {@link String}
     */
    public String getMarkedAsDoneDate() {
        if (markedAsDoneDate == -1)
            return "not marked as done yet";
        return TimeFormatter.getStringDate(markedAsDoneDate);
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
