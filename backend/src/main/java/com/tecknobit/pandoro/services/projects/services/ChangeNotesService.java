package com.tecknobit.pandoro.services.projects.services;

import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.notes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@code ChangeNotesService} class is useful to handle the change notes database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @since 1.2.0
 */
@Service
public class ChangeNotesService {

    /**
     * {@code notesRepository} instance for the notes repository
     */
    private final NotesRepository notesRepository;

    // TODO: 26/08/2025 TO DOCU 1.2.0
    private final UpdateEventsNotifier updateEventsNotifier;

    /**
     * Constructor used to init the service
     *
     * @param notesRepository The instance for the notes repository
     */
    @Autowired
    // TODO: 26/08/2025 TO DOCU 1.2.0
    public ChangeNotesService(NotesRepository notesRepository, UpdateEventsNotifier updateEventsNotifier) {
        this.notesRepository = notesRepository;
        this.updateEventsNotifier = updateEventsNotifier;
    }

    /**
     * Method to add a change note to an update
     *
     * @param userId      The user identifier
     * @param noteId      The identifier of the note to add
     * @param contentNote The content of the note to add
     * @param updateId    The update identifier
     */
    public void addChangeNote(String userId, String noteId, String contentNote, String updateId) {
        notesRepository.addChangeNote(userId, noteId, contentNote, System.currentTimeMillis(), updateId);
    }

    /**
     * Method to edit an existing change note of an update
     *
     * @param userId      The user identifier
     * @param noteId      The identifier of the note to add
     * @param contentNote The content of the note to add
     */
    public void editChangeNote(String userId, String noteId, String contentNote) {
        notesRepository.editNote(userId, noteId, contentNote);
    }

    /**
     * Method to check whether a change note exists
     *
     * @param noteId   The identifier of the note to add
     * @param updateId The update identifier
     * @return whether the change note exists as boolean
     */
    public boolean changeNoteExists(String updateId, String noteId) {
        return notesRepository.getNoteByUpdate(updateId, noteId) != null;
    }

    /**
     * Method used to get a change note by its identifier
     *
     * @param noteId The identifier of the note to get
     * @return the change note as {@link Note}, {@code null} otherwise
     * @since 1.2.0
     */
    public Note getChangeNote(String noteId) {
        return notesRepository.findById(noteId).orElse(null);
    }

    /**
     * Method to mark as done a change note
     *
     * @param updateId The update identifier
     * @param noteId   The identifier of the note
     * @param userId   The user identifier
     */
    public void markChangeNoteAsDone(String updateId, String noteId, String userId) {
        notesRepository.manageChangeNoteStatus(updateId, noteId, true, userId, System.currentTimeMillis());
    }

    /**
     * Method to mark as to-do a change note
     *
     * @param updateId The update identifier
     * @param noteId   The identifier of the note
     */
    public void markChangeNoteAsToDo(String updateId, String noteId) {
        notesRepository.manageChangeNoteStatus(updateId, noteId, false, null, -1);
    }

    /**
     * Method used to move a change note from an update to other
     *
     * @param noteId    The identifier of the note
     * @param destinationUpdateId The identifier of the update to move the change note
     *
     * @since 1.2.0
     */
    public void moveChangeNote(String noteId, String destinationUpdateId) {
        notesRepository.moveChangeNote(noteId, destinationUpdateId);
    }

    /**
     * Method to delete a change note
     *
     * @param updateId The update identifier
     * @param noteId   The identifier of the note
     */
    public void deleteChangeNote(String updateId, String noteId) {
        notesRepository.deleteChangeNote(updateId, noteId);
    }

}
