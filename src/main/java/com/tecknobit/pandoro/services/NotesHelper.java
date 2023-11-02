package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Note;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesHelper {

    public static final String AUTHOR_KEY = "author";

    public static final String CONTENT_NOTE_KEY = "contentNote";

    public static final String CREATION_DATE_KEY = "creationDate";

    public static final String MARKED_AS_DONE_KEY = "markedAsDone";

    public static final String MARKED_AS_DONE_BY_KEY = "markedAsDoneBy";

    public static final String MARKED_AS_DONE_DATE_KEY = "markedAsDoneDate";

    @Autowired
    private NotesRepository notesRepository;

    public List<Note> getNotes(String id) {
        return notesRepository.getNotes(id);
    }

    public void createNote(String userId, String id, String contentNote) {
        notesRepository.createNote(userId, id, contentNote, System.currentTimeMillis());
    }

}
