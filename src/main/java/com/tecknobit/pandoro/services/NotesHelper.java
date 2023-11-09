package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Note;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.System.currentTimeMillis;

@Service
public class NotesHelper {

    public static final String NOTE_IDENTIFIER_KEY = "note_id";

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

    public void createNote(String authorId, String noteId, String contentNote) {
        notesRepository.createNote(authorId, noteId, contentNote, currentTimeMillis());
    }

    public boolean noteExists(String authorId, String noteId) {
        return notesRepository.getNote(authorId, noteId) != null;
    }

    public void markAsDone(String authorId, String noteId) {
        notesRepository.manageNoteStatus(authorId, noteId, true, currentTimeMillis());
    }

    public void markAsToDo(String authorId, String noteId) {
        notesRepository.manageNoteStatus(authorId, noteId, false, -1);
    }

    public void deleteNote(String authorId, String noteId) {
        notesRepository.deleteNote(authorId, noteId);
    }

}
