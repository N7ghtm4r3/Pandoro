package com.tecknobit.pandoro.services.notes.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser;
import com.tecknobit.pandoro.services.projects.models.ProjectUpdate;
import com.tecknobit.pandoro.services.users.models.PandoroUser;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
public class Note extends com.tecknobit.equinoxbackend.environment.models.EquinoxItem {

    /**
     * {@code NOTE_CONTENT_MAX_LENGTH} the max length of the content for a note
     */
    public static final int NOTE_CONTENT_MAX_LENGTH = 200;

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
    private final EquinoxUser author;

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
    private final com.tecknobit.equinoxbackend.environment.models.EquinoxUser markedAsDoneBy;

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
     * @param jNote: note details as {@link JSONObject}
     */
    public Note(JSONObject jNote) {
        super(jNote);
        author = PandoroUser.getInstance(hItem.getJSONObject(AUTHOR_KEY));
        content = hItem.getString(CONTENT_NOTE_KEY);
        creationDate = hItem.getLong(CREATION_DATE_KEY, -1);
        markedAsDone = hItem.getBoolean(MARKED_AS_DONE_KEY);
        markedAsDoneBy = PandoroUser.getInstance(hItem.getJSONObject(MARKED_AS_DONE_BY_KEY));
        markAsDoneDate = hItem.getLong(MARKED_AS_DONE_DATE_KEY, -1);
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
    public EquinoxUser getAuthor() {
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
     * Method to get an instance of this Telegram's type
     *
     * @param jItems: items details as {@link JSONArray}
     * @return instance as {@link ArrayList} of {@link Note}
     */
    @Returner
    public static ArrayList<Note> getInstances(JSONArray jItems) {
        ArrayList<Note> notes = new ArrayList<>();
        if (jItems != null) {
            for (int j = 0; j < jItems.length(); j++)
                notes.add(new Note(jItems.getJSONObject(j)));
        }
        return notes;
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItem: item details as {@link JSONObject}
     * @return instance as {@link Note}
     */
    @Returner
    public static Note getInstance(JSONObject jItem) {
        if (jItem == null)
            return null;
        else
            return new Note(jItem);
    }

}
