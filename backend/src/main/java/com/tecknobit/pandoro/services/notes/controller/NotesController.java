package com.tecknobit.pandoro.services.notes.controller;

import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.notes.service.NotesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;
import static com.tecknobit.pandoro.services.notes.service.NotesHelper.ALL_FILTER_VALUE;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_AS_DONE_ENDPOINT;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_AS_TO_DO_ENDPOINT;
import static com.tecknobit.pandorocore.helpers.PandoroInputsValidator.INSTANCE;

/**
 * The {@code NotesController} class is useful to manage all the note operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController
 * @see DefaultPandoroController
 */
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + NOTES_KEY)
public class NotesController extends DefaultPandoroController {

    /**
     * {@code WRONG_CONTENT_NOTE_MESSAGE} message to use when a wrong content note has been inserted
     */
    public static final String WRONG_CONTENT_NOTE_MESSAGE = "wrong_content_note_key";

    /**
     * {@code notesHelper} instance to manage the notes database operations
     */
    @Autowired
    private NotesHelper notesHelper;

    /**
     * Method to get a notes list
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param statusFilter The status of the note to use as filter
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/notes", method = GET)
    public <T> T getNotes(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize,
            @RequestParam(name = MARKED_AS_DONE_KEY, defaultValue = ALL_FILTER_VALUE, required = false) String statusFilter
    ) {
        if (isMe(id, token))
            return (T) successResponse(notesHelper.getNotes(id, page, pageSize, statusFilter));
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to create a new note
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param payload The payload with the content of the note
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
        if (!isMe(id, token))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String contentNote = jsonHelper.getString(CONTENT_NOTE_KEY);
        if (!INSTANCE.isContentNoteValid(contentNote))
            return failedResponse(WRONG_CONTENT_NOTE_MESSAGE);
        notesHelper.createNote(id, generateIdentifier(), contentNote);
        return successResponse();
    }

    /**
     * Method to edit an existing note
     *
     * @param id      The identifier of the user
     * @param token   The token of the user
     * @param payload The payload with the content of the note
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "{" + NOTE_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}", method = PATCH)
    public String editNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody Map<String, String> payload
    ) {
        if (!isMe(id, token) || !notesHelper.noteExists(id, noteId))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String contentNote = jsonHelper.getString(CONTENT_NOTE_KEY);
        if (!INSTANCE.isContentNoteValid(contentNote))
            return failedResponse(WRONG_CONTENT_NOTE_MESSAGE);
        notesHelper.editNote(id, noteId, contentNote);
        return successResponse();
    }

    /**
     * Method to mark as done an existing note
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param noteId The identifier of the note
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
     * @param id The identifier of the user
     * @param token The token of the user
     * @param noteId The identifier of the note
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
     * @param id The identifier of the user
     * @param token The token of the user
     * @param noteId The identifier of the note
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
