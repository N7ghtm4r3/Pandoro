package com.tecknobit.pandoro.services.projects.controller;

import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.projects.dto.ProjectDTO;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.projects.services.ChangeNotesService;
import com.tecknobit.pandoro.services.projects.services.ProjectsService;
import com.tecknobit.pandorocore.enums.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;
import static com.tecknobit.pandoro.services.notes.controller.NotesController.WRONG_CONTENT_NOTE_MESSAGE;
import static com.tecknobit.pandoro.services.projects.controller.ProjectsController.ChangeNoteOperation.MARK_AS_DONE;
import static com.tecknobit.pandoro.services.projects.controller.ProjectsController.ChangeNoteOperation.MARK_AS_TODO;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT;
import static com.tecknobit.pandorocore.enums.UpdateStatus.SCHEDULED;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.*;
import static com.tecknobit.pandorocore.helpers.PandoroInputsValidator.INSTANCE;

/**
 * The {@code ProjectsController} class is useful to manage all the project operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController
 * @see DefaultPandoroController
 */
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + PROJECTS_KEY)
public class ProjectsController extends DefaultPandoroController {

    /**
     * {@code WRONG_PROJECT_NAME_ERROR_MESSAGE} message to use when the name of the project is not a valid name
     */
    public static final String WRONG_PROJECT_NAME_ERROR_MESSAGE = "wrong_project_name";

    /**
     * {@code WRONG_PROJECT_DESCRIPTION_ERROR_MESSAGE} message to use when the description of the project is not a valid description
     */
    public static final String WRONG_PROJECT_DESCRIPTION_ERROR_MESSAGE = "wrong_project_description";

    /**
     * {@code WRONG_PROJECT_VERSION_ERROR_MESSAGE} message to use when the version of the project is not valid
     */
    public static final String WRONG_PROJECT_VERSION_ERROR_MESSAGE = "wrong_project_version";

    /**
     * {@code WRONG_PROJECT_REPOSITORY_ERROR_MESSAGE} message to use when the repository of the project is not valid
     */
    public static final String WRONG_PROJECT_REPOSITORY_ERROR_MESSAGE = "wrong_project_repository";

    /**
     * {@code WRONG_PROJECT_NAME_EXISTS_ERROR_MESSAGE} message to use when the name of the project is already used
     */
    public static final String WRONG_PROJECT_NAME_EXISTS_ERROR_MESSAGE = "project_name_already_exists";

    /**
     * {@code WRONG_UPDATE_TARGET_VERSION_ERROR_MESSAGE} message to use when the target version of an update is not valid
     */
    public static final String WRONG_UPDATE_TARGET_VERSION_ERROR_MESSAGE = "wrong_target_version";

    /**
     * {@code WRONG_UPDATE_TARGET_VERSION_EXISTS_ERROR_MESSAGE} message to use when the version of an update is already used
     */
    public static final String WRONG_UPDATE_TARGET_VERSION_EXISTS_ERROR_MESSAGE = "update_version_already_exists";

    /**
     * {@code WRONG_CHANGE_NOTES_ERROR_MESSAGE} message to use when the change notes list is not valid
     */
    public static final String WRONG_CHANGE_NOTES_ERROR_MESSAGE = "wrong_change_notes_list";

    /**
     * {@code WRONG_PUBLISH_UPDATE_REQUEST_ERROR_MESSAGE} message to use when a request to publish an update is not valid
     */
    public static final String WRONG_PUBLISH_UPDATE_REQUEST_ERROR_MESSAGE = "wrong_publish_update_request";

    /**
     * {@code WRONG_START_UPDATE_REQUEST_ERROR_MESSAGE} message to use when a request to start an update is not valid
     */
    public static final String WRONG_START_UPDATE_REQUEST_ERROR_MESSAGE = "wrong_development_update_request";

    /**
     * The {@code ChangeNoteOperation} are the available operations to be performed on a change note
     *
     * @since 1.2.0
     */
    enum ChangeNoteOperation {

        /**
         * {@code MARK_AS_DONE} the change note must be marked as done
         */
        MARK_AS_DONE,

        /**
         * {@code MARK_AS_TODO} the change note must be marked as to do
         */
        MARK_AS_TODO,

        /**
         * {@code DELETE} the change note must be deleted
         */
        DELETE

    }

    /**
     * {@code projectsService} instance to manage the projects database operations
     */
    private final ProjectsService projectsService;

    /**
     * {@code changeNotesService} The service which handles the database operations of the change notes
     *
     * @since 1.2.0
     */
    private final ChangeNotesService changeNotesService;

    /**
     * Constructor used to init the controller
     *
     * @param projectsService The instance to manage the projects database operations
     * @param changeNotesService The service which handles the database operations of the change notes
     */
    @Autowired
    public ProjectsController(ProjectsService projectsService, ChangeNotesService changeNotesService) {
        this.projectsService = projectsService;
        this.changeNotesService = changeNotesService;
    }

