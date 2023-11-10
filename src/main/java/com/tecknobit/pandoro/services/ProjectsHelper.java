package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.services.repositories.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public static final String UPDATE_KEY = "project_update";

    public static final String UPDATE_CREATE_DATE_KEY = "create_date";

    public static final String UPDATE_TARGET_VERSION_KEY = "target_version";

    public static final String UPDATE_STATUS_KEY = "status";

    public static final String UPDATE_STARTED_BY_KEY = "started_by";

    public static final String UPDATE_START_DATE_KEY = "start_date";

    public static final String UPDATE_PUBLISHED_BY_KEY = "published_by";

    public static final String UPDATE_PUBLISH_DATE_KEY = "publish_date";

    @Autowired
    private ProjectsRepository projectsRepository;

    public List<Project> getProjectsList(String userId) {
        return projectsRepository.getProjectsList(userId);
    }

    public boolean projectExists(String userId, String projectName) {
        return projectsRepository.getProjectByName(userId, projectName) != null;
    }

    public void workWithProject(String userId, String projectId, String name, String description, String shortDescription,
                                String version, String repository, ArrayList<String> groups, boolean isAdding) {
        if (isAdding) {
            // TODO: 10/11/2023 INSERT THE GROUPS ALSO
            projectsRepository.insertProject(
                    userId,
                    projectId,
                    name,
                    description,
                    shortDescription,
                    version,
                    repository
            );
        } else {

        }
    }

}
