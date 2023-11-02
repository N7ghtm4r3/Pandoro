package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Note;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesHelper {

    public static final String AUTHOR_KEY = "author";

    public static final String CONTENT_NOTE_KEY = "content_note";

    public static final String CREATION_DATE_KEY = "creation_date";

    public static final String MARKED_AS_DONE_KEY = "marked_as_done";

    public static final String MARKED_AS_DONE_BY_KEY = "marked_as_done_by";

    public static final String MARKED_AS_DONE_DATE_KEY = "marked_as_done_date";

    @Autowired
    private NotesRepository notesRepository;

    public List<Note> getNotes(String id) {
        return notesRepository.getNotes(id);
    }

    public void createNote(String userId, String id, String contentNote) {
        notesRepository.createNote(userId, id, contentNote, System.currentTimeMillis());
    }

}