    /**
     * Method to get the authored projects list
     *
     * @param id    The identifier of the user
     * @param token The token of the user
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            path = AUTHORED_PROJECTS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/authored", method = GET)
    public <T> T getAuthoredProjects(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isMe(id, token))
            return (T) successResponse(projectsService.getAuthoredProjects(id));
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to get a {@link UpdateStatus#IN_DEVELOPMENT} projects list
     *
     * @param id       The identifier of the user
     * @param token    The token of the user
     * @param page     The page requested
     * @param pageSize The size of the items to insert in the page
     * @param filters The filter to apply to the query to select the project
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            path = IN_DEVELOPMENT_PROJECTS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/in_development", method = GET)
    public <T> T getInDevelopmentProjects(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize,
            @RequestParam(name = FILTERS_KEY, defaultValue = "", required = false) Set<String> filters
    ) {
        if (isMe(id, token))
            return (T) successResponse(projectsService.getInDevelopmentProjects(id, page, pageSize, filters));
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to get a projects list
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param filters The filter to apply to the query to select the project
     *
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects", method = GET)
    public <T> T getProjects(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize,
            @RequestParam(name = FILTERS_KEY, defaultValue = "", required = false) Set<String> filters
    ) {
        if (isMe(id, token))
            return (T) successResponse(projectsService.getProjects(id, page, pageSize, filters));
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to add a new project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "name" : "name of the project", -> [String]
     *                  "project_description": "description of the project", -> [String]
     *                  "project_version": "current project project_version", -> [String]
     *                  "groups" : [ -> [List of Strings or empty]
     *                      // id of the group -> [String]
     *                  ],
     *                  "project_repository": "the GitHub or Gitlab project's project_repository" -> [String]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects", method = POST)
    public String addProject(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @ModelAttribute ProjectDTO payload
    ) {
        return workWithProject(id, token, payload, null);
    }

    /**
     * Method to edit an existing project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "name" : "name of the project", -> [String]
     *                  "project_description": "description of the project", -> [String]
     *                  "project_version": "current project project_version", -> [String]
     *                  "groups" : [ -> [List of Strings or empty]
     *                      // id of the group -> [String]
     *                  ],
     *                  "project_repository": "the GitHub or Gitlab project's project_repository" -> [String]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = PATCH)
    public String editProject(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @ModelAttribute ProjectDTO payload
    ) {
        return workWithProject(id, token, payload, projectId);
    }

    /**
     * Method to add or edit a project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "name" : "name of the project", -> [String]
     *                  "project_description": "description of the project", -> [String]
     *                  "project_version": "current project project_version", -> [String]
     *                  "groups" : [ -> [List of Strings or empty]
     *                      // id of the group -> [String]
     *                  ],
     *                  "project_repository": "the GitHub or Gitlab project's project_repository" -> [String]
     *              }
     *      }
     * </pre>
     * @param projectId The identifier of the project if exists
     *
     * @return the result of the request as {@link String}
     */
    private String workWithProject(String id, String token, ProjectDTO payload, String projectId) {
        if (!isMe(id, token))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        String name = payload.name();
        if (!INSTANCE.isValidProjectName(name))
            return failedResponse(WRONG_PROJECT_NAME_ERROR_MESSAGE);
        boolean isAdding = projectId == null;
        if (!isAdding) {
            Project currentEditingProject = projectsService.getProjectById(projectId);
            if (currentEditingProject == null || !currentEditingProject.getAuthor().getId().equals(id))
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        }
        String description = payload.project_description();
        if (!INSTANCE.isValidProjectDescription(description))
            return failedResponse(WRONG_PROJECT_DESCRIPTION_ERROR_MESSAGE);
        String version = payload.project_version();
        if (!INSTANCE.isValidVersion(version))
            return failedResponse(WRONG_PROJECT_VERSION_ERROR_MESSAGE);
        String repository = payload.project_repository();
        if (!INSTANCE.isValidRepository(repository))
            return failedResponse(WRONG_PROJECT_REPOSITORY_ERROR_MESSAGE);
        if (isAdding)
            projectId = generateIdentifier();
        try {
            projectsService.workWithProject(id, projectId, payload, isAdding);
        } catch (Exception e) {
            return failedResponse(WRONG_PROJECT_NAME_EXISTS_ERROR_MESSAGE);
        }
        return successResponse();
    }

