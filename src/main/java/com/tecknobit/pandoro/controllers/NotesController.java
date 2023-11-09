package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.NotesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.services.NotesHelper.NOTE_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY;
import static helpers.InputsValidatorKt.isContentNoteValid;

@RestController
@RequestMapping(path = BASE_ENDPOINT + NOTES_KEY)
public class NotesController extends PandoroController {

    public static final String NOTES_KEY = "notes";

    public static final String CREATE_NOTE_ENDPOINT = "/create";

    public static final String MARK_AS_DONE_ENDPOINT = "/markAsDone";

    public static final String MARK_AS_TO_DO_ENDPOINT = "/markAsToDo";

    public static final String DELETE_NOTE_ENDPOINT = "/deleteNote";

    public static final String WRONG_CONTENT_NOTE_MESSAGE = "Wrong content note";

    private final NotesHelper notesHelper;

    @Autowired
    public NotesController(NotesHelper notesHelper) {
        this.notesHelper = notesHelper;
    }

    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public <T> T getNotesList(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token))
            return (T) notesHelper.getNotes(id);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PostMapping(
            path = CREATE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String createNote(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody String contentNote
    ) {
        if (isAuthenticatedUser(id, token)) {
            if (isContentNoteValid(contentNote)) {
                notesHelper.createNote(id, generateIdentifier(), contentNote);
                return successResponse();
            } else
                return failedResponse(WRONG_CONTENT_NOTE_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PatchMapping(
            path = "{" + NOTE_IDENTIFIER_KEY + "}" + MARK_AS_DONE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String markAsDone(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        if (isAuthenticatedUser(id, token) && notesHelper.noteExists(id, noteId)) {
            notesHelper.markAsDone(id, noteId);
            return successResponse();
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PatchMapping(
            path = "{" + NOTE_IDENTIFIER_KEY + "}" + MARK_AS_TO_DO_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String markAsToDo(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        if (isAuthenticatedUser(id, token) && notesHelper.noteExists(id, noteId)) {
            notesHelper.markAsToDo(id, noteId);
            return successResponse();
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @DeleteMapping(
            path = "{" + NOTE_IDENTIFIER_KEY + "}" + DELETE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String deleteNote(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        if (isAuthenticatedUser(id, token) && notesHelper.noteExists(id, noteId)) {
            notesHelper.deleteNote(id, noteId);
            return successResponse();
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
