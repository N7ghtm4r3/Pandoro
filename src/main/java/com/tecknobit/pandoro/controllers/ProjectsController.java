package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.records.ProjectUpdate;
import com.tecknobit.pandoro.records.ProjectUpdate.Status;
import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.ProjectsHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*;
import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY;
import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.NotesController.WRONG_CONTENT_NOTE_MESSAGE;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.helpers.InputsValidatorKt.*;
import static com.tecknobit.pandoro.records.ProjectUpdate.Status.IN_DEVELOPMENT;
import static com.tecknobit.pandoro.records.ProjectUpdate.Status.SCHEDULED;
import static com.tecknobit.pandoro.services.NotesHelper.NOTE_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.NAME_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY;

/**
 * The {@code ProjectsController} class is useful to manage all the projects operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroController
 */
@RestController
@RequestMapping(path = BASE_ENDPOINT + PROJECTS_KEY)
public class ProjectsController extends PandoroController {

    /**
     * {@code ADD_PROJECT_ENDPOINT} endpoint to add a new project
     */
    public static final String ADD_PROJECT_ENDPOINT = "/addProject";

    /**
     * {@code EDIT_PROJECT_ENDPOINT} endpoint to edit an existing project
     */
    public static final String EDIT_PROJECT_ENDPOINT = "/editProject";

    /**
     * {@code DELETE_PROJECT_ENDPOINT} endpoint to delete an existing project
     */
    public static final String DELETE_PROJECT_ENDPOINT = "/deleteProject";

    /**
     * {@code UPDATES_PATH} path for the updates operations
     */
    public static final String UPDATES_PATH = "/updates/";

    /**
     * {@code SCHEDULE_UPDATE_ENDPOINT} endpoint to schedule a new update for a project
     */
    public static final String SCHEDULE_UPDATE_ENDPOINT = "schedule";

    /**
     * {@code START_UPDATE_ENDPOINT} endpoint to start an existing update for a project
     */
    public static final String START_UPDATE_ENDPOINT = "/start";

    /**
     * {@code PUBLISH_UPDATE_ENDPOINT} endpoint to publish an existing update for a project
     */
    public static final String PUBLISH_UPDATE_ENDPOINT = "/publish";

    /**
     * {@code ADD_CHANGE_NOTE_ENDPOINT} endpoint to add a new change note for an update
     */
    public static final String ADD_CHANGE_NOTE_ENDPOINT = "/addChangeNote";

    /**
     * {@code MARK_CHANGE_NOTE_AS_DONE_ENDPOINT} endpoint to mark as done a change note of an update
     */
    public static final String MARK_CHANGE_NOTE_AS_DONE_ENDPOINT = "/markChangeNoteAsDone";

    /**
     * {@code MARK_CHANGE_NOTE_AS_TODO_ENDPOINT} endpoint to mark as todo a change note of an update
     */
    public static final String MARK_CHANGE_NOTE_AS_TODO_ENDPOINT = "/markChangeNoteAsToDo";

    /**
     * {@code DELETE_CHANGE_NOTE_ENDPOINT} endpoint to delete a change note of an update
     */
    public static final String DELETE_CHANGE_NOTE_ENDPOINT = "/deleteChangeNote";

    /**
     * {@code DELETE_UPDATE_ENDPOINT} endpoint to delete a project
     */
    public static final String DELETE_UPDATE_ENDPOINT = "/delete";

    /**
     * {@code projectsHelper} instance to manage the projects database operations
     */
    private final ProjectsHelper projectsHelper;

    /**
     * Constructor to init the {@link ProjectsController} controller
     *
     * @param projectsHelper:{@code projectsHelper} instance to manage the projects database operations
     */
    @Autowired
    public ProjectsController(ProjectsHelper projectsHelper) {
        this.projectsHelper = projectsHelper;
    }

