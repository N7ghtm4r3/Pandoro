package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.services.ProjectsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/projects" // TODO: 29/10/2023 INSERT THE CORRECT PATH
)
public class ProjectsController {

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
