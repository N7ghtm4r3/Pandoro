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

// TODO: 26/08/2025 TO DOCU 1.2.0
@Service
public class UpdateEventsNotifier {

    private final UpdateEventsRepository updateEventsRepository;

    @Autowired
    public UpdateEventsNotifier(UpdateEventsRepository updateEventsRepository) {
        this.updateEventsRepository = updateEventsRepository;
    }

    @Wrapper
    public void updateScheduled(PandoroUser author, Update owner) {
        storeUpdateEvent(author, SCHEDULED, owner);
    }

    @Wrapper
    public void changeNoteAdded(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_ADDED, owner, changeNote);
    }

    @Wrapper
    public void changeNoteDone(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_DONE, owner, changeNote);
    }

    @Wrapper
    public void changeNoteUndone(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_UNDONE, owner, changeNote);
    }

    @Wrapper
    public void changeNoteEdited(PandoroUser author, Update owner, Note changeNote, String oldContent) {
        storeUpdateEvent(author, CHANGENOTE_EDITED, owner, changeNote, oldContent);
    }

    @Wrapper
    @Transactional
    public void changeNoteMoved(PandoroUser author, Update currentOwner, Update nextOwner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_MOVED_TO, currentOwner, changeNote, nextOwner.getTargetVersion());
        storeUpdateEvent(author, CHANGENOTE_MOVED_FROM, nextOwner, changeNote, currentOwner.getTargetVersion());
    }

    @Wrapper
    public void changeNoteRemoved(PandoroUser author, Update owner, Note changeNote) {
        storeUpdateEvent(author, CHANGENOTE_REMOVED, owner, changeNote);
    }

    @Wrapper
    public void updateStarted(PandoroUser author, Update owner) {
        storeUpdateEvent(author, STARTED, owner);
    }

    @Wrapper
    public void updatePublished(PandoroUser author, Update owner) {
        storeUpdateEvent(author, PUBLISHED, owner);
    }

    @Wrapper
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner) {
        storeUpdateEvent(author, type, owner, (String) null);
    }

    @Wrapper
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner, String extraContent) {
        storeUpdateEvent(author, type, owner, null, extraContent);
    }

    @Wrapper
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner, Note changeNote) {
        storeUpdateEvent(author, type, owner, changeNote, null);
    }

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
