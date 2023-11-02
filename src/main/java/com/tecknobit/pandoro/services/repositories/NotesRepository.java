package com.tecknobit.pandoro.services.repositories;

import com.tecknobit.pandoro.records.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.NotesHelper.*;

@Service
public interface NotesRepository extends JpaRepository<Note, String> {

    @Query(
            value = "SELECT * FROM " + NOTES_KEY + " WHERE id=:id",
            nativeQuery = true
    )
    List<Note> getNotes(@Param(IDENTIFIER_KEY) String id);

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
                    + ":id,"
                    + ":authorId,"
                    + ":contentNote,"
                    + ":creationDate,"
                    + "NULL,"
                    + "NULL,"
                    + "-1"
                    + ")",
            nativeQuery = true
    )
    void createNote(
            @Param(AUTHOR_KEY) String authorId,
            @Param(IDENTIFIER_KEY) String id,
            @Param(CONTENT_NOTE_KEY) String contentNote,
            @Param(CREATION_DATE_KEY) long creationDate
    );
}
