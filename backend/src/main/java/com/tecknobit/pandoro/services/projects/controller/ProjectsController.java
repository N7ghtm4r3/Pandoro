package com.tecknobit.pandoro.services.projects.controller;

import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.groups.model.Group;
import com.tecknobit.pandoro.services.groups.service.GroupsHelper;
import com.tecknobit.pandoro.services.projects.models.Project;
import com.tecknobit.pandoro.services.projects.models.ProjectUpdate;
import com.tecknobit.pandoro.services.projects.service.ProjectsHelper;
import com.tecknobit.pandorocore.enums.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.pandoro.services.notes.controller.NotesController.WRONG_CONTENT_NOTE_MESSAGE;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.*;
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
     * {@code projectsHelper} instance to manage the projects database operations
     */
    private final ProjectsHelper projectsHelper;

    /**
     * {@code groupsHelper} instance to manage the groups database operations
     */
    private final GroupsHelper groupsHelper;

    /**
     * Constructor to init the {@link ProjectsController} controller
     *
     * @param projectsHelper: instance to manage the projects database operations
     * @param groupsHelper: instance to manage the groups database operations
     */
    @Autowired
    public ProjectsController(ProjectsHelper projectsHelper, GroupsHelper groupsHelper) {
        this.projectsHelper = projectsHelper;
        this.groupsHelper = groupsHelper;
    }

    /**
     * Method to get a projects list
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     *
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects", method = GET)
    public <T> T getProjectsList(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isMe(id, token))
            return (T) successResponse(projectsHelper.getProjectsList(id));
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
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects", method = POST)
    public String addProject(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody Map<String, Object> payload
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
            @RequestBody Map<String, Object> payload
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
    private String workWithProject(String id, String token, Map<String, Object> payload, String projectId) {
        if (isMe(id, token)) {
            loadJsonHelper(payload);
            String name = jsonHelper.getString(NAME_KEY);
            boolean isAdding = projectId == null;
            if (INSTANCE.isValidProjectName(name)) {
                if (!isAdding) {
                    Project currentEditingProject = projectsHelper.getProjectById(projectId);
                    if (currentEditingProject == null || !currentEditingProject.getAuthor().getId().equals(id))
                        return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                }
                Project checkProject = projectsHelper.getProjectByName(id, name);
                if (checkProject == null || (!isAdding && checkProject.getId().equals(projectId))) {
                    String description = jsonHelper.getString(PROJECT_DESCRIPTION_KEY);
                    if (INSTANCE.isValidProjectDescription(description)) {
                        String shortDescription = jsonHelper.getString(PROJECT_SHORT_DESCRIPTION_KEY);
                        if (INSTANCE.isValidProjectShortDescription(shortDescription)) {
                            String version = jsonHelper.getString(PROJECT_VERSION_KEY);
                            if (INSTANCE.isValidVersion(version)) {
                                ArrayList<String> groups = jsonHelper.fetchList(GROUPS_KEY, new ArrayList<>());
                                ArrayList<String> adminGroups = new ArrayList<>();
                                for (Group group : me.getAdminGroups())
                                    adminGroups.add(group.getId());
                                if (adminGroups.isEmpty()) {
                                    me.setGroups(groupsHelper.getGroups(id));
                                    for (Group group : me.getAdminGroups())
                                        adminGroups.add(group.getId());
                                }
                                if (groups.isEmpty() || adminGroups.containsAll(groups)) {
                                    String repository = jsonHelper.getString(PROJECT_REPOSITORY_KEY);
                                    if (INSTANCE.isValidRepository(repository)) {
                                        if (isAdding)
                                            projectId = generateIdentifier();
                                        projectsHelper.workWithProject(id, projectId, name, description, shortDescription,
                                                version, repository, groups, isAdding);
                                        return successResponse();
                                    } else
                                        return failedResponse("wrong_project_repository_key");
                                } else
                                    return failedResponse("wrong_groups_list_key");
                            } else
                                return failedResponse("wrong_project_version_key");
                        } else
                            return failedResponse("wrong_project_short_description_key");
                    } else
                        return failedResponse("wrong_project_description_key");
                } else
                    return failedResponse("project_name_already_exists_key");
            } else
                return failedResponse("wrong_project_name_key");
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
        if (isMe(id, token)) {
            Project project = projectsHelper.getProject(id, projectId);
            if (project != null)
                return (T) successResponse(project);
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
        if (isMe(id, token)) {
            Project project = projectsHelper.getProjectById(projectId);
            if (project != null && project.getAuthor().getId().equals(id)) {
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
        if (isMe(id, token)) {
            if (projectsHelper.getProject(id, projectId) != null) {
                loadJsonHelper(payload);
                String targetVersion = jsonHelper.getString(UPDATE_TARGET_VERSION_KEY);
                if (INSTANCE.isValidVersion(targetVersion)) {
                    if (!projectsHelper.targetVersionExists(projectId, targetVersion)) {
                        ArrayList<String> changeNotes = jsonHelper.fetchList(UPDATE_CHANGE_NOTES_KEY);
                        if (INSTANCE.areNotesValid(changeNotes)) {
                            projectsHelper.scheduleUpdate(generateIdentifier(), targetVersion, changeNotes, projectId, id);
                            return successResponse();
                        } else
                            return failedResponse("wrong_change_notes_list_key");
                    } else
                        return failedResponse("update_version_already_exists_key");
                } else
                    return failedResponse("wrong_target_version_key");
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/start", method = PATCH)
    public String startUpdate(
            @PathVariable(IDENTIFIER_KEY) String id,
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/publish", method = PATCH)
    public String publishUpdate(
            @PathVariable(IDENTIFIER_KEY) String id,
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
        if (isMe(id, token)) {
            ProjectUpdate update = projectsHelper.updateExists(projectId, updateId);
            if (projectsHelper.getProject(id, projectId) != null && update != null) {
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
     * @param payload: the payload with the content of the change note
     *
     * @return the result of the request as {@link String}
     */
    @PutMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + ADD_CHANGE_NOTE_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/addChangeNote", method = PUT)
    public String addChangeNote(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId,
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
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param projectId: the identifier of the project where delete the update
     * @param updateId: the identifier of the update to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}", method = DELETE)
    public String deleteUpdate(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId
    ) {
        if (isMe(id, token)) {
            if (projectsHelper.getProject(id, projectId) != null &&
                    projectsHelper.updateExists(projectId, updateId) != null) {
                projectsHelper.deleteUpdate(projectId, updateId, id);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
