package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.helpers.ChangelogsCreator.ChangelogOperator;
import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.records.ProjectUpdate;
import com.tecknobit.pandoro.records.users.GroupMember;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
import com.tecknobit.pandoro.services.repositories.projects.ProjectsRepository;
import com.tecknobit.pandoro.services.repositories.projects.UpdatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.controllers.PandoroController.generateIdentifier;
import static com.tecknobit.pandoro.records.ProjectUpdate.Status.SCHEDULED;

/**
 * The {@code ProjectsHelper} class is useful to manage all the projects database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ChangelogOperator
 */
@Service
public class ProjectsHelper extends ChangelogOperator {

    /**
     * {@code PROJECTS_KEY} projects key
     */
    public static final String PROJECTS_KEY = "projects";

    /**
     * {@code PROJECT_IDENTIFIER_KEY} project identifier key
     */
    public static final String PROJECT_IDENTIFIER_KEY = "project_id";

    /**
     * {@code PROJECTS_GROUPS_TABLE} projects groups table
     */
    public static final String PROJECTS_GROUPS_TABLE = "projects_groups";

    /**
     * {@code PROJECT_KEY} project key
     */
    public static final String PROJECT_KEY = "project";

    /**
     * {@code PROJECT_SHORT_DESCRIPTION_KEY} project short description key
     */
    public static final String PROJECT_SHORT_DESCRIPTION_KEY = "project_short_description";

    /**
     * {@code PROJECT_DESCRIPTION_KEY} project description key
     */
    public static final String PROJECT_DESCRIPTION_KEY = "project_description";

    /**
     * {@code PROJECT_VERSION_KEY} project version key
     */
    public static final String PROJECT_VERSION_KEY = "project_version";

    /**
     * {@code PROJECT_REPOSITORY_KEY} project repository key
     */
    public static final String PROJECT_REPOSITORY_KEY = "project_repository";

    /**
     * {@code UPDATES_KEY} updates key
     */
    public static final String UPDATES_KEY = "updates";

    /**
     * {@code UPDATE_ID} update identifier key
     */
    public static final String UPDATE_ID = "update_id";

    /**
     * {@code UPDATE_KEY} project update key
     */
    public static final String UPDATE_KEY = "project_update";

    /**
     * {@code UPDATE_CREATE_DATE_KEY} create date key
     */
    public static final String UPDATE_CREATE_DATE_KEY = "create_date";

    /**
     * {@code UPDATE_TARGET_VERSION_KEY} target version key
     */
    public static final String UPDATE_TARGET_VERSION_KEY = "target_version";

    /**
     * {@code UPDATE_CHANGE_NOTES_KEY} update change notes key
     */
    public static final String UPDATE_CHANGE_NOTES_KEY = "update_change_notes";

    /**
     * {@code UPDATE_STATUS_KEY} update status key
     */
    public static final String UPDATE_STATUS_KEY = "status";

    /**
     * {@code UPDATE_STARTED_BY_KEY} started by key
     */
    public static final String UPDATE_STARTED_BY_KEY = "started_by";

    /**
     * {@code UPDATE_START_DATE_KEY} start date key
     */
    public static final String UPDATE_START_DATE_KEY = "start_date";

    /**
     * {@code UPDATE_PUBLISHED_BY_KEY} published by key
     */
    public static final String UPDATE_PUBLISHED_BY_KEY = "published_by";

    /**
     * {@code UPDATE_PUBLISH_DATE_KEY} publish date key
     */
    public static final String UPDATE_PUBLISH_DATE_KEY = "publish_date";

    /**
     * {@code projectsRepository} instance for the projects repository
     */
    @Autowired
    private ProjectsRepository projectsRepository;

    /**
     * {@code updatesRepository} instance for the updates repository
     */
    @Autowired
    private UpdatesRepository updatesRepository;

    /**
     * {@code notesRepository} instance for the notes repository
     */
    @Autowired
    private NotesRepository notesRepository;

    /**
     * {@code groupMembersRepository} instance for the group members repository
     */
    @Autowired
    private GroupMembersRepository groupMembersRepository;

    /**
     * Method to get the user's projects list
     *
     * @param userId: the user identifier
     * @return the projects list as {@link List} of {@link Project}
     * @apiNote also the projects of a group in which he is a member are returned
     */
    public List<Project> getProjectsList(String userId) {
        return projectsRepository.getProjectsList(userId);
    }

    /**
     * Method to get the user's project by its name
     *
     * @param userId:      the user identifier
     * @param projectName: the name of the project
     * @return the project as {@link Project}
     * @apiNote also the project of a group in which he is a member is returned
     */
    public Project getProjectByName(String userId, String projectName) {
        return projectsRepository.getProjectByName(userId, projectName);
    }

    /**
     * Method to get the user's project by its id
     *
     * @param userId: the user identifier
     * @param projectId: the project identifier
     * @return the project as {@link Project}
     */
    public Project getProjectById(String userId, String projectId) {
        return projectsRepository.getProjectById(userId, projectId);
    }

    /**
     * Method to get the user's project by its id
     *
     * @param userId: the user identifier
     * @param projectId: the project identifier
     * @return the project as {@link Project}
     * @apiNote also the project of a group in which he is a member is returned
     */
    public Project getProject(String userId, String projectId) {
        return projectsRepository.getProject(userId, projectId);
    }

