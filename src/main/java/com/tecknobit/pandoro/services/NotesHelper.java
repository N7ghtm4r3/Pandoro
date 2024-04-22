package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Note;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * The {@code NotesHelper} class is useful to manage all the notes database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Service
public class NotesHelper {

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
     * {@code notesRepository} instance for the notes repository
     */
    @Autowired
    private NotesRepository notesRepository;

    /**
     * Method to get the user's notes list
     *
     * @param userId: the user identifier
     * @return the notes list as {@link List} of {@link Note}
     */
    public List<Note> getNotes(String userId) {
        return notesRepository.getNotes(userId);
    }

    /**
     * Method to create a new note
     *
     * @param authorId:    the author of the note identifier
     * @param noteId:      the note identifier
     * @param contentNote: the content of the note
     */
    public void createNote(String authorId, String noteId, String contentNote) {
        notesRepository.createNote(authorId, noteId, contentNote, currentTimeMillis());
    }

    /**
     * Method to check whether a note exists
     *
     * @param authorId: the author of the note identifier
     * @param noteId: the note identifier
     * @return whether a note exists as boolean
     */
    public boolean noteExists(String authorId, String noteId) {
        return notesRepository.getNote(authorId, noteId) != null;
    }

    /**
     * Method to mark a note as done
     *
     * @param authorId: the author of the note identifier
     * @param noteId: the note identifier
     */
    public void markAsDone(String authorId, String noteId) {
        notesRepository.manageNoteStatus(authorId, noteId, true, currentTimeMillis());
    }

    /**
     * Method to mark a note as todo
     *
     * @param authorId: the author of the note identifier
     * @param noteId: the note identifier
     */
    public void markAsToDo(String authorId, String noteId) {
        notesRepository.manageNoteStatus(authorId, noteId, false, -1);
    }

    /**
     * Method to delete a note
     *
     * @param authorId: the author of the note identifier
     * @param noteId: the note identifier
     */
    public void deleteNote(String authorId, String noteId) {
        notesRepository.deleteNote(authorId, noteId);
    }

}