    /**
     * Method to get a single project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project to fetch
     *
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = GET)
    public <T> T getProject(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId
    ) {
        if (!isMe(id, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        Project project = projectsService.getProject(id, projectId);
        if (project != null)
            return (T) successResponse(project);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to delete a project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = DELETE)
    public String deleteProject(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId
    ) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        Project project = projectsService.getProjectById(projectId);
        if (project == null || !project.getAuthor().getId().equals(id))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        projectsService.deleteProject(id, projectId);
        return successResponse();
    }

    /**
     * Method to schedule an update for a project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where add the new update
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "target_version": "the target project_version of the update", -> [String]
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/schedule", method = POST)
    public String scheduleUpdate(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @RequestBody Map<String, Object> payload
    ) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        if (projectsService.getProject(id, projectId) == null)
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String targetVersion = jsonHelper.getString(UPDATE_TARGET_VERSION_KEY);
        if (!INSTANCE.isValidVersion(targetVersion))
            return failedResponse(WRONG_UPDATE_TARGET_VERSION_ERROR_MESSAGE);
        if (projectsService.targetVersionExists(projectId, targetVersion))
            return failedResponse(WRONG_UPDATE_TARGET_VERSION_EXISTS_ERROR_MESSAGE);
        List<String> changeNotes = jsonHelper.fetchList(UPDATE_CHANGE_NOTES_KEY);
        if (!INSTANCE.areNotesValid(changeNotes))
            return failedResponse(WRONG_CHANGE_NOTES_ERROR_MESSAGE);
        projectsService.scheduleUpdate(generateIdentifier(), targetVersion, changeNotes, projectId, id);
        return successResponse();
    }

    /**
     * Method to start an update of a project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where start the update
     * @param updateId The identifier of the update to start
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}" + START_UPDATE_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/start", method = PATCH)
    public String startUpdate(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId
    ) {
        return manageUpdateStatus(id, token, projectId, updateId, false);
    }

    /**
     * Method to publish an update of a project
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where publish the update
     * @param updateId The identifier of the update to publish
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}" + PUBLISH_UPDATE_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/publish", method = PATCH)
    public String publishUpdate(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId
    ) {
        return manageUpdateStatus(id, token, projectId, updateId, true);
    }

    /**
     * Method to manage the status of an update
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where manage the update status
     * @param updateId The identifier of the update to manage its status
     * @param isPublishing: whether is publishing or starting operation
     *
     * @return the result of the request as {@link String}
     */
    private String manageUpdateStatus(String id, String token, String projectId, String updateId, boolean isPublishing) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        Update update = projectsService.updateExists(projectId, updateId);
        if (projectsService.getProject(id, projectId) == null || update == null)
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        UpdateStatus status = update.getStatus();
        if (isPublishing) {
            if (status != IN_DEVELOPMENT)
                return failedResponse(WRONG_PUBLISH_UPDATE_REQUEST_ERROR_MESSAGE);
            projectsService.publishUpdate(projectId, updateId, id, update.getTargetVersion());
        } else {
            if (status != SCHEDULED)
                return failedResponse(WRONG_START_UPDATE_REQUEST_ERROR_MESSAGE);
            projectsService.startUpdate(projectId, updateId, id);
        }
        return successResponse();
    }

    /**
     * Method to add a change note to an update
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where add the new change note
     * @param updateId The identifier of the update where add the new change note
     * @param payload The payload with the content of the change note
     *
     * @return the result of the request as {@link String}
     */
    @PutMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}" + ADD_CHANGE_NOTE_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/addChangeNote", method = PUT)
    public String addChangeNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId,
            @RequestBody Map<String, String> payload
    ) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        Update update = projectsService.updateExists(projectId, updateId);
        if (projectsService.getProject(id, projectId) == null || update == null || update.isPublished())
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String contentNote = jsonHelper.getString(CONTENT_NOTE_KEY);
        if (!INSTANCE.isContentNoteValid(contentNote))
            return failedResponse(WRONG_CONTENT_NOTE_MESSAGE);
        changeNotesService.addChangeNote(id, generateIdentifier(), contentNote, updateId);
        return successResponse();
    }

    /**
     * Method to edit an existing change note of an update
     *
     * @param id        The identifier of the user
     * @param token     The token of the user
     * @param projectId The identifier of the project where add the new change note
     * @param updateId  The identifier of the update where add the new change note
     * @param noteId    The identifier of the note to edit
     * @param payload   The payload with the content of the change note
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}/" + NOTES_KEY +
                    "/{" + NOTE_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}", method = PATCH)
    public String editChangeNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId,
            @RequestBody Map<String, String> payload
    ) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        Update update = projectsService.updateExists(projectId, updateId);
        if (projectsService.getProject(id, projectId) == null || update == null || update.isPublished()
                || !changeNotesService.changeNoteExists(updateId, noteId))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String contentNote = jsonHelper.getString(CONTENT_NOTE_KEY);
        if (!INSTANCE.isContentNoteValid(contentNote))
            return failedResponse(WRONG_CONTENT_NOTE_MESSAGE);
        changeNotesService.editChangeNote(id, noteId, contentNote);
        return successResponse();
    }

    /**
     * Method to mark a change note as done
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where mark as done the change note of an update
     * @param updateId The identifier of the update where mark as done the change note
     * @param noteId The identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + MARK_CHANGE_NOTE_AS_DONE_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsDone",
            method = PATCH
    )
    public String markChangeNoteAsDone(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        return handleChangeNote(id, token, projectId, updateId, noteId, MARK_AS_DONE);
    }

    /**
     * Method to mark a change note as to-do
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where mark as to-do the change note of an update
     * @param updateId The identifier of the update where mark as to-do the change note
     * @param noteId The identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + MARK_CHANGE_NOTE_AS_TODO_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsToDo",
            method = PATCH
    )
    public String markChangeNoteAsToDo(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        return handleChangeNote(id, token, projectId, updateId, noteId, MARK_AS_TODO);
    }

    /**
     * Method to delete a change note
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where delete the change note of an update
     * @param updateId The identifier of the update to where delete the change note
     * @param noteId The identifier of the note
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}",
            method = DELETE
    )
    public String deleteChangeNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId
    ) {
        return handleChangeNote(id, token, projectId, updateId, noteId, ChangeNoteOperation.DELETE);
    }

    /**
     * Method to manage a change note
     *
     * @param id        The identifier of the user
     * @param token     The token of the user
     * @param projectId The identifier of the project where manage the change note
     * @param updateId  The identifier of the update where manage the change note
     * @param noteId    The identifier of the note
     * @param operation The operation to execute
     * @return the result of the request as {@link String}
     */
    private String handleChangeNote(String id, String token, String projectId, String updateId, String noteId,
                                    ChangeNoteOperation operation) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        Update update = projectsService.updateExists(projectId, updateId);
        if (projectsService.getProject(id, projectId) == null || update == null ||
                !changeNotesService.changeNoteExists(updateId, noteId)) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        boolean isNotInDevelopment = update.getStatus() != IN_DEVELOPMENT;
        switch (operation) {
            case MARK_AS_DONE -> {
                if (isNotInDevelopment)
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
                changeNotesService.markChangeNoteAsDone(updateId, noteId, id);
            }
            case MARK_AS_TODO -> {
                if (isNotInDevelopment)
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
                changeNotesService.markChangeNoteAsToDo(updateId, noteId);
            }
            default -> {
                if (update.isPublished())
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
                changeNotesService.deleteChangeNote(updateId, noteId);
            }
        }
        return successResponse();
    }

    /**
     * Endpoint used to move a change note from an update to other update
     *
     * @param id                  The identifier of the user
     * @param token               The token of the user
     * @param projectId           The identifier of the project where manage the change note
     * @param sourceUpdateId      The identifier of the update from move the change note
     * @param noteId              The identifier of the note
     * @param destinationUpdateId The identifier of the update to move the change note
     * @return the result of the request as {@link String}
     * @since 1.2.0
     */
    @PutMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + MOVE_ENDPOINT + "{" + DESTINATION_UPDATE_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/move/{destination_id}",
            method = PUT
    )
    public String moveChangeNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String sourceUpdateId,
            @PathVariable(NOTE_IDENTIFIER_KEY) String noteId,
            @PathVariable(DESTINATION_UPDATE_IDENTIFIER_KEY) String destinationUpdateId
    ) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        boolean userIsNotProjectCollaborator = projectsService.getProject(id, projectId) == null;
        Update sourceUpdate = projectsService.updateExists(projectId, sourceUpdateId);
        boolean sourceUpdateDoesNotExist = sourceUpdate == null;
        Update destinationUpdate = projectsService.updateExists(projectId, destinationUpdateId);
        boolean destinationUpdateDoesNotExist = destinationUpdate == null;
        boolean sourceUpdateDoesNotContainNote = !changeNotesService.changeNoteExists(sourceUpdateId, noteId);
        boolean destinationUpdateAlreadyContainsNote = changeNotesService.changeNoteExists(destinationUpdateId, noteId);
        if (userIsNotProjectCollaborator || sourceUpdateDoesNotExist || destinationUpdateDoesNotExist ||
                sourceUpdateDoesNotContainNote || destinationUpdateAlreadyContainsNote ||
                changeNotesService.getChangeNote(noteId).isMarkedAsDone() ||
                sourceUpdate.isPublished() || destinationUpdate.isPublished()) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        changeNotesService.moveChangeNote(noteId, destinationUpdateId);
        return successResponse();
    }

    /**
     * Method to delete an update
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where delete the update
     * @param updateId The identifier of the update to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}", method = DELETE)
    public String deleteUpdate(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_IDENTIFIER_KEY) String updateId
    ) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        if (projectsService.getProject(id, projectId) == null || projectsService.updateExists(projectId, updateId) == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        projectsService.deleteUpdate(projectId, updateId, id);
        return successResponse();
    }

}
