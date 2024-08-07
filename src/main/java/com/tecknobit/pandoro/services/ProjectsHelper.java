package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.helpers.ChangelogsCreator.ChangelogOperator;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupsRepository;
import com.tecknobit.pandoro.services.repositories.projects.ProjectsRepository;
import com.tecknobit.pandoro.services.repositories.projects.UpdatesRepository;
import com.tecknobit.pandorocore.records.Group;
import com.tecknobit.pandorocore.records.Project;
import com.tecknobit.pandorocore.records.ProjectUpdate;
import com.tecknobit.pandorocore.records.users.GroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.equinox.environment.controllers.EquinoxController.generateIdentifier;
import static com.tecknobit.pandorocore.records.ProjectUpdate.Status.SCHEDULED;

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
     * @param projectId: the project identifier
     * @return the project as {@link Project}
     */
    public Project getProjectById(String projectId) {
        return projectsRepository.getProjectById(projectId);
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
            projectsRepository.insertProject(
                    userId,
                    projectId,
                    name,
                    System.currentTimeMillis(),
                    description,
                    shortDescription,
                    version,
                    repository
            );
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
        Project project = getProjectById(projectId);
        if (project.hasGroups())
            for (Group group : project.getGroups())
                groupsRepository.removeGroupProject(projectId, group.getId());
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
        if (projectsRepository.getProjectById(projectId).hasGroups())
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
        if (projectsRepository.getProjectById(projectId).hasGroups()) {
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
     * @param updateVersion: the version of the update to set as last version of the project
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
        ProjectUpdate update = updatesRepository.getUpdateById(projectId, updateId);
        updatesRepository.deleteUpdate(updateId);
        if (projectsRepository.getProjectById(projectId).hasGroups())
            changelogsCreator.updateDeleted(update.getTargetVersion(), projectId, userId);
    }

}
