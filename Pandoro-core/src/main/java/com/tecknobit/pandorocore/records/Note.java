package com.tecknobit.pandorocore.records;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.pandorocore.records.structures.PandoroItem;
import com.tecknobit.pandorocore.records.structures.PandoroItemStructure;
import com.tecknobit.pandorocore.records.users.PublicUser;
import com.tecknobit.pandorocore.records.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.tecknobit.pandorocore.records.Note.NOTES_KEY;
import static com.tecknobit.pandorocore.records.Project.PROJECTS_KEY;
import static com.tecknobit.pandorocore.records.Project.UPDATE_KEY;
import static com.tecknobit.pandorocore.records.users.User.LANGUAGE_KEY;

/**
 * The {@code Note} class is useful to create a <b>Pandoro's note</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItemStructure
 * @see Serializable
 */
@Entity
@Table(name = NOTES_KEY)
public class Note extends PandoroItemStructure {

    /**
     * {@code NOTES_KEY} notes key
     */
    public static final String NOTES_KEY = "notes";

    /**
     * {@code NOTE_IDENTIFIER_KEY} the note identifier key
     */
    public static final String NOTE_IDENTIFIER_KEY = "note_id";

    /**
     * {@code CONTENT_NOTE_KEY} the content of the note key
     */
    public static final String CONTENT_NOTE_KEY = "content_note";

    /**
     * {@code MARKED_AS_DONE_KEY} mark as done key
     */
    public static final String MARKED_AS_DONE_KEY = "marked_as_done";

    /**
     * {@code MARKED_AS_DONE_BY_KEY} mark as done author key
     */
    public static final String MARKED_AS_DONE_BY_KEY = "marked_as_done_by";

    /**
     * {@code MARKED_AS_DONE_DATE_KEY} marked as done date key
     */
    public static final String MARKED_AS_DONE_DATE_KEY = "marked_as_done_date";

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
    @JoinColumn(name = PandoroItem.AUTHOR_KEY)
    @JsonIgnoreProperties({
            PublicUser.TOKEN_KEY,
            PublicUser.PASSWORD_KEY,
            LANGUAGE_KEY,
            PublicUser.COMPLETE_NAME_KEY,
            Changelog.CHANGELOGS_KEY,
            Group.GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            PublicUser.UNREAD_CHANGELOGS_KEY,
            PublicUser.ADMIN_GROUPS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
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
    @Column(name = PandoroItem.CREATION_DATE_KEY)
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
            PublicUser.TOKEN_KEY,
            PublicUser.PASSWORD_KEY,
            PublicUser.COMPLETE_NAME_KEY,
            Changelog.CHANGELOGS_KEY,
            Group.GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            PublicUser.UNREAD_CHANGELOGS_KEY,
            PublicUser.ADMIN_GROUPS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final PublicUser markedAsDoneBy;

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
    @JoinColumn(name = UPDATE_KEY, referencedColumnName = PandoroItem.IDENTIFIER_KEY)
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
        id = hItem.getString(PandoroItem.IDENTIFIER_KEY);
        author = User.getInstance(hItem.getJSONObject(PandoroItem.AUTHOR_KEY));
        content = hItem.getString(CONTENT_NOTE_KEY);
        creationDate = hItem.getLong(PandoroItem.CREATION_DATE_KEY, -1);
        markedAsDone = hItem.getBoolean(MARKED_AS_DONE_KEY);
        markedAsDoneBy = PublicUser.getInstance(hItem.getJSONObject(MARKED_AS_DONE_BY_KEY));
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
    public Note(String id, User author, String content, long creationDate, boolean markedAsDone,
                PublicUser markedAsDoneBy, long markAsDoneDate) {
        super(null);
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
    @JsonGetter(PandoroItem.CREATION_DATE_KEY)
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
     * @return {@link #markedAsDoneBy} instance as {@link PublicUser}
     */
    @JsonGetter(MARKED_AS_DONE_BY_KEY)
    public PublicUser getMarkedAsDoneBy() {
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
