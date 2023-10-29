package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsHelper {

    public Project getProject(long projectId) {
        if (projectId == 1)
            return new Project("gaga1");
        return null;
    }

    public List<Project> getProjects() {
        return List.of(new Project("gaga"));
    }

}
