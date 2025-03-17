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
public class NotesService {

    /**
     * {@code ALL_FILTER_VALUE} the default value of the filter to retrieve the notes list
     */
    public static final String ALL_FILTER_VALUE = "all";

    /**
     * {@code notesRepository} instance for the notes project_repository
     */
    @Autowired
    private NotesRepository notesRepository;

    /**
     * Method to get the user's notes list
     *
     * @param userId The user identifier
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param statusFilter The status of the note to use as filter
     * @return the notes list as {@link PaginatedResponse} of {@link Note}
     */
    public PaginatedResponse<Note> getNotes(String userId, int page, int pageSize, String statusFilter) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Note> notes;
        long notesCount;
        if (statusFilter.equals(ALL_FILTER_VALUE)) {
            notes = notesRepository.getNotes(userId, pageable);
            notesCount = notesRepository.getNotesCount(userId);
        } else {
            boolean markedAsDone = Boolean.parseBoolean(statusFilter);
            notes = notesRepository.getNotes(userId, markedAsDone, pageable);
            notesCount = notesRepository.getNotesCount(userId, markedAsDone);
        }
        return new PaginatedResponse<>(notes, page, pageSize, notesCount);
    }

    /**
     * Method to get an existing note
     *
     * @param noteId The note identifier
     */
    public Note getNote(String noteId) {
        return notesRepository.findById(noteId).orElseThrow();
    }

    /**
     * Method to create a new note
     *
     * @param authorId The author of the note identifier
     * @param noteId The note identifier
     * @param contentNote The content of the note
     */
    public void createNote(String authorId, String noteId, String contentNote) {
        notesRepository.createNote(authorId, noteId, contentNote, currentTimeMillis());
    }

    /**
     * Method to edit an existing note
     *
     * @param authorId    The author of the note identifier
     * @param noteId      The note identifier
     * @param contentNote The content of the note
     */
    public void editNote(String authorId, String noteId, String contentNote) {
        notesRepository.editNote(authorId, noteId, contentNote);
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
    public void manageNoteStatus(String authorId, String noteId, boolean completed) {
        notesRepository.manageNoteStatus(authorId, noteId, completed, currentTimeMillis());
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
