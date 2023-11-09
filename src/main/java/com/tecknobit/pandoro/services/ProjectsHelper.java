package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.services.repositories.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectsHelper {

    public static final String PROJECTS_KEY = "projects";

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

}
