package com.tecknobit.pandoro.services.notes.repository;

import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.projects.entities.ProjectUpdate;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper._WHERE_;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code NotesRepository} interface is useful to manage the queries for the notes
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 * @see Note
 */
@Repository
public interface NotesRepository extends JpaRepository<Note, String> {

    /**
     * Method to execute the query to select the number of the notes
     *
     * @param authorId The author identifier
     * @return the number of the notes
     */
    @Query(
            value = "SELECT COUNT(*) FROM " + NOTES_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY +
                    " AND " + UPDATE_KEY + " IS NULL",
            nativeQuery = true
    )
    long getNotesCount(
            @Param(AUTHOR_KEY) String authorId
    );

    /**
     * Method to execute the query to select the list of a {@link Note}
     *
     * @param authorId The author identifier
     * @param pageable  The parameters to paginate the query
     * @return the list of notes as {@link List} of {@link Note}
     */
    @Query(
            value = "SELECT * FROM " + NOTES_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + UPDATE_KEY + " IS NULL ORDER BY " + CREATION_DATE_KEY + " DESC ",
            nativeQuery = true
    )
    List<Note> getNotes(
            @Param(AUTHOR_KEY) String authorId,
            Pageable pageable
    );

    /**
     * Method to execute the query to select the number of the notes
     *
     * @param authorId     The author identifier
     * @param markedAsDone Whether retrieve the notes marked as done or the not ones
     * @return the number of the notes
     */
    @Query(
            value = "SELECT COUNT(*) FROM " + NOTES_KEY +
                    " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY +
                    " AND " + MARKED_AS_DONE_KEY + "=:" + MARKED_AS_DONE_KEY +
                    " AND " + UPDATE_KEY + " IS NULL",
            nativeQuery = true
    )
    long getNotesCount(
            @Param(AUTHOR_KEY) String authorId,
            @Param(MARKED_AS_DONE_KEY) boolean markedAsDone
    );

    /**
     * Method to execute the query to select the list of a {@link Note}
     *
     * @param authorId     The author identifier
     * @param pageable     The parameters to paginate the query
     * @param markedAsDone Whether retrieve the notes marked as done or the not ones
     * @return the list of notes as {@link List} of {@link Note}
     */
    @Query(
            value = "SELECT * FROM " + NOTES_KEY +
                    " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY +
                    " AND " + UPDATE_KEY + " IS NULL" +
                    " AND " + MARKED_AS_DONE_KEY + "=:" + MARKED_AS_DONE_KEY +
                    " ORDER BY " + CREATION_DATE_KEY + " DESC ",
            nativeQuery = true
    )
    List<Note> getNotes(
            @Param(AUTHOR_KEY) String authorId,
            @Param(MARKED_AS_DONE_KEY) boolean markedAsDone,
            Pageable pageable
    );

    /**
     * Method to execute the query to select a {@link Note} by its id
     *
     * @param authorId The author identifier
     * @param noteId The note identifier
     * @return the note as {@link Note}
     */
    @Query(
            value = "SELECT * FROM " + NOTES_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    Note getNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(IDENTIFIER_KEY) String noteId
    );

    /**
     * Method to execute the query to select a {@link Note} of an {@link ProjectUpdate}
     *
     * @param updateId The update identifier
     * @param noteId The note identifier
     * @return the note as {@link Note}
     */
    @Query(
            value = "SELECT * FROM " + NOTES_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + UPDATE_KEY + "=:" + UPDATE_KEY,
            nativeQuery = true
    )
    Note getNoteByUpdate(
            @Param(UPDATE_KEY) String updateId,
            @Param(IDENTIFIER_KEY) String noteId
    );

