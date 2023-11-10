package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.ProjectsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
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

    public static final String SCHEDULE_UPDATE_ENDPOINT = "/schedule";

    public static final String START_UPDATE_ENDPOINT = "/start";

    public static final String PUBLISH_UPDATE_ENDPOINT = "/publish";

    public static final String ADD_CHANGELOG_NOTE_ENDPOINT = "/addChangelogNote";

    public static final String DELETE_CHANGELOG_NOTE_ENDPOINT = "/deleteChangelogNote";

    public static final String MARK_CHANGELOG_NOTE_AS_DONE_ENDPOINT = "/markChangelogNoteAsDone";

    public static final String MARK_CHANGELOG_NOTE_AS_TODO_ENDPOINT = "/markChangelogNoteAsToDo";

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
                if (!projectsHelper.projectExists(id, name) || !isAdding) {
                    String description = hPayload.getString(PROJECT_DESCRIPTION_KEY);
                    if (isValidProjectDescription(description)) {
                        String shortDescription = hPayload.getString(PROJECT_SHORT_DESCRIPTION_KEY);
                        if (isValidProjectShortDescription(shortDescription)) {
                            String version = hPayload.getString(PROJECT_VERSION_KEY);
                            if (isValidVersion(version)) {
                                ArrayList<String> groups = hPayload.fetchList(GROUPS_KEY);
                                ArrayList<Group> adminGroups = me.getAdminGroups();
                                boolean haveAdminGroups = !adminGroups.isEmpty();
                                boolean correctList = true;
                                if (!groups.isEmpty() && haveAdminGroups) {
                                    for (Group group : adminGroups) {
                                        if (!groups.contains(group.getId())) {
                                            correctList = false;
                                            break;
                                        }
                                    }
                                } else if (haveAdminGroups)
                                    correctList = false;
                                if (correctList) {
                                    String repository = hPayload.getString(PROJECT_REPOSITORY_KEY);
                                    if (isValidRepository(repository)) {
                                        if (isAdding)
                                            projectId = generateIdentifier();
                                        projectsHelper.workWithProject(
                                                id,
                                                projectId,
                                                name,
                                                description,
                                                shortDescription,
                                                version,
                                                repository,
                                                groups,
                                                isAdding
                                        );
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
            path = "/{" + IDENTIFIER_KEY + "}",
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public <T> T getProject(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(IDENTIFIER_KEY) String projectId
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
            Project project = projectsHelper.getProjectById(id, projectId);
            if (project != null) {
                projectsHelper.deleteProject(projectId);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
