package com.tecknobit.pandoro.services.projects.services;

import com.tecknobit.equinoxbackend.annotations.TableColumns;
import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import com.tecknobit.pandoro.configuration.PandoroResourcesManager;
import com.tecknobit.pandoro.services.changelogs.helpers.ChangelogsCreator.ChangelogOperator;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.groups.repositories.GroupMembersRepository;
import com.tecknobit.pandoro.services.groups.repositories.GroupsRepository;
import com.tecknobit.pandoro.services.projects.dto.ProjectDTO;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.projects.repositories.ProjectsRepository;
import com.tecknobit.pandoro.services.projects.repositories.UpdatesRepository;
import com.tecknobit.pandoro.services.users.entities.GroupMember;
import com.tecknobit.pandorocore.enums.UpdateStatus;
import jakarta.persistence.Query;
import kotlin.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController.generateIdentifier;
import static com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper.InsertCommand.INSERT_IGNORE_INTO;
import static com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper.InsertCommand.INSERT_INTO;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT;
import static com.tecknobit.pandorocore.enums.UpdateStatus.SCHEDULED;

/**
 * The {@code ProjectsService} class is useful to manage all the projects database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ChangelogOperator
 */
@Service
public class ProjectsService extends ChangelogOperator implements PandoroResourcesManager {

    /**
     * {@code PROJECT_NAME_REGEX} regex to validate the project name
     */
    private static final String PROJECT_NAME_REGEX = "^[a-zA-Z0-9_-]{1,15}$";

    /**
     * {@code PROJECT_NAME_PATTERN} the pattern to validate the project names values
     */
    private static final Pattern PROJECT_NAME_PATTERN = Pattern.compile(PROJECT_NAME_REGEX);

    /**
     * {@code PROJECT_VERSION_REGEX} regex to validate the project version
     */
    private static final String PROJECT_VERSION_REGEX = "^v?(?:\\d+[-.]?)+(-[a-zA-Z]+\\d*)?$";

    /**
     * {@code PROJECT_VERSION_PATTERN} the pattern to validate the project version
     */
    private static final Pattern PROJECT_VERSION_PATTERN = Pattern.compile(PROJECT_VERSION_REGEX);

    /**
     * {@code projectsRepository} instance for the projects repository
     */
    private final ProjectsRepository projectsRepository;

    /**
     * {@code updatesRepository} instance for the updates repository
     */
    private final UpdatesRepository updatesRepository;

    /**
     * {@code groupsRepository} instance for the groups repository
     */
    private final GroupsRepository groupsRepository;

    /**
     * {@code groupMembersRepository} instance for the group members repository
     */
    private final GroupMembersRepository groupMembersRepository;

    /**
     * Constructor used to init the service
     *
     * @param projectsRepository     The instance for the projects repository
     * @param updatesRepository      The instance for the updates repository
     * @param groupsRepository       The instance for the groups repository
     * @param groupMembersRepository The instance for the group members repository
     */
    @Autowired
    public ProjectsService(ProjectsRepository projectsRepository, UpdatesRepository updatesRepository,
                           GroupsRepository groupsRepository, GroupMembersRepository groupMembersRepository) {
        this.projectsRepository = projectsRepository;
        this.updatesRepository = updatesRepository;
        this.groupsRepository = groupsRepository;
        this.groupMembersRepository = groupMembersRepository;
    }

    /**
     * Method to get the user's authored projects list
     *
     * @param userId The user identifier
     * @return the projects list as {@link PaginatedResponse} of {@link Project}
     */
    public List<Project> getAuthoredProjects(String userId) {
        return projectsRepository.getAuthoredProjects(userId);
    }

