package com.tecknobit.pandoro.services.projects.services;

import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.notes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeNotesService {

    /**
     * {@code notesRepository} instance for the notes repository
     */
    private final NotesRepository notesRepository;

    /**
     * Constructor used to init the service
     *
     * @param notesRepository The instance for the notes repository
     */
    @Autowired
    public ChangeNotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
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

    // TODO: 25/08/2025 TO DOCU
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

    // TODO: 25/08/2025 TO DOCUMENT
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
