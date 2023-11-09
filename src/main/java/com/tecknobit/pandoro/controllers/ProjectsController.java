package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.ProjectsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.services.ProjectsHelper.PROJECTS_KEY;

@RestController
@RequestMapping(path = BASE_ENDPOINT + PROJECTS_KEY)
public class ProjectsController extends PandoroController {

    private final ProjectsHelper projectsHelper;

    @Autowired
    public ProjectsController(ProjectsHelper projectsHelper) {
        this.projectsHelper = projectsHelper;
    }

}