    /**
     * Method to get the user's {@link UpdateStatus#IN_DEVELOPMENT} projects list
     *
     * @param userId   The user identifier
     * @param page     The page requested
     * @param pageSize The size of the items to insert in the page
     * @param filters The filter to apply to the query to select the project
     * @return the projects list as {@link PaginatedResponse} of {@link Project}
     * @apiNote also the projects of a group in which he is a member are returned
     */
    public PaginatedResponse<Project> getInDevelopmentProjects(String userId, int page, int pageSize,
                                                               Set<String> filters) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Pair<String, Set<String>> filtersSet = extractProjectFilters(filters);
        String projectNameFilter = filtersSet.getFirst();
        Set<String> versionsFilter = filtersSet.getSecond();
        List<Project> projects = projectsRepository.getInDevelopmentProjects(
                userId,
                projectNameFilter,
                versionsFilter,
                pageable
        );
        for (Project project : projects) {
            ArrayList<Update> updates = project.getUpdates();
            updates.removeIf(update -> update.getStatus() != IN_DEVELOPMENT);
            project.setUpdates(updates);
        }
        long projectsCount = projectsRepository.getCompleteInDevelopmentProjectsList(
                userId,
                projectNameFilter,
                versionsFilter
        ).size();
        return new PaginatedResponse<>(projects, page, pageSize, projectsCount);
    }

    /**
     * Method to get the user's projects list
     *
     * @param userId The user identifier
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param filters The filter to apply to the query to select the project
     *
     * @return the projects list as {@link PaginatedResponse} of {@link Project}
     *
     * @apiNote also the projects of a group in which he is a member are returned
     */
    public PaginatedResponse<Project> getProjects(String userId, int page, int pageSize, Set<String> filters) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Pair<String, Set<String>> filtersSet = extractProjectFilters(filters);
        String projectNameFilter = filtersSet.getFirst();
        Set<String> versionsFilter = filtersSet.getSecond();
        List<Project> projects = projectsRepository.getProjects(userId, projectNameFilter, versionsFilter, pageable);
        long projectsCount = projectsRepository.getCompleteProjectsList(userId, projectNameFilter, versionsFilter).size();
        return new PaginatedResponse<>(projects, page, pageSize, projectsCount);
    }

    /**
     * Method to select from the raw filter the specific values of the different filters
     *
     * @param rawFilters The raw filters
     * @return the specific filters as {@link Pair} of {@link String} and {@link HashSet} of {@link String}
     */
    private Pair<String, Set<String>> extractProjectFilters(Set<String> rawFilters) {
        String projectName = "";
        HashSet<String> filters = new HashSet<>();
        for (String filter : rawFilters) {
            if (projectName.isEmpty() && PROJECT_NAME_PATTERN.matcher(filter).matches())
                projectName = filter;
            else if (PROJECT_VERSION_PATTERN.matcher(filter).matches())
                filters.add(filter);
        }
        return new Pair<>(projectName, filters);
    }

    /**
     * Method to get the user's project by its id
     *
     * @param projectId The project identifier
     * @return the project as {@link Project}
     */
    public Project getProjectById(String projectId) {
        return projectsRepository.getProjectById(projectId);
    }

    /**
     * Method to get the user's project by its id
     *
     * @param userId The user identifier
     * @param projectId The project identifier
     * @return the project as {@link Project}
     * @apiNote also the project of a group in which he is a member is returned
     */
    public Project getProject(String userId, String projectId) {
        return projectsRepository.getProject(userId, projectId);
    }

    /**
     * Method to work with a project, add or edit operation are executed
     *
     * @param userId The user identifier
     * @param projectId The project identifier
     * @param project The payload with the project details
     * @param isAdding Whether is the adding operation that need to be executed
     */
    public void workWithProject(String userId, String projectId, ProjectDTO project, boolean isAdding) throws IOException {
        MultipartFile icon = project.icon();
        String iconPath = null;
        String name = project.name();
        String description = project.project_description();
        String version = project.project_version();
        String repository = project.project_repository();
        List<String> groups = project.groups();
        long operationDate = System.currentTimeMillis();
        if (icon != null) {
            if (!isAdding)
                deleteProjectIconResource(projectId);
            iconPath = createProjectIconResource(icon, projectId + operationDate);
        }
        if (isAdding) {
            projectsRepository.insertProject(
                    userId,
                    projectId,
                    name,
                    iconPath,
                    operationDate,
                    description,
                    version,
                    repository
            );
            addGroupsToAProject(groups, projectId);
            if (icon != null)
                saveResource(icon, iconPath);
        } else {
            if (iconPath != null) {
                projectsRepository.editProject(userId, projectId, name, iconPath, description, version, repository);
                saveResource(icon, iconPath);
            } else
                projectsRepository.editProject(userId, projectId, name, description, version, repository);
            List<String> currentGroups = projectsRepository.getProjectGroupsIds(projectId);
            currentGroups.removeAll(groups);
            for (String group : currentGroups) {
                projectsRepository.removeProjectGroup(projectId, group);
                List<GroupMember> members = groupMembersRepository.getGroupMembers(group);
                for (GroupMember member : members)
                    changelogsCreator.removedGroupProject(projectId, member.getId());
            }
            groups.removeAll(projectsRepository.getProjectGroupsIds(projectId));
            addGroupsToAProject(groups, projectId);
        }
    }

    /**
     * Method to add groups list to a project
     *
     * @param groups:    the groups list to add
     * @param projectId The project identifier
     */
    private void addGroupsToAProject(List<String> groups, String projectId) {
        batchInsert(INSERT_INTO, PROJECTS_GROUPS_TABLE, new BatchQuery<String>() {
            @Override
            public Collection<String> getData() {
                return groups;
            }

            @Override
            @TableColumns(columns = {PROJECT_IDENTIFIER_KEY, GROUP_IDENTIFIER_KEY})
            public void prepareQuery(Query query, int index, Collection<String> groups) {
                for (String group : groups) {
                    query.setParameter(index++, projectId);
                    query.setParameter(index++, group);
                }
            }

            @Override
            public String[] getColumns() {
                return new String[]{PROJECT_IDENTIFIER_KEY, GROUP_IDENTIFIER_KEY};
            }
        });
        for (String group : groups) {
            List<GroupMember> members = groupMembersRepository.getGroupMembers(group);
            for (GroupMember member : members)
                changelogsCreator.addedGroupProject(projectId, member.getId());
        }
    }

    /**
     * Method to delete a user's project
     *
     * @param userId The user identifier
     * @param projectId The project identifier
     */
    public void deleteProject(String userId, String projectId) {
        Project project = getProjectById(projectId);
        if (project.hasGroups())
            for (Group group : project.getGroups())
                groupsRepository.removeGroupProject(projectId, group.getId());
        projectsRepository.deleteProject(userId, projectId);
        deleteProjectIconResource(projectId);
    }

    /**
     * Method to check whether an update with the target project_version inserted already exists
     *
     * @param projectId The project identifier
     * @param targetVersion The target project_version to check
     * @return whether an update with the target project_version inserted already exists as boolean
     */
    public boolean targetVersionExists(String projectId, String targetVersion) {
        return updatesRepository.getUpdateByVersion(projectId, targetVersion) != null;
    }

    /**
     * Method to fetch and check if an update exists
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @return project update, if exists, as {@link Update}, null if not
     */
    public Update updateExists(String projectId, String updateId) {
        return updatesRepository.getUpdateById(projectId, updateId);
    }

    /**
     * Method to schedule a new update
     *
     * @param updateId The update identifier
     * @param targetVersion The target project_version of the new update
     * @param changeNotes The change notes of the new update
     * @param userId The user identifier
     * @param projectId The project identifier
     */
    public void scheduleUpdate(String updateId, String targetVersion, List<String> changeNotes,
                               String projectId, String userId) {
        updatesRepository.scheduleUpdate(updateId, targetVersion, System.currentTimeMillis(), SCHEDULED,
                projectId, userId);
        batchInsert(INSERT_IGNORE_INTO, NOTES_KEY, new BatchQuery<String>() {
            @Override
            public Collection<String> getData() {
                return changeNotes;
            }

            @Override
            @TableColumns(columns = {IDENTIFIER_KEY, AUTHOR_KEY, CONTENT_NOTE_KEY, CREATION_DATE_KEY, UPDATE_KEY})
            public void prepareQuery(Query query, int index, Collection<String> changeNotes) {
                for (String changeNote : changeNotes) {
                    query.setParameter(index++, generateIdentifier());
                    query.setParameter(index++, userId);
                    query.setParameter(index++, changeNote);
                    query.setParameter(index++, System.currentTimeMillis());
                    query.setParameter(index++, updateId);
                }
            }

            @Override
            public String[] getColumns() {
                return new String[]{IDENTIFIER_KEY, AUTHOR_KEY, CONTENT_NOTE_KEY, CREATION_DATE_KEY, UPDATE_KEY};
            }
        });
        if (projectsRepository.getProjectById(projectId).hasGroups())
            changelogsCreator.scheduledNewUpdate(targetVersion, projectId, userId);
    }

    /**
     * Method to start an existing update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param userId The user identifier who start the update
     */
    public void startUpdate(String projectId, String updateId, String userId) {
        updatesRepository.startUpdate(updateId, System.currentTimeMillis(), userId);
        if (projectsRepository.getProjectById(projectId).hasGroups()) {
            changelogsCreator.updateStarted(updatesRepository.getUpdateById(projectId, updateId).getTargetVersion(),
                    projectId, userId);
        }
    }

    /**
     * Method to publish an existing update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param userId The user identifier who publish the update
     * @param updateVersion The project_version of the update to set as last project_version of the project
     */
    public void publishUpdate(String projectId, String updateId, String userId, String updateVersion) {
        updatesRepository.publishUpdate(updateId, System.currentTimeMillis(), userId);
        projectsRepository.updateProjectVersion(userId, projectId, updateVersion);
        if (projectsRepository.getProjectById(projectId).hasGroups()) {
            changelogsCreator.updatePublished(updatesRepository.getUpdateById(projectId, updateId).getTargetVersion(),
                    projectId, userId);
        }
    }

    /**
     * Method to delete an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param userId The user identifier
     */
    public void deleteUpdate(String projectId, String updateId, String userId) {
        Update update = updatesRepository.getUpdateById(projectId, updateId);
        updatesRepository.deleteUpdate(updateId);
        if (projectsRepository.getProjectById(projectId).hasGroups())
            changelogsCreator.updateDeleted(update.getTargetVersion(), projectId, userId);
    }

}
