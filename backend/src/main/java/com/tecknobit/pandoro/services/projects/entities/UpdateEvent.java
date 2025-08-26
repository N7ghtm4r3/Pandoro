package com.tecknobit.pandoro.services.projects.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.equinoxbackend.annotations.EmptyConstructor;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import com.tecknobit.pandoro.services.notes.entity.Note;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandorocore.enums.events.UpdateEventType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

// TODO: 26/08/2025 TO DOCU 1.2.0
@Entity
@Table(name = UPDATE_EVENTS_KEY)
public class UpdateEvent extends EquinoxItem {

    @ManyToOne
    @JoinColumn(name = OWNER_KEY)
    @JsonIgnoreProperties({
            EVENTS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    @OnDelete(action = CASCADE)
    private final Update owner;

    @Enumerated(value = EnumType.STRING)
    private final UpdateEventType type;

    @ManyToOne
    @JoinColumn(name = AUTHOR_KEY)
    @JsonIgnoreProperties({
            TOKEN_KEY,
            PASSWORD_KEY,
            LANGUAGE_KEY,
            CHANGELOGS_KEY,
            GROUPS_KEY,
            PROJECTS_KEY,
            NOTES_KEY,
            EVENTS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    private final PandoroUser author;

    @Column(name = TIMESTAMP_KEY)
    private final long timestamp;

    @ManyToOne
    @JsonIgnoreProperties({
            EVENTS_KEY,
            "hibernateLazyInitializer",
            "handler"
    })
    private final Note note;

    @EmptyConstructor
    public UpdateEvent() {
        this(null, null, null, null, -1, null);
    }

    public UpdateEvent(String id, Update owner, UpdateEventType type, PandoroUser author, long timestamp, Note changeNote) {
        super(id);
        this.owner = owner;
        this.type = type;
        this.author = author;
        this.timestamp = timestamp;
        this.note = changeNote;
    }

    public Update getOwner() {
        return owner;
    }

    public UpdateEventType getType() {
        return type;
    }

    public PandoroUser getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp;

    }

    public Note getNote() {
        return note;
    }

}
