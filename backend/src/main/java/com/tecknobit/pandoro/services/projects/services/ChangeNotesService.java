package com.tecknobit.pandoro.services.projects.services;

import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.notes.repository.NotesRepository;
import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // TODO: 26/08/2025 TO DOCU 1.2.0
    public Note getChangeNote(String updateId, String noteId) {
        return notesRepository.getNoteByUpdate(updateId, noteId);
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
     * Method to check whether a change note exists
     *
     * @param noteId   The identifier of the note to add
     * @param updateId The update identifier
     * @return whether the change note exists as boolean
     * @since 1.2.0
     */
    public boolean updateHasChangeNote(String updateId, String noteId) {
        return notesRepository.getNoteByUpdate(updateId, noteId) != null;
    }

    /**
     * Method to add a change note to an update
     *
     * @param user The user owner of the change note
     * @param noteId      The identifier of the note to add
     * @param contentNote The content of the note to add
     * @param update    The update where the change note will be added
     */
    @Transactional
    public void addChangeNote(PandoroUser user, String noteId, String contentNote, Update update) {
        Note changeNote = new Note(noteId, user, contentNote, System.currentTimeMillis(), update);
        notesRepository.save(changeNote);
        updateEventsNotifier.changeNoteAdded(user, update, changeNote);
    }

    /**
     * Method to edit an existing change note of an update
     *
     * @param user The user owner of the change note
     * @param changeNote    The note to edit
     * @param contentNote The content of the note to add
     */
    @Transactional
    // TODO: 26/08/2025 TO DOCU 1.2.0
    public void editChangeNote(PandoroUser user, Update update, Note changeNote, String contentNote) {
        notesRepository.editNote(user.getId(), changeNote.getId(), contentNote);
        updateEventsNotifier.changeNoteEdited(user, update, changeNote);
    }

    /**
     * Method to mark as done a change note
     *
     * @param update The update owner of the change note
     * @param changeNote   The note to mark as done
     * @param user   The user who marks the note as done
     */
    @Transactional
    public void markChangeNoteAsDone(Update update, Note changeNote, PandoroUser user) {
        long markAsDoneDate = System.currentTimeMillis();
        notesRepository.manageChangeNoteStatus(update.getId(), changeNote.getId(), true, user.getId(), markAsDoneDate);
        updateEventsNotifier.changeNoteDone(user, update, changeNote);
    }

    /**
     * Method to mark as to-do a change note
     *
     * @param update The update owner of the change note
     * @param changeNote   The note to mark as to-do
     * @param user   The user who marks the note as to-do
     */
    @Transactional
    public void markChangeNoteAsToDo(Update update, Note changeNote, PandoroUser user) {
        notesRepository.manageChangeNoteStatus(update.getId(), changeNote.getId(), false, null, -1);
        updateEventsNotifier.changeNoteUndone(user, update, changeNote);
    }

    /**
     * Method used to move a change note from an update to other
     *
     *
     * @since 1.2.0
     */
    @Transactional
    // TODO: 26/08/2025 TO DOCU 1.2.0
    public void moveChangeNote(Note changeNote, Update sourceUpdate, Update destinationUpdate, PandoroUser user) {
        notesRepository.moveChangeNote(changeNote.getId(), destinationUpdate.getId());
        updateEventsNotifier.changeNoteMoved(user, sourceUpdate, destinationUpdate, changeNote);
    }

    /**
     * Method to delete a change note
     *
     * @param update The update owner of the change note
     * @param changeNote   The identifier of the note to delete
     * @param user   The user who deleted the change note
     */
    @Transactional
    public void deleteChangeNote(Update update, Note changeNote, PandoroUser user) {
        notesRepository.deleteChangeNote(update.getId(), changeNote.getId());
        updateEventsNotifier.changeNoteRemoved(user, update, changeNote);
    }

}
