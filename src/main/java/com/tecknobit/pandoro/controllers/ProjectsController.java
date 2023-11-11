package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.records.ProjectUpdate;
import com.tecknobit.pandoro.records.ProjectUpdate.Status;
import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.ProjectsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY;
import static com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY;
import static com.tecknobit.pandoro.controllers.NotesController.WRONG_CONTENT_NOTE_MESSAGE;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.records.ProjectUpdate.Status.IN_DEVELOPMENT;
import static com.tecknobit.pandoro.records.ProjectUpdate.Status.SCHEDULED;
import static com.tecknobit.pandoro.services.NotesHelper.NOTE_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.NAME_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY;
import static helpers.InputsValidatorKt.*;

@RestController
@RequestMapping(path = BASE_ENDPOINT + PROJECTS_KEY)
public class ProjectsController extends PandoroController {

    public static final String ADD_PROJECT_ENDPOINT = "/addProject";

    public static final String EDIT_PROJECT_ENDPOINT = "/editProject";

    public static final String DELETE_PROJECT_ENDPOINT = "/deleteProject";

    public static final String UPDATES_PATH = "/updates/";

    public static final String SCHEDULE_UPDATE_ENDPOINT = "schedule";

    public static final String START_UPDATE_ENDPOINT = "/start";

    public static final String PUBLISH_UPDATE_ENDPOINT = "/publish";

    public static final String ADD_CHANGE_NOTE_ENDPOINT = "/addChangeNote";

    public static final String MARK_CHANGE_NOTE_AS_DONE_ENDPOINT = "/markChangeNoteAsDone";

    public static final String MARK_CHANGE_NOTE_AS_TODO_ENDPOINT = "/markChangeNoteAsToDo";

    public static final String DELETE_CHANGE_NOTE_ENDPOINT = "/deleteChangeNote";

    public static final String DELETE_UPDATE_ENDPOINT = "/delete";

    private final ProjectsHelper projectsHelper;

    @Autowired
    public ProjectsController(ProjectsHelper projectsHelper) {
        this.projectsHelper = projectsHelper;
    }

    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public <T> T getProjectsList(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token)) {
            return (T) projectsHelper.getProjectsList(id);
        } else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PostMapping(
            path = ADD_PROJECT_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String addProject(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody String payload
    ) {
        return workWithProject(id, token, payload, null);
    }

    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + EDIT_PROJECT_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String editProject(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @RequestBody String payload
    ) {
        return workWithProject(id, token, payload, projectId);
    }

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

    @GetMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}",
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
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

    @DeleteMapping(
            path = "/{" + IDENTIFIER_KEY + "}" + DELETE_PROJECT_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
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

    @PostMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + SCHEDULE_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
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

    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + START_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String startUpdate(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId
    ) {
        return manageUpdateStatus(id, token, projectId, updateId, false);
    }

    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + PUBLISH_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String publishUpdate(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId
    ) {
        return manageUpdateStatus(id, token, projectId, updateId, true);
    }

    private String manageUpdateStatus(String id, String token, String projectId, String updateId, boolean isPublishing) {
        if (isAuthenticatedUser(id, token)) {
            ProjectUpdate update = projectsHelper.updateExists(projectId, updateId);
            if (projectsHelper.getProject(id, projectId) != null && update != null) {
                Status status = update.getStatus();
                if (isPublishing) {
                    if (status != IN_DEVELOPMENT)
                        return failedResponse("An update to be published must be IN_DEVELOPMENT first");
                    projectsHelper.publishUpdate(updateId, id);
                } else {
                    if (status != SCHEDULED)
                        return failedResponse("An update to be published must be SCHEDULED first");
                    projectsHelper.startUpdate(updateId, id);
                }
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PutMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + ADD_CHANGE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
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

    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + MARK_CHANGE_NOTE_AS_DONE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
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

    @PatchMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + MARK_CHANGE_NOTE_AS_TODO_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
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

    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}/" + NOTES_KEY
                    + "/{" + NOTE_IDENTIFIER_KEY + "}" + DELETE_CHANGE_NOTE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
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

    private String manageChangeNote(String id, String token, String projectId, String updateId, String noteId, String ope) {
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

    @DeleteMapping(
            path = "/{" + PROJECT_IDENTIFIER_KEY + "}" + UPDATES_PATH + "{" + UPDATE_ID + "}" + DELETE_UPDATE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String deleteUpdate(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(PROJECT_IDENTIFIER_KEY) String projectId,
            @PathVariable(UPDATE_ID) String updateId
    ) {
        if (isAuthenticatedUser(id, token)) {
            if (projectsHelper.getProject(id, projectId) != null &&
                    projectsHelper.updateExists(projectId, updateId) != null) {
                projectsHelper.deleteUpdate(updateId);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
