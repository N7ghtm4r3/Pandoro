package com.tecknobit.pandoro.services.projects.service;

import com.tecknobit.pandoro.helpers.ChangelogsCreator.ChangelogOperator;
import com.tecknobit.pandoro.services.groups.model.Group;
import com.tecknobit.pandoro.services.groups.repositories.GroupMembersRepository;
import com.tecknobit.pandoro.services.groups.repositories.GroupsRepository;
import com.tecknobit.pandoro.services.notes.repository.NotesRepository;
import com.tecknobit.pandoro.services.projects.models.Project;
import com.tecknobit.pandoro.services.projects.models.ProjectUpdate;
import com.tecknobit.pandoro.services.projects.repositories.ProjectsRepository;
import com.tecknobit.pandoro.services.projects.repositories.UpdatesRepository;
import com.tecknobit.pandoro.services.users.models.GroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController.generateIdentifier;
import static com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper.InsertCommand.INSERT_IGNORE_INTO;
import static com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper.InsertCommand.INSERT_INTO;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.UpdateStatus.SCHEDULED;

/**
 * The {@code ProjectsHelper} class is useful to manage all the projects database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ChangelogOperator
 */
@Service
public class ProjectsHelper extends ChangelogOperator {

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
     * {@code groupsRepository} instance for the groups repository
     */
    @Autowired
    private GroupsRepository groupsRepository;

    /**
     * {@code groupMembersRepository} instance for the group members repository
     */
    @Autowired
    private GroupMembersRepository groupMembersRepository;

    /**
     * Method to get the user's projects list
     *
     * @param userId The user identifier
     * @return the projects list as {@link List} of {@link Project}
     * @apiNote also the projects of a group in which he is a member are returned
     */
    public List<Project> getProjectsList(String userId) {
        return projectsRepository.getProjectsList(userId);
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
     * @param name The name of the project
     * @param description The description of the project
     * @param version The version of the project
     * @param repository The GitHub or Gitlab repository url of the project
     * @param groups The groups of the project
     * @param isAdding: whether is the adding operation that need to be executed
     */
    public void workWithProject(String userId, String projectId, String name, String description, String version,
                                String repository, ArrayList<String> groups, boolean isAdding) {
        if (isAdding) {
            projectsRepository.insertProject(
                    userId,
                    projectId,
                    name,
                    System.currentTimeMillis(),
                    description,
                    version,
                    repository
            );
            addGroupsToAProject(groups, projectId);
        } else {
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
    private void addGroupsToAProject(ArrayList<String> groups, String projectId) {
        batchInsert(INSERT_INTO, PROJECTS_GROUPS_TABLE, groups, query -> {
            int index = 1;
            for (String group : groups) {
                query.setParameter(index++, projectId);
                query.setParameter(index++, group);
            }
        }, PROJECT_IDENTIFIER_KEY, GROUP_IDENTIFIER_KEY);
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
    }

    /**
     * Method to check whether an update with the target version inserted already exists
     *
     * @param projectId The project identifier
     * @param targetVersion The target version to check
     * @return whether an update with the target version inserted already exists as boolean
     */
    public boolean targetVersionExists(String projectId, String targetVersion) {
        return updatesRepository.getUpdateByVersion(projectId, targetVersion) != null;
    }

    /**
     * Method to fetch and check if an update exists
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @return project update, if exists, as {@link ProjectUpdate}, null if not
     */
    public ProjectUpdate updateExists(String projectId, String updateId) {
        return updatesRepository.getUpdateById(projectId, updateId);
    }

    /**
     * Method to schedule a new update
     *
     * @param updateId The update identifier
     * @param targetVersion The target version of the new update
     * @param changeNotes The change notes of the new update
     * @param userId The user identifier
     * @param projectId The project identifier
     */
    public void scheduleUpdate(String updateId, String targetVersion, ArrayList<String> changeNotes,
                               String projectId, String userId) {
        updatesRepository.scheduleUpdate(updateId, targetVersion, System.currentTimeMillis(), SCHEDULED,
                projectId, userId);
        batchInsert(INSERT_IGNORE_INTO, NOTES_KEY, changeNotes, query -> {
            int index = 1;
            for (String changeNote : changeNotes) {
                query.setParameter(index++, generateIdentifier());
                query.setParameter(index++, userId);
                query.setParameter(index++, changeNote);
                query.setParameter(index++, System.currentTimeMillis());
                query.setParameter(index++, updateId);
            }
        }, IDENTIFIER_KEY, AUTHOR_KEY, CONTENT_NOTE_KEY, CREATION_DATE_KEY, UPDATE_KEY);
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
     * @param updateVersion The version of the update to set as last version of the project
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
     * Method to add a change note to an update
     *
     * @param userId The user identifier
     * @param noteId The identifier of the note to add
     * @param contentNote The content of the note to add
     * @param updateId The update identifier
     */
    public void addChangeNote(String userId, String noteId, String contentNote, String updateId) {
        notesRepository.addChangeNote(userId, noteId, contentNote, System.currentTimeMillis(), updateId);
    }

    /**
     * Method to check whether a change note exists
     *
     * @param noteId The identifier of the note to add
     * @param updateId The update identifier
     * @return whether the change note exists as boolean
     */
    public boolean changeNoteExists(String updateId, String noteId) {
        return notesRepository.getNoteByUpdate(updateId, noteId) != null;
    }

    /**
     * Method to mark as done a change note
     *
     * @param updateId The update identifier
     * @param noteId The identifier of the note
     * @param userId The user identifier
     */
    public void markChangeNoteAsDone(String updateId, String noteId, String userId) {
        notesRepository.manageChangeNoteStatus(updateId, noteId, true, userId, System.currentTimeMillis());
    }

    /**
     * Method to mark as todo a change note
     *
     * @param updateId The update identifier
     * @param noteId The identifier of the note
     */
    public void markChangeNoteAsToDo(String updateId, String noteId) {
        notesRepository.manageChangeNoteStatus(updateId, noteId, false, null, -1);
    }

    /**
     * Method to delete a change note
     *
     * @param updateId The update identifier
     * @param noteId The identifier of the note
     */
    public void deleteChangeNote(String updateId, String noteId) {
        notesRepository.deleteChangeNote(updateId, noteId);
    }

    /**
     * Method to delete an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param userId The user identifier
     */
    public void deleteUpdate(String projectId, String updateId, String userId) {
        ProjectUpdate update = updatesRepository.getUpdateById(projectId, updateId);
        updatesRepository.deleteUpdate(updateId);
        if (projectsRepository.getProjectById(projectId).hasGroups())
            changelogsCreator.updateDeleted(update.getTargetVersion(), projectId, userId);
    }

}