    /**
     * Method to get a projects list
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     *
     * @return the result of the request as {@link String} if fails or {@link JSONArray} if is successfully
     */
    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects", method = GET)
    public <T> T getProjectsList(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token))
            return (T) projectsHelper.getProjectsList(id);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to add a new project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "name" : "name of the project", -> [String]
     *                  "project_description": "description of the project", -> [String]
     *                  "project_short_description": "short description of the project", -> [String]
     *                  "project_version": "current project version", -> [String]
     *                  "groups" : [ -> [List of Strings or empty]
     *                      // id of the group -> [String]
     *                  ],
     *                  "project_repository": "the GitHub or Gitlab project's repository" -> [String]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = ADD_PROJECT_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/addProject", method = POST)
    public String addProject(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody String payload
    ) {
        return workWithProject(id, token, payload, null);
    }

    /**
     * Method to edit an existing project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "name" : "name of the project", -> [String]
     *                  "project_description": "description of the project", -> [String]
     *                  "project_short_description": "short description of the project", -> [String]
     *                  "project_version": "current project version", -> [String]
     *                  "groups" : [ -> [List of Strings or empty]
     *                      // id of the group -> [String]
     *                  ],
     *                  "project_repository": "the GitHub or Gitlab project's repository" -> [String]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + EDIT_PROJECT_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}/editProject", method = PATCH)
    public String editProject(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @RequestBody String payload
    ) {
        return workWithProject(id, token, payload, projectId);
    }

    /**
     * Method to add or edit a project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "name" : "name of the project", -> [String]
     *                  "project_description": "description of the project", -> [String]
     *                  "project_short_description": "short description of the project", -> [String]
     *                  "project_version": "current project version", -> [String]
     *                  "groups" : [ -> [List of Strings or empty]
     *                      // id of the group -> [String]
     *                  ],
     *                  "project_repository": "the GitHub or Gitlab project's repository" -> [String]
     *              }
     *      }
     * </pre>
     * @param projectId: the identifier of the project if exists
     *
     * @return the result of the request as {@link String}
     */
    private String workWithProject(String id, String token, String payload, String projectId) {
        User me = getMe(id, token);
        if (me != null) {
            JsonHelper hPayload = new JsonHelper(payload);
            String name = hPayload.getString(NAME_KEY);
            boolean isAdding = projectId == null;
            if (isValidProjectName(name)) {
                if (!isAdding) {
                    Project currentEditingProject = projectsHelper.getProjectById(id, projectId);
                    if (currentEditingProject == null || !currentEditingProject.getAuthor().getId().equals(id))
                        return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                }
                Project checkProject = projectsHelper.getProjectByName(id, name);
                if (checkProject == null || (!isAdding && checkProject.getId().equals(projectId))) {
                    String description = hPayload.getString(PROJECT_DESCRIPTION_KEY);
                    if (isValidProjectDescription(description)) {
                        String shortDescription = hPayload.getString(PROJECT_SHORT_DESCRIPTION_KEY);
                        if (isValidProjectShortDescription(shortDescription)) {
                            String version = hPayload.getString(PROJECT_VERSION_KEY);
                            if (isValidVersion(version)) {
                                ArrayList<String> groups = hPayload.fetchList(GROUPS_KEY);
                                ArrayList<String> adminGroups = new ArrayList<>();
                                for (Group group : me.getAdminGroups())
                                    adminGroups.add(group.getId());
                                if (groups.isEmpty() || adminGroups.containsAll(groups)) {
                                    String repository = hPayload.getString(PROJECT_REPOSITORY_KEY);
                                    if (isValidRepository(repository)) {
                                        if (isAdding)
                                            projectId = generateIdentifier();
                                        projectsHelper.workWithProject(id, projectId, name, description, shortDescription,
                                                version, repository, groups, isAdding);
                                        return successResponse();
                                    } else
                                        return failedResponse("Wrong project repository");
                                } else
                                    return failedResponse("Wrong groups list");
                            } else
                                return failedResponse("Wrong project version");
                        } else
                            return failedResponse("Wrong project short description");
                    } else
                        return failedResponse("Wrong project description");
                } else
                    return failedResponse("A project with this name already exists");
            } else
                return failedResponse("Wrong project name");
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to get a single project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project to fetch
     *
     * @return the result of the request as {@link String} if fails or {@link JSONObject} if is successfully
     */
    @GetMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}",
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}", method = GET)
    public <T> T getProject(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId
    ) {
        if (isAuthenticatedUser(id, token)) {
            Project project = projectsHelper.getProject(id, projectId);
            if (project != null)
                return (T) project;
            else
                return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to delete a project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + IDENTIFIER_KEY + "}" + DELETE_PROJECT_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}", method = DELETE)
    public String deleteProject(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(IDENTIFIER_KEY) String projectId
    ) {
        if (isAuthenticatedUser(id, token)) {
            if (projectsHelper.getProjectById(id, projectId) != null) {
                projectsHelper.deleteProject(id, projectId);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to schedule an update for a project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where add the new update
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "target_version": "the target version of the update", -> [String]
     *                  "update_change_notes": [ -> [List of Strings or empty]
     *                      // the change note of the update -> [String]
     *                  ]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + SCHEDULE_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/schedule", method = POST)
    public String scheduleUpdate(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @RequestBody String payload
    ) {
        if (isAuthenticatedUser(id, token)) {
            if (projectsHelper.getProject(id, projectId) != null) {
                JsonHelper hPayload = new JsonHelper(payload);
                String targetVersion = hPayload.getString(UPDATE_TARGET_VERSION_KEY);
                if (isValidVersion(targetVersion)) {
                    if (!projectsHelper.targetVersionExists(projectId, targetVersion)) {
                        ArrayList<String> changeNotes = hPayload.fetchList(UPDATE_CHANGE_NOTES_KEY);
                        if (areNotesValid(changeNotes)) {
                            projectsHelper.scheduleUpdate(generateIdentifier(), targetVersion, changeNotes, projectId, id);
                            return successResponse();
                        } else
                            return failedResponse("Wrong change notes list");
                    } else
                        return failedResponse("An update with this target version already exists");
                } else
                    return failedResponse("Wrong target version");
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to start an update of a project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where start the update
     * @param updateId: the identifier of the update to start
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + START_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/start", method = PATCH)
    public String startUpdate(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId
    ) {
        return manageUpdateStatus(id, token, projectId, updateId, false);
    }

    /**
     * Method to publish an update of a project
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where publish the update
     * @param updateId: the identifier of the update to publish
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + PUBLISH_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/publish", method = PATCH)
    public String publishUpdate(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId
    ) {
        return manageUpdateStatus(id, token, projectId, updateId, true);
    }

    /**
     * Method to manage the status of an update
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where manage the update status
     * @param updateId: the identifier of the update to manage its status
     * @param isPublishing: whether is publishing or starting operation
     *
     * @return the result of the request as {@link String}
     */
    private String manageUpdateStatus(String id, String token, String projectId, String updateId, boolean isPublishing) {
        if (isAuthenticatedUser(id, token)) {
            ProjectUpdate update = projectsHelper.updateExists(projectId, updateId);
            if (projectsHelper.getProject(id, projectId) != null && update != null) {
                Status status = update.getStatus();
                if (isPublishing) {
                    if (status != IN_DEVELOPMENT)
                        return failedResponse("An update to be published must be IN_DEVELOPMENT first");
                    projectsHelper.publishUpdate(projectId, updateId, id);
                } else {
                    if (status != SCHEDULED)
                        return failedResponse("An update to be published must be SCHEDULED first");
                    projectsHelper.startUpdate(projectId, updateId, id);
                }
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to add a change note to an update
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where add the new change note
     * @param updateId: the identifier of the update where add the new change note
     * @param contentNote: the content of the change note
     *
     * @return the result of the request as {@link String}
     */
    @PutMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + ADD_CHANGE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/addChangeNote", method = PUT)
    public String addChangeNote(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId,
            @RequestBody String contentNote
    ) {
        if (isAuthenticatedUser(id, token)) {
            if (projectsHelper.getProject(id, projectId) != null &&
                    projectsHelper.updateExists(projectId, updateId) != null) {
                if (isContentNoteValid(contentNote)) {
                    projectsHelper.addChangeNote(id, generateIdentifier(), contentNote, updateId);
                    return successResponse();
                } else
                    return failedResponse(WRONG_CONTENT_NOTE_MESSAGE);
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to mark a change note as done
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where mark as done the change note of an update
     * @param updateId: the identifier of the update where mark as done the change note
     * @param noteId: the identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + MARK_CHANGE_NOTE_AS_DONE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsDone",
            method = PATCH
    )
    public String markChangeNoteAsDone(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        return manageChangeNote(id, token, projectId, updateId, noteId, "markAsDone");
    }

    /**
     * Method to mark a change note as todo
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where mark as todo the change note of an update
     * @param updateId: the identifier of the update where mark as todo the change note
     * @param noteId: the identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + MARK_CHANGE_NOTE_AS_TODO_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsToDo",
            method = PATCH
    )
    public String markChangeNoteAsToDo(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        return manageChangeNote(id, token, projectId, updateId, noteId, "markAsToDo");
    }

    /**
     * Method to delete a change note
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where delete the change note of an update
     * @param updateId: the identifier of the update to where delete the change note
     * @param noteId: the identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + DELETE_CHANGE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/projects/{project_id}/updates/{update_id}/notes/{note_id}/deleteChangeNote",
            method = DELETE
    )
    public String deleteChangeNote(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        return manageChangeNote(id, token, projectId, updateId, noteId, "deleteChangeNote");
    }

    /**
     * Method to manage a change note
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where manage the change note
     * @param updateId: the identifier of the update where manage the change note
     * @param noteId: the identifier of the note
     * @param ope: the operation to execute
     *
     * @return the result of the request as {@link String}
     */
    private String manageChangeNote(String id, String token, String projectId, String updateId, String noteId,
                                    String ope) {
        if (isAuthenticatedUser(id, token)) {
            if (projectsHelper.getProject(id, projectId) != null &&
                    projectsHelper.updateExists(projectId, updateId) != null &&
                    projectsHelper.changeNoteExists(updateId, noteId)) {
                switch (ope) {
                    case "markAsDone" -> projectsHelper.markChangeNoteAsDone(updateId, noteId, id);
                    case "markAsToDo" -> projectsHelper.markChangeNoteAsToDo(updateId, noteId);
                    default -> projectsHelper.deleteChangeNote(updateId, noteId);
                }
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to delete an update
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where delete the update
     * @param updateId: the identifier of the update to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + DELETE_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/delete", method = DELETE)
    public String deleteUpdate(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId
    ) {
        if (isAuthenticatedUser(id, token)) {
            if (projectsHelper.getProject(id, projectId) != null &&
                    projectsHelper.updateExists(projectId, updateId) != null) {
                projectsHelper.deleteUpdate(projectId, updateId, projectId);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
