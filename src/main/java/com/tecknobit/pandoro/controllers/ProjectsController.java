package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.services.ProjectsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.controllers.ProjectsController.PROJECTS_ENDPOINT;

@RestController
@RequestMapping(path = BASE_ENDPOINT + PROJECTS_ENDPOINT)
public class ProjectsController extends PandoroController {

    public static final String PROJECTS_ENDPOINT = "projects";

    private final ProjectsHelper projectsHelper;

    @Autowired
    public ProjectsController(ProjectsHelper projectsHelper) {
        this.projectsHelper = projectsHelper;
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    public Project getProject(@PathVariable(name = "projectId") long projectId) {
        return projectsHelper.getProject(projectId);
    }

    public List<Project> getProjects() {
        return projectsHelper.getProjects();
    }

}
