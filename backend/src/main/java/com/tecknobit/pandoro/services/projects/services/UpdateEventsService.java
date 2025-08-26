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

// TODO: 26/08/2025 TO DOCU 1.2.0
@Service
public class UpdateEventsService {

    private final UpdateEventsRepository updateEventsRepository;

    @Autowired
    public UpdateEventsService(UpdateEventsRepository updateEventsRepository) {
        this.updateEventsRepository = updateEventsRepository;
    }


    @Wrapper
    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner) {
        storeUpdateEvent(author, type, owner, null);
    }

    private void storeUpdateEvent(PandoroUser author, UpdateEventType type, Update owner, Note changeNote) {
        String eventId = EquinoxController.generateIdentifier();
        UpdateEvent event = new UpdateEvent(
                eventId,
                owner,
                type,
                author,
                System.currentTimeMillis(),
                changeNote
        );
        updateEventsRepository.save(event);
    }

}
