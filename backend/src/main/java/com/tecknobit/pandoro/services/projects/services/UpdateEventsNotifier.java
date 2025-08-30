package com.tecknobit.pandoro.services.projects.services;

import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import com.tecknobit.equinoxcore.annotations.Wrapper;
import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.projects.entities.UpdateEvent;
import com.tecknobit.pandoro.services.projects.repositories.UpdateEventsRepository;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandorocore.enums.events.UpdateEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tecknobit.pandorocore.enums.events.UpdateEventType.*;

/**
 * The {@code UpdateEventsNotifier} notify and store the event occurred in the lifecycle of an update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @since 1.2.0
 */
@Service
public class UpdateEventsNotifier {

    /**
     * {@code updateEventsRepository} instance for the update events repository
     */
    private final UpdateEventsRepository updateEventsRepository;

    /**
     * Constructor used to init the service
     *
     * @param updateEventsRepository The instance for the update events repository
     */
    @Autowired
    public UpdateEventsNotifier(UpdateEventsRepository updateEventsRepository) {
        this.updateEventsRepository = updateEventsRepository;
    }

    /**
     * Method used to store a {@link UpdateEventType#SCHEDULED} event
     *
     * @param author The author who made an action which created the event
     * @param owner  The update owner of the event
     */
    @Wrapper
    public void updateScheduled(PandoroUser author, Update owner) {
        storeUpdateEvent(author, SCHEDULED, owner);
    }

    /**
     * Method used to store a {@link UpdateEventType#CHANGENOTE_ADDED} event
     *
     * @param author The author who made an action which created the event
     * @param owner  The update owner of the event
     */
    @Wrapper
    public void changeNoteAdded(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_ADDED, owner, changeNote);
    }

    /**
     * Method used to store a {@link UpdateEventType#CHANGENOTE_DONE} event
     *
     * @param author     The author who made an action which created the event
     * @param owner      The update owner of the event
     * @param changeNote The change note marked as done
     */
    @Wrapper
    public void changeNoteDone(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_DONE, owner, changeNote);
    }

    /**
     * Method used to store a {@link UpdateEventType#CHANGENOTE_UNDONE} event
     *
     * @param author The author who made an action which created the event
     * @param owner The update owner of the event
     * @param changeNote The change note marked as to-do
     */
    @Wrapper
    public void changeNoteUndone(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_UNDONE, owner, changeNote);
    }

    /**
     * Method used to store a {@link UpdateEventType#CHANGENOTE_EDITED} event
     *
     * @param author The author who made an action which created the event
     * @param owner The update owner of the event
     * @param changeNote The edited change note
     * @param oldContent The old content of the change note
     */
    @Wrapper
    public void changeNoteEdited(PandoroUser author, Update owner, Note changeNote, String oldContent) {
        storeUpdateEvent(author, CHANGENOTE_EDITED, owner, changeNote, oldContent);
    }

    /**
     * Method used to store the {@link UpdateEventType#CHANGENOTE_MOVED_TO} and {@link UpdateEventType#CHANGENOTE_MOVED_FROM}
     * events
     *
     * @param author The author who made an action which created the event
     * @param currentOwner The current update from where the change note has been moved
     * @param nextOwner The next update where the change note has been moved
     * @param changeNote The moved change note
     */
    @Wrapper
    @Transactional
    public void changeNoteMoved(PandoroUser author, Update currentOwner, Update nextOwner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_MOVED_TO, currentOwner, changeNote, nextOwner.getTargetVersion());
        storeUpdateEvent(author, CHANGENOTE_MOVED_FROM, nextOwner, changeNote, currentOwner.getTargetVersion());
    }

    /**
     * Method used to store a {@link UpdateEventType#CHANGENOTE_REMOVED} event
     *
     * @param author The author who made an action which created the event
     * @param owner The update owner of the event
     * @param changeNote The removed change note
     */
    @Wrapper
    public void changeNoteRemoved(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_REMOVED, owner, changeNote);
    }

    /**
     * Method used to store a {@link UpdateEventType#STARTED} event
     *
     * @param author The author who made an action which created the event
     * @param owner The update owner of the event
     */
    @Wrapper
    public void updateStarted(PandoroUser author, Update owner) {
        storeUpdateEvent(author, STARTED, owner);
    }

    /**
     * Method used to store a {@link UpdateEventType#PUBLISHED} event
     *
     * @param author The author who made an action which created the event
     * @param owner The update owner of the event
     */
    @Wrapper
    public void updatePublished(PandoroUser author, Update owner) {
        storeUpdateEvent(author, PUBLISHED, owner);
    }

    /**
     * Method used to store a notified update event
     *
     * @param author The author who made an action which created the event
     * @param type The type of the occurred event
     * @param owner The update owner of the event
     */
    @Wrapper
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner) {
        storeUpdateEvent(author, type, owner, (String) null);
    }

    /**
     * Method used to store a notified update event
     *
     * @param author The author who made an action which created the event
     * @param type The type of the occurred event
     * @param owner The update owner of the event
     * @param extraContent Extra content to attach to the event
     */
    @Wrapper
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner, String extraContent) {
        storeUpdateEvent(author, type, owner, null, extraContent);
    }

    /**
     * Method used to store a notified update event
     *
     * @param author The author who made an action which created the event
     * @param type The type of the occurred event
     * @param owner The update owner of the event
     * @param changeNote The change note related to the occurred event
     */
    @Wrapper
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner, Note changeNote) {
        storeUpdateEvent(author, type, owner, changeNote, null);
    }

    /**
     * Method used to store a notified update event
     *
     * @param author The author who made an action which created the event
     * @param type The type of the occurred event
     * @param owner The update owner of the event
     * @param changeNote The change note related to the occurred event
     * @param extraContent Extra content to attach to the event
     */
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner, Note changeNote,
                                  String extraContent) {
        String eventId = EquinoxController.generateIdentifier();
        String noteContent = null;
        if (changeNote != null)
            noteContent = changeNote.getContent();
        UpdateEvent event = new UpdateEvent(
                eventId,
                owner,
                type,
                author,
                System.currentTimeMillis(),
                noteContent,
                extraContent
        );
        updateEventsRepository.save(event);
    }

}
