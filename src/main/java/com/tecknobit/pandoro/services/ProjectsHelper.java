package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.records.ProjectUpdate;
import com.tecknobit.pandoro.services.repositories.NotesRepository;
import com.tecknobit.pandoro.services.repositories.projects.ProjectsRepository;
import com.tecknobit.pandoro.services.repositories.projects.UpdatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.controllers.PandoroController.generateIdentifier;
import static com.tecknobit.pandoro.records.ProjectUpdate.Status.SCHEDULED;

@Service
public class ProjectsHelper {

    public static final String PROJECTS_KEY = "projects";

    public static final String PROJECT_IDENTIFIER_KEY = "project_id";

    public static final String PROJECTS_GROUPS_TABLE = "projects_groups";

    public static final String PROJECT_KEY = "project";

    public static final String PROJECT_SHORT_DESCRIPTION_KEY = "project_short_description";

    public static final String PROJECT_DESCRIPTION_KEY = "project_description";

    public static final String PROJECT_VERSION_KEY = "project_version";

    public static final String PROJECT_REPOSITORY_KEY = "project_repository";

    public static final String UPDATES_KEY = "updates";

    public static final String UPDATE_ID = "update_id";

    public static final String UPDATE_KEY = "project_update";

    public static final String UPDATE_CREATE_DATE_KEY = "create_date";

    public static final String UPDATE_TARGET_VERSION_KEY = "target_version";

    public static final String UPDATE_CHANGE_NOTES_KEY = "update_change_notes";

    public static final String UPDATE_STATUS_KEY = "status";

    public static final String UPDATE_STARTED_BY_KEY = "started_by";

    public static final String UPDATE_START_DATE_KEY = "start_date";

    public static final String UPDATE_PUBLISHED_BY_KEY = "published_by";

    public static final String UPDATE_PUBLISH_DATE_KEY = "publish_date";

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private UpdatesRepository updatesRepository;

    @Autowired
    private NotesRepository notesRepository;

    public List<Project> getProjectsList(String userId) {
        return projectsRepository.getProjectsList(userId);
    }

    public Project getProjectByName(String userId, String projectName) {
        return projectsRepository.getProjectByName(userId, projectName);
    }

    public Project getProjectById(String userId, String projectId) {
        return projectsRepository.getProjectById(userId, projectId);
    }

    public Project getProject(String userId, String projectId) {
        return projectsRepository.getProject(userId, projectId);
    }

    public void workWithProject(String userId, String projectId, String name, String description, String shortDescription,
                                String version, String repository, ArrayList<String> groups, boolean isAdding) {
        if (isAdding) {
            projectsRepository.insertProject(
                    userId,
                    projectId,
                    name,
                    description,
                    shortDescription,
                    version,
                    repository
            );
            for (String group : groups) {
                // TODO: 10/11/2023 CREATE THE CHANGELOG
                projectsRepository.addProjectGroup(projectId, group);
            }
        } else {
            projectsRepository.editProject(
                    userId,
                    projectId,
                    name,
                    description,
                    shortDescription,
                    version,
                    repository
            );
            List<String> currentGroups = projectsRepository.getProjectGroupsIds(projectId);
            currentGroups.removeAll(groups);
            for (String group : currentGroups) {
                // TODO: 10/11/2023 CREATE THE CHANGELOG
                projectsRepository.deleteProjectGroup(projectId, group);
            }
            groups.removeAll(projectsRepository.getProjectGroupsIds(projectId));
            for (String group : groups) {
                // TODO: 10/11/2023 CREATE THE CHANGELOG
                projectsRepository.addProjectGroup(projectId, group);
            }
        }
    }

    public void deleteProject(String userId, String projectId) {
        projectsRepository.deleteProject(userId, projectId);
    }

    public boolean targetVersionExists(String projectId, String targetVersion) {
        return updatesRepository.getUpdateByVersion(projectId, targetVersion) != null;
    }

    public ProjectUpdate updateExists(String projectId, String updateId) {
        return updatesRepository.getUpdateById(projectId, updateId);
    }

    public void scheduleUpdate(String updateId, String targetVersion, ArrayList<String> changeNotes,
                               String projectId, String userId) {
        // TODO: 11/11/2023 CREATE THE CHANGELOG IF IS A GROUP PROJECT
        updatesRepository.scheduleUpdate(updateId, targetVersion, System.currentTimeMillis(), SCHEDULED,
                projectId, userId);
        for (String note : changeNotes)
            notesRepository.addChangeNote(userId, generateIdentifier(), note, System.currentTimeMillis(), updateId);
    }

    public void startUpdate(String updateId, String userId) {
        // TODO: 11/11/2023 CREATE THE CHANGELOG IF IS A GROUP PROJECT
        updatesRepository.startUpdate(updateId, System.currentTimeMillis(), userId);
    }

    public void publishUpdate(String updateId, String userId) {
        // TODO: 11/11/2023 CREATE THE CHANGELOG IF IS A GROUP PROJECT
        updatesRepository.publishUpdate(updateId, System.currentTimeMillis(), userId);
    }

    public void addChangeNote(String userId, String noteId, String contentNote, String updateId) {
        notesRepository.addChangeNote(userId, noteId, contentNote, System.currentTimeMillis(), updateId);
    }

    public boolean changeNoteExists(String updateId, String noteId) {
        return notesRepository.getNoteByUpdate(updateId, noteId) != null;
    }

    public void markChangeNoteAsDone(String updateId, String noteId, String userId) {
        notesRepository.manageNoteStatus(updateId, noteId, true, userId, System.currentTimeMillis());
    }

    public void markChangeNoteAsToDo(String updateId, String noteId) {
        notesRepository.manageNoteStatus(updateId, noteId, false, null, -1);
    }

    public void deleteChangeNote(String updateId, String noteId) {
        notesRepository.deleteChangeNote(updateId, noteId);
    }

    public void deleteUpdate(String updateId) {
        // TODO: 11/11/2023 CREATE THE CHANGELOG IF IS A GROUP PROJECT
        updatesRepository.deleteUpdate(updateId);
    }

}
