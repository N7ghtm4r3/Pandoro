package com.tecknobit.pandoro.services.projects.controller;

import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.groups.service.GroupsHelper;
import com.tecknobit.pandoro.services.projects.dto.ProjectDTO;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.projects.entities.ProjectUpdate;
import com.tecknobit.pandoro.services.projects.service.ProjectsHelper;
import com.tecknobit.pandorocore.enums.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;
import static com.tecknobit.pandoro.services.notes.controller.NotesController.WRONG_CONTENT_NOTE_MESSAGE;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.*;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.*;
import static com.tecknobit.pandorocore.helpers.PandoroInputsValidator.INSTANCE;


// TODO: 30/11/2024 TO MAP THE ERROR KEYS HARDCODED AS CONSTANT IN PandoroInputsValidator

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
     * {@code projectsHelper} instance to manage the projects database operations
     */
    @Autowired
    private ProjectsHelper projectsHelper;

    /**
     * {@code groupsHelper} instance to manage the groups database operations
     */
    @Autowired
    private GroupsHelper groupsHelper;

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
            return (T) successResponse(projectsHelper.getAuthoredProjects(id));
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
            return (T) successResponse(projectsHelper.getInDevelopmentProjects(id, page, pageSize, filters));
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
            return (T) successResponse(projectsHelper.getProjects(id, page, pageSize, filters));
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
            return failedResponse("wrong_project_name_key");
        boolean isAdding = projectId == null;
        if (!isAdding) {
            Project currentEditingProject = projectsHelper.getProjectById(projectId);
            if (currentEditingProject == null || !currentEditingProject.getAuthor().getId().equals(id))
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        }
        String description = payload.project_description();
        if (!INSTANCE.isValidProjectDescription(description))
            return failedResponse("wrong_project_description_key");
        String version = payload.project_version();
        if (!INSTANCE.isValidVersion(version))
            return failedResponse("wrong_project_version_key");
        List<String> groups = payload.groups();
        ArrayList<String> adminGroups = new ArrayList<>();
        for (Group group : me.getAdminGroups())
            adminGroups.add(group.getId());
        if (adminGroups.isEmpty()) {
            me.setGroups(groupsHelper.getCompleteGroupsList(id));
            for (Group group : me.getAdminGroups())
                adminGroups.add(group.getId());
        }
        if (!groups.isEmpty() && !adminGroups.containsAll(groups))
            return failedResponse("wrong_groups_list_key");
        String repository = payload.project_repository();
        if (!INSTANCE.isValidRepository(repository))
            return failedResponse("wrong_project_repository_key");
        if (isAdding)
            projectId = generateIdentifier();
        try {
            projectsHelper.workWithProject(id, projectId, payload, isAdding);
        } catch (Exception e) {
            return failedResponse("project_name_already_exists_key");
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
        Project project = projectsHelper.getProject(id, projectId);
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
        Project project = projectsHelper.getProjectById(projectId);
        if (project == null || !project.getAuthor().getId().equals(id))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        projectsHelper.deleteProject(id, projectId);
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
        if (projectsHelper.getProject(id, projectId) == null)
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String targetVersion = jsonHelper.getString(UPDATE_TARGET_VERSION_KEY);
        if (!INSTANCE.isValidVersion(targetVersion))
            return failedResponse("wrong_target_version_key");
        if (projectsHelper.targetVersionExists(projectId, targetVersion))
            return failedResponse("update_version_already_exists_key");
        List<String> changeNotes = Arrays.asList(jsonHelper.getString(UPDATE_CHANGE_NOTES_KEY).split(","));
        if (!INSTANCE.areNotesValid(changeNotes))
            return failedResponse("wrong_change_notes_list_key");
        projectsHelper.scheduleUpdate(generateIdentifier(), targetVersion, changeNotes, projectId, id);
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
        ProjectUpdate update = projectsHelper.updateExists(projectId, updateId);
        if (projectsHelper.getProject(id, projectId) == null || update == null)
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        UpdateStatus status = update.getStatus();
        if (isPublishing) {
            if (status != IN_DEVELOPMENT)
                return failedResponse("wrong_publish_update_request_key");
            projectsHelper.publishUpdate(projectId, updateId, id, update.getTargetVersion());
        } else {
            if (status != SCHEDULED)
                return failedResponse("wrong_development_update_request_key");
            projectsHelper.startUpdate(projectId, updateId, id);
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
        if (isMe(id, token)) {
            ProjectUpdate update = projectsHelper.updateExists(projectId, updateId);
            if (projectsHelper.getProject(id, projectId) != null && update != null && update.getStatus() != PUBLISHED) {
                loadJsonHelper(payload);
                String contentNote = jsonHelper.getString(CONTENT_NOTE_KEY);
                if (INSTANCE.isContentNoteValid(contentNote)) {
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
        return manageChangeNote(id, token, projectId, updateId, noteId, "markAsDone");
    }

    /**
     * Method to mark a change note as todo
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where mark as todo the change note of an update
     * @param updateId The identifier of the update where mark as todo the change note
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
        return manageChangeNote(id, token, projectId, updateId, noteId, "markAsToDo");
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
        return manageChangeNote(id, token, projectId, updateId, noteId, "deleteChangeNote");
    }

    /**
     * Method to manage a change note
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param projectId The identifier of the project where manage the change note
     * @param updateId The identifier of the update where manage the change note
     * @param noteId The identifier of the note
     * @param ope The operation to execute
     *
     * @return the result of the request as {@link String}
     */
    private String manageChangeNote(String id, String token, String projectId, String updateId, String noteId,
                                    String ope) {
        if (isMe(id, token)) {
            ProjectUpdate update = projectsHelper.updateExists(projectId, updateId);
            if (projectsHelper.getProject(id, projectId) != null && update != null &&
                    projectsHelper.changeNoteExists(updateId, noteId)) {
                boolean isInDevelopment = update.getStatus() == IN_DEVELOPMENT;
                switch (ope) {
                    case "markAsDone" -> {
                        if (isInDevelopment)
                            projectsHelper.markChangeNoteAsDone(updateId, noteId, id);
                        else
                            return failedResponse(WRONG_PROCEDURE_MESSAGE);
                    }
                    case "markAsToDo" -> {
                        if (isInDevelopment)
                            projectsHelper.markChangeNoteAsToDo(updateId, noteId);
                        else
                            return failedResponse(WRONG_PROCEDURE_MESSAGE);
                    }
                    default -> {
                        if (update.getStatus() != PUBLISHED)
                            projectsHelper.deleteChangeNote(updateId, noteId);
                        else
                            return failedResponse(WRONG_PROCEDURE_MESSAGE);
                    }
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
        if (projectsHelper.getProject(id, projectId) == null || projectsHelper.updateExists(projectId, updateId) == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        projectsHelper.deleteUpdate(projectId, updateId, id);
        return successResponse();
    }

}
