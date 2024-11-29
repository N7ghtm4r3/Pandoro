package com.tecknobit.pandoro.services.notes.service;

import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.notes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     * {@code notesRepository} instance for the notes repository
     */
    @Autowired
    private NotesRepository notesRepository;

    /**
     * Method to get the user's notes list
     *
     * @param userId The user identifier
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @return the notes list as {@link PaginatedResponse} of {@link Note}
     */
    public PaginatedResponse<Note> getNotes(String userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Note> notes = notesRepository.getNotes(userId, pageable);
        long notesCount = notesRepository.getNotesCount(userId);
        return new PaginatedResponse<>(notes, page, pageSize, notesCount);
    }

    /**
     * Method to create a new note
     *
     * @param authorId:    the author of the note identifier
     * @param noteId:      the note identifier
     * @param contentNote The content of the note
     */
    public void createNote(String authorId, String noteId, String contentNote) {
        notesRepository.createNote(authorId, noteId, contentNote, currentTimeMillis());
    }

    /**
     * Method to check whether a note exists
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     * @return whether a note exists as boolean
     */
    public boolean noteExists(String authorId, String noteId) {
        return notesRepository.getNote(authorId, noteId) != null;
    }

    /**
     * Method to mark a note as done
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     */
    public void markAsDone(String authorId, String noteId) {
        notesRepository.manageNoteStatus(authorId, noteId, true, currentTimeMillis());
    }

    /**
     * Method to mark a note as todo
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     */
    public void markAsToDo(String authorId, String noteId) {
        notesRepository.manageNoteStatus(authorId, noteId, false, -1);
    }

    /**
     * Method to delete a note
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     */
    public void deleteNote(String authorId, String noteId) {
        notesRepository.deleteNote(authorId, noteId);
    }

}
