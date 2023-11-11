package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.Note;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.AUTHOR_KEY;
import static com.tecknobit.pandoro.services.NotesHelper.*;
import static com.tecknobit.pandoro.services.ProjectsHelper.UPDATE_KEY;

@Service
@Repository
public interface NotesRepository extends JpaRepository<Note, String> {

    @Query(
            value = "SELECT * FROM " + NOTES_KEY + " WHERE " + AUTHOR_KEY + "=:" + AUTHOR_KEY
                    + " AND " + UPDATE_KEY + " = NULL"
                    + " ORDER BY " + CREATION_DATE_KEY + " DESC ",
            nativeQuery = true
    )
    List<Note> getNotes(@Param(AUTHOR_KEY) String authorId);

    @Query(
            value = "SELECT * FROM " + NOTES_KEY + " WHERE " + NOTE_IDENTIFIER_KEY + "=:" + NOTE_IDENTIFIER_KEY
                    + " AND " + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    Note getNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(NOTE_IDENTIFIER_KEY) String noteId
    );

    @Query(
            value = "SELECT * FROM " + NOTES_KEY + " WHERE " + NOTE_IDENTIFIER_KEY + "=:" + NOTE_IDENTIFIER_KEY
                    + " AND " + UPDATE_KEY + "=:" + UPDATE_KEY,
            nativeQuery = true
    )
    Note getNoteByUpdate(
            @Param(UPDATE_KEY) String updateId,
            @Param(NOTE_IDENTIFIER_KEY) String noteId
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + NOTES_KEY
                    + " (" + NOTE_IDENTIFIER_KEY + ","
                    + AUTHOR_KEY + ","
                    + CONTENT_NOTE_KEY + ","
                    + CREATION_DATE_KEY + ","
                    + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + ","
                    + MARKED_AS_DONE_DATE_KEY + ")"
                    + "VALUES ("
                    + ":" + NOTE_IDENTIFIER_KEY + ","
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
            @Param(NOTE_IDENTIFIER_KEY) String noteId,
            @Param(CONTENT_NOTE_KEY) String contentNote,
            @Param(CREATION_DATE_KEY) long creationDate
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "INSERT INTO " + NOTES_KEY
                    + " (" + NOTE_IDENTIFIER_KEY + ","
                    + AUTHOR_KEY + ","
                    + CONTENT_NOTE_KEY + ","
                    + CREATION_DATE_KEY + ","
                    + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + ","
                    + MARKED_AS_DONE_DATE_KEY + ","
                    + UPDATE_KEY + ") "
                    + "VALUES ("
                    + ":" + NOTE_IDENTIFIER_KEY + ","
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
            @Param(NOTE_IDENTIFIER_KEY) String noteId,
            @Param(CONTENT_NOTE_KEY) String contentNote,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(UPDATE_KEY) String updateId
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET "
                    + MARKED_AS_DONE_KEY + "=:" + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + "= NULL,"
                    + MARKED_AS_DONE_DATE_KEY + "=:" + MARKED_AS_DONE_DATE_KEY
                    + " WHERE " + NOTE_IDENTIFIER_KEY + "=:" + NOTE_IDENTIFIER_KEY + " AND "
                    + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    void manageNoteStatus(
            @Param(AUTHOR_KEY) String authorId,
            @Param(NOTE_IDENTIFIER_KEY) String noteId,
            @Param(MARKED_AS_DONE_KEY) boolean markedAsDone,
            @Param(MARKED_AS_DONE_DATE_KEY) long markedAsDoneDate
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE " + NOTES_KEY + " SET "
                    + MARKED_AS_DONE_KEY + "=:" + MARKED_AS_DONE_KEY + ","
                    + MARKED_AS_DONE_BY_KEY + "=:" + MARKED_AS_DONE_BY_KEY + ","
                    + MARKED_AS_DONE_DATE_KEY + "=:" + MARKED_AS_DONE_DATE_KEY
                    + " WHERE " + NOTE_IDENTIFIER_KEY + "=:" + NOTE_IDENTIFIER_KEY + " AND "
                    + UPDATE_KEY + "=:" + UPDATE_KEY,
            nativeQuery = true
    )
    void manageNoteStatus(
            @Param(UPDATE_KEY) String updateId,
            @Param(NOTE_IDENTIFIER_KEY) String noteId,
            @Param(MARKED_AS_DONE_KEY) boolean markedAsDone,
            @Param(MARKED_AS_DONE_BY_KEY) String marker,
            @Param(MARKED_AS_DONE_DATE_KEY) long markedAsDoneDate
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + NOTES_KEY + " WHERE " + NOTE_IDENTIFIER_KEY + "=:" + NOTE_IDENTIFIER_KEY
                    + " AND " + AUTHOR_KEY + "=:" + AUTHOR_KEY,
            nativeQuery = true
    )
    void deleteNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(NOTE_IDENTIFIER_KEY) String noteId
    );

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "DELETE FROM " + NOTES_KEY + " WHERE " + NOTE_IDENTIFIER_KEY + "=:" + NOTE_IDENTIFIER_KEY
                    + " AND " + UPDATE_KEY + "=:" + UPDATE_KEY,
            nativeQuery = true
    )
    void deleteChangeNote(
            @Param(UPDATE_KEY) String updateId,
            @Param(NOTE_IDENTIFIER_KEY) String noteId
    );

}
