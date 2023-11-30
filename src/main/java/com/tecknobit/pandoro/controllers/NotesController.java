package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.pandoro.services.NotesHelper;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*;
import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.helpers.InputsValidatorKt.isContentNoteValid;
import static com.tecknobit.pandoro.services.NotesHelper.NOTE_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY;

/**
 * The {@code NotesController} class is useful to manage all the note operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroController
 */
@RestController
@RequestMapping(path = BASE_ENDPOINT + NOTES_KEY)
public class NotesController extends PandoroController {

    /**
     * {@code NOTES_KEY} notes key
     */
    public static final String NOTES_KEY = "notes";

    /**
     * {@code CREATE_NOTE_ENDPOINT} endpoint to create a new note
     */
    public static final String CREATE_NOTE_ENDPOINT = "/create";

    /**
     * {@code MARK_AS_DONE_ENDPOINT} endpoint to mark as done an existing note
     */
    public static final String MARK_AS_DONE_ENDPOINT = "/markAsDone";

    /**
     * {@code MARK_AS_TO_DO_ENDPOINT} endpoint to mark as todo an existing note
     */
    public static final String MARK_AS_TO_DO_ENDPOINT = "/markAsToDo";

    /**
     * {@code DELETE_NOTE_ENDPOINT} endpoint to delete an existing note
     */
    public static final String DELETE_NOTE_ENDPOINT = "/deleteNote";

    /**
     * {@code WRONG_CONTENT_NOTE_MESSAGE} message to use when a wrong content note has been inserted
     */
    public static final String WRONG_CONTENT_NOTE_MESSAGE = "Wrong content note";

    /**
     * {@code notesHelper} instance to manage the notes database operations
     */
    private final NotesHelper notesHelper;

    /**
     * Constructor to init a {@link NotesController} controller
     *
     * @param notesHelper: instance to manage the notes database operations
     */
    @Autowired
    public NotesController(NotesHelper notesHelper) {
        this.notesHelper = notesHelper;
    }

    /**
     * Method to get a notes list
     *
     * @param id:    the identifier of the user
     * @param token: the token of the user
     * @return the result of the request as {@link String} if fails or {@link JSONArray} if is successfully
     */
    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/notes", method = GET)
    public <T> T getNotesList(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token))
            return (T) notesHelper.getNotes(id);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to create a new note
     *
     * @param id:          the identifier of the user
     * @param token:       the token of the user
     * @param contentNote: the content of the note
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = CREATE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/notes/create", method = POST)
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

    /**
     * Method to mark as done an existing note
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param noteId: the identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "{" + NOTE_IDENTIFIER_KEY + "}" + MARK_AS_DONE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/notes/{note_id}/markAsDone", method = PATCH)
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

    /**
     * Method to mark as todo an existing note
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param noteId: the identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "{" + NOTE_IDENTIFIER_KEY + "}" + MARK_AS_TO_DO_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/notes/{note_id}/markAsToDo", method = PATCH)
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

    /**
     * Method to delete an existing note
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param noteId: the identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "{" + NOTE_IDENTIFIER_KEY + "}" + DELETE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/notes/{note_id}/deleteNote", method = DELETE)
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