    /**
     * Method to work with a project, add or edit operation are executed
     *
     * @param userId: the user identifier
     * @param projectId: the project identifier
     * @param name: the name of the project
     * @param description: the description of the project
     * @param shortDescription: the short description of the project
     * @param version: the version of the project
     * @param repository: the GitHub or Gitlab repository url of the project
     * @param groups: the groups of the project
     * @param isAdding: whether is the adding operation that need to be executed
     */
    public void workWithProject(String userId, String projectId, String name, String description, String shortDescription,
                                String version, String repository, ArrayList<String> groups, boolean isAdding) {
        if (isAdding) {
            projectsRepository.insertProject(userId, projectId, name, description, shortDescription, version, repository);
            addGroupsToAProject(groups, projectId);
        } else {
            projectsRepository.editProject(userId, projectId, name, description, shortDescription, version, repository);
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
     * @param projectId: the project identifier
     */
    private void addGroupsToAProject(ArrayList<String> groups, String projectId) {
        for (String group : groups) {
            projectsRepository.addProjectGroup(projectId, group);
            List<GroupMember> members = groupMembersRepository.getGroupMembers(group);
            for (GroupMember member : members)
                changelogsCreator.addedGroupProject(projectId, member.getId());
        }
    }

    /**
     * Method to delete a user's project
     *
     * @param userId: the user identifier
     * @param projectId: the project identifier
     */
    public void deleteProject(String userId, String projectId) {
        projectsRepository.deleteProject(userId, projectId);
    }

    /**
     * Method to check whether an update with the target version inserted already exists
     *
     * @param projectId: the project identifier
     * @param targetVersion: the target version to check
     * @return whether an update with the target version inserted already exists as boolean
     */
    public boolean targetVersionExists(String projectId, String targetVersion) {
        return updatesRepository.getUpdateByVersion(projectId, targetVersion) != null;
    }

    /**
     * Method to fetch and check if an update exists
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @return project update, if exists, as {@link ProjectUpdate}, null if not
     */
    public ProjectUpdate updateExists(String projectId, String updateId) {
        return updatesRepository.getUpdateById(projectId, updateId);
    }

    /**
     * Method to schedule a new update
     *
     * @param updateId: the update identifier
     * @param targetVersion: the target version of the new update
     * @param changeNotes: the change notes of the new update
     * @param userId: the user identifier
     * @param projectId: the project identifier
     */
    public void scheduleUpdate(String updateId, String targetVersion, ArrayList<String> changeNotes,
                               String projectId, String userId) {
        updatesRepository.scheduleUpdate(updateId, targetVersion, System.currentTimeMillis(), SCHEDULED,
                projectId, userId);
        for (String note : changeNotes)
            notesRepository.addChangeNote(userId, generateIdentifier(), note, System.currentTimeMillis(), updateId);
        changelogsCreator.scheduledNewUpdate(targetVersion, projectId, userId);
    }

    /**
     * Method to start an existing update
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @param userId: the user identifier who start the update
     */
    public void startUpdate(String projectId, String updateId, String userId) {
        updatesRepository.startUpdate(updateId, System.currentTimeMillis(), userId);
        if (projectsRepository.getProjectById(userId, projectId).hasGroups()) {
            changelogsCreator.updateStarted(updatesRepository.getUpdateById(projectId, updateId).getTargetVersion(),
                    projectId, userId);
        }
    }

    /**
     * Method to publish an existing update
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @param userId: the user identifier who publish the update
     */
    public void publishUpdate(String projectId, String updateId, String userId) {
        updatesRepository.publishUpdate(updateId, System.currentTimeMillis(), userId);
        if (projectsRepository.getProjectById(userId, projectId).hasGroups()) {
            changelogsCreator.updatePublished(updatesRepository.getUpdateById(projectId, updateId).getTargetVersion(),
                    projectId, userId);
        }
    }

    /**
     * Method to add a change note to an update
     *
     * @param userId: the user identifier
     * @param noteId: the identifier of the note to add
     * @param contentNote: the content of the note to add
     * @param updateId: the update identifier
     */
    public void addChangeNote(String userId, String noteId, String contentNote, String updateId) {
        notesRepository.addChangeNote(userId, noteId, contentNote, System.currentTimeMillis(), updateId);
    }

    /**
     * Method to check whether a change note exists
     *
     * @param noteId: the identifier of the note to add
     * @param updateId: the update identifier
     * @return whether the change note exists as boolean
     */
    public boolean changeNoteExists(String updateId, String noteId) {
        return notesRepository.getNoteByUpdate(updateId, noteId) != null;
    }

    /**
     * Method to mark as done a change note
     *
     * @param updateId: the update identifier
     * @param noteId: the identifier of the note
     * @param userId: the user identifier
     */
    public void markChangeNoteAsDone(String updateId, String noteId, String userId) {
        notesRepository.manageChangeNoteStatus(updateId, noteId, true, userId, System.currentTimeMillis());
    }

    /**
     * Method to mark as todo a change note
     *
     * @param updateId: the update identifier
     * @param noteId: the identifier of the note
     */
    public void markChangeNoteAsToDo(String updateId, String noteId) {
        notesRepository.manageChangeNoteStatus(updateId, noteId, false, null, -1);
    }

    /**
     * Method to delete a change note
     *
     * @param updateId: the update identifier
     * @param noteId: the identifier of the note
     */
    public void deleteChangeNote(String updateId, String noteId) {
        notesRepository.deleteChangeNote(updateId, noteId);
    }

    /**
     * Method to delete an update
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @param userId: the user identifier
     */
    public void deleteUpdate(String projectId, String updateId, String userId) {
        updatesRepository.deleteUpdate(updateId);
        if (projectsRepository.getProjectById(userId, projectId).hasGroups()) {
            changelogsCreator.updateDeleted(updatesRepository.getUpdateById(projectId, updateId).getTargetVersion(),
                    projectId, userId);
        }
    }

}
