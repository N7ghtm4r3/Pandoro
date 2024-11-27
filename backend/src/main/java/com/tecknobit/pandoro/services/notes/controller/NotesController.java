package com.tecknobit.pandoro.services.notes.controller;

import com.tecknobit.equinoxbackend.environment.services.DefaultEquinoxController;
import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.notes.service.NotesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.helpers.PandoroInputsValidator.Companion;

/**
 * The {@code NotesController} class is useful to manage all the note operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController
 * @see DefaultEquinoxController
 */
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + NOTES_KEY)
public class NotesController extends DefaultEquinoxController {

    /**
     * {@code WRONG_CONTENT_NOTE_MESSAGE} message to use when a wrong content note has been inserted
     */
    public static final String WRONG_CONTENT_NOTE_MESSAGE = "wrong_content_note_key";

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
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/notes", method = GET)
    public <T> T getNotesList(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isMe(id, token))
            return (T) successResponse(notesHelper.getNotes(id));
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to create a new note
     *
     * @param id:          the identifier of the user
     * @param token:       the token of the user
     * @param payload: the payload with the content of the note
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/notes", method = POST)
    public String createNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody Map<String, String> payload
    ) {
        if (isMe(id, token)) {
            loadJsonHelper(payload);
            String contentNote = jsonHelper.getString(CONTENT_NOTE_KEY);
            if (Companion.isContentNoteValid(contentNote)) {
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}/markAsDone", method = PATCH)
    public String markAsDone(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        if (isMe(id, token) && notesHelper.noteExists(id, noteId)) {
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}/markAsToDo", method = PATCH)
    public String markAsToDo(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        if (isMe(id, token) && notesHelper.noteExists(id, noteId)) {
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
            path = "{" + NOTE_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}", method = DELETE)
    public String deleteNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        if (isMe(id, token) && notesHelper.noteExists(id, noteId)) {
            notesHelper.deleteNote(id, noteId);
            return successResponse();
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