    /**
     * Method to execute the query to create a new {@link Note}
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     * @param contentNote The content of the note
     * @param creationDate The creation date of the note
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + NOTES_KEY
                    + " (" + IDENTIFIER_KEY + ","
                    + AUTHOR_KEY + ","
                    + CONTENT_NOTE_KEY + ","
                    + CREATION_DATE_KEY + ","
                    + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + ","
                    + MARKED_AS_DONE_DATE_KEY + ")"
                    + "VALUES ("
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + AUTHOR_KEY + ","
                    + ":" + CONTENT_NOTE_KEY + ","
                    + ":" + CREATION_DATE_KEY + ","
                    + "false,"
                    + "NULL,"
                    + "-1"
                    + ")",
            nativeQuery = true
    )
    void createNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(IDENTIFIER_KEY) String noteId,
            @Param(CONTENT_NOTE_KEY) String contentNote,
            @Param(CREATION_DATE_KEY) long creationDate
    );

    /**
     * Method to execute the query to edit an existing {@link Note}
     *
     * @param authorId    The author of the note identifier
     * @param noteId      The note identifier
     * @param contentNote The content of the note
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET " +
                    CONTENT_NOTE_KEY + "=:" + CONTENT_NOTE_KEY +
                    " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY +
                    " AND " + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    void editNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(IDENTIFIER_KEY) String noteId,
            @Param(CONTENT_NOTE_KEY) String contentNote
    );

    /**
     * Method to execute the query to add a new change note
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     * @param contentNote The content of the note
     * @param creationDate The creation date of the note
     * @param updateId The update identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + NOTES_KEY
                    + " (" + IDENTIFIER_KEY + ","
                    + AUTHOR_KEY + ","
                    + CONTENT_NOTE_KEY + ","
                    + CREATION_DATE_KEY + ","
                    + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + ","
                    + MARKED_AS_DONE_DATE_KEY + ","
                    + UPDATE_KEY + ") "
                    + "VALUES ("
                    + ":" + IDENTIFIER_KEY + ","
                    + ":" + AUTHOR_KEY + ","
                    + ":" + CONTENT_NOTE_KEY + ","
                    + ":" + CREATION_DATE_KEY + ","
                    + "false,"
                    + "NULL,"
                    + "-1,"
                    + ":" + UPDATE_KEY
                    + ")",
            nativeQuery = true
    )
    void addChangeNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(IDENTIFIER_KEY) String noteId,
            @Param(CONTENT_NOTE_KEY) String contentNote,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(UPDATE_KEY) String updateId
    );

    /**
     * Method to execute the query to manage the status of a {@link Note}
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     * @param markedAsDone: whether the note has been marked as done
     * @param markedAsDoneDate The date when the note has been marked as done or -1 if not
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET "
                    + MARKED_AS_DONE_KEY + "=:" + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + "= NULL,"
                    + MARKED_AS_DONE_DATE_KEY + "=:" + MARKED_AS_DONE_DATE_KEY
                    + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY + " AND "
                    + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    void manageNoteStatus(
            @Param(AUTHOR_KEY) String authorId,
            @Param(IDENTIFIER_KEY) String noteId,
            @Param(MARKED_AS_DONE_KEY) boolean markedAsDone,
            @Param(MARKED_AS_DONE_DATE_KEY) long markedAsDoneDate
    );

    /**
     * Method to execute the query to manage the status of a change note
     *
     * @param updateId The update identifier
     * @param noteId The note identifier
     * @param markedAsDone: whether the change note has been marked as done
     * @param marker: who marks as done the change note
     * @param markedAsDoneDate The date when the change note has been marked as done or -1 if not
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET "
                    + MARKED_AS_DONE_KEY + "=:" + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + "=:" + MARKED_AS_DONE_BY_KEY + ","
                    + MARKED_AS_DONE_DATE_KEY + "=:" + MARKED_AS_DONE_DATE_KEY
                    + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY + " AND "
                    + UPDATE_KEY + "=:" + UPDATE_KEY,
            nativeQuery = true
    )
    void manageChangeNoteStatus(
            @Param(UPDATE_KEY) String updateId,
            @Param(IDENTIFIER_KEY) String noteId,
            @Param(MARKED_AS_DONE_KEY) boolean markedAsDone,
            @Param(MARKED_AS_DONE_BY_KEY) String marker,
            @Param(MARKED_AS_DONE_DATE_KEY) long markedAsDoneDate
    );

    /**
     * Method to execute the query to delete a {@link Note}
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + NOTES_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY +
                    " AND " + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    void deleteNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(IDENTIFIER_KEY) String noteId
    );

    // TODO: 25/08/2025 TO DOCUMENT
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET " +
                    UPDATE_KEY + "=:" + DESTINATION_UPDATE_IDENTIFIER_KEY +
                    _WHERE_ + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void moveChangeNote(
            @Param(IDENTIFIER_KEY) String noteId,
            @Param(DESTINATION_UPDATE_IDENTIFIER_KEY) String destinationUpdateId
    );

    /**
     * Method to execute the query to delete a change note
     *
     * @param updateId The update identifier
     * @param noteId The note identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + NOTES_KEY + " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY +
                    " AND " + UPDATE_KEY + "=:" + UPDATE_KEY,
            nativeQuery = true
    )
    void deleteChangeNote(
            @Param(UPDATE_KEY) String updateId,
            @Param(IDENTIFIER_KEY) String noteId
    );

    /**
     * Method to execute the query to remove the constraints between {@link PandoroUser} deleted and {@link Note}
     *
     * @param userId The user identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + NOTES_KEY + " WHERE " + AUTHOR_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + UPDATE_KEY + " IS NULL",
            nativeQuery = true
    )
    void removeUserConstraints(
            @Param(IDENTIFIER_KEY) String userId
    );

    /**
     * Method to execute the query to remove the constraints between {@link PandoroUser} deleted and the author of the
     * {@link Note}
     *
     * @param userId The user identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET "
                    + AUTHOR_KEY + "= NULL"
                    + " WHERE " + AUTHOR_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + UPDATE_KEY + " IS NOT NULL",
            nativeQuery = true
    )
    void setGroupNotesAuthorAfterUserDeletion(
            @Param(IDENTIFIER_KEY) String userId
    );

    /**
     * Method to execute the query to remove the constraints between {@link PandoroUser} deleted and the marker of the
     * {@link Note}
     *
     * @param userId The user identifier
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET "
                    + MARKED_AS_DONE_BY_KEY + "= NULL"
                    + " WHERE " + MARKED_AS_DONE_BY_KEY + "=:" + IDENTIFIER_KEY
                    + " AND " + UPDATE_KEY + " IS NOT NULL",
            nativeQuery = true
    )
    void setGroupNotesMarkerAfterUserDeletion(
            @Param(IDENTIFIER_KEY) String userId
    );

}
