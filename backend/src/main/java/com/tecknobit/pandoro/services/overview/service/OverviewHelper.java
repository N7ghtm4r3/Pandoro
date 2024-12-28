package com.tecknobit.pandoro.services.overview.service;

import com.tecknobit.pandoro.services.overview.dto.Overview;
import com.tecknobit.pandoro.services.overview.dto.Overview.ProjectPerformanceStats;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.projects.entities.ProjectUpdate;
import com.tecknobit.pandoro.services.projects.repositories.ProjectsRepository;
import com.tecknobit.pandorocore.enums.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

// TODO: 28/12/2024 TO DOCU 
@Service
public class OverviewHelper {

    private static final double HUNDRED_PERCENT_VALUE = 100.0;

    @Autowired
    private ProjectsRepository projectsRepository;

    public Overview getOverview(String userId) {
        Pageable pageable = PageRequest.ofSize(Integer.MAX_VALUE);
        List<Project> projects = projectsRepository.getProjects(userId, "", Collections.emptySet(), pageable);
        if (projects.isEmpty())
            return null;
        Overview overview = new Overview();
        overview.setTotalProjects(getTotalProjectsStats(projects));
        overview.setTotalUpdates(getTotalUpdatesStats(projects));
        for (UpdateStatus status : UpdateStatus.getEntries())
            overview.setUpdatesStats(status, getUpdatesStats(userId, projects, status));
        overview.setDevelopmentDays(getDevelopmentDays(projects));
        overview.setAverageDevelopmentDays(getAverageDevelopmentDays(projects));
        ProjectsAnalyzer projectsAnalyzer = new ProjectsAnalyzer(projects);
        overview.setBestPersonalPerformanceProject(projectsAnalyzer.getBestPersonalProjectStats());
        overview.setWorstPersonalPerformanceProject(projectsAnalyzer.getWorstPersonalProjectStats());
        overview.setBestGroupPerformanceProject(projectsAnalyzer.getBestGroupProjectStats());
        overview.setWorstGroupPerformanceProject(projectsAnalyzer.getWorstGroupProjectStats());
        return overview;
    }

    private Overview.OverviewStatsItem getTotalProjectsStats(List<Project> projects) {
        int total = projects.size();
        int personal = 0;
        int group = 0;
        for (Project project : projects) {
            if (project.hasGroups())
                group++;
            else
                personal++;
        }
        double personalPercentage = ((personal * HUNDRED_PERCENT_VALUE) / total);
        double groupPercentage = 0;
        if (personalPercentage > 0)
            groupPercentage = HUNDRED_PERCENT_VALUE - personalPercentage;
        return new Overview.OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    private Overview.OverviewStatsItem getTotalUpdatesStats(List<Project> projects) {
        int total = 0;
        int personal = 0;
        int group = 0;
        for (Project project : projects) {
            List<ProjectUpdate> updates = project.getUpdates();
            if (!updates.isEmpty()) {
                total += updates.size();
                if (project.hasGroups())
                    group++;
                else
                    personal++;
            }
        }
        boolean hasEnoughStats = total > 0;
        double personalPercentage = 0;
        double groupPercentage = 0;
        if (hasEnoughStats) {
            personalPercentage = ((personal * HUNDRED_PERCENT_VALUE) / total);
            groupPercentage = HUNDRED_PERCENT_VALUE - personalPercentage;
        }
        return new Overview.OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    private Overview.OverviewFullStatsItem getUpdatesStats(String userId, List<Project> projects, UpdateStatus status) {
        int total = 0;
        int personal = 0;
        int group = 0;
        int byMe = 0;
        for (Project project : projects) {
            for (ProjectUpdate update : project.getUpdates()) {
                UpdateStatus updateStatus = update.getStatus();
                if (updateStatus == status) {
                    if (project.hasGroups())
                        group++;
                    else
                        personal++;
                    total++;
                    switch (status) {
                        case SCHEDULED -> {
                            if (update.getAuthor().getId().equals(userId))
                                byMe++;
                        }
                        case IN_DEVELOPMENT -> {
                            if (update.getStartedBy().getId().equals(userId))
                                byMe++;
                        }
                        case PUBLISHED -> {
                            if (update.getPublishedBy().getId().equals(userId))
                                byMe++;
                        }
                    }
                }
            }
        }
        boolean hasEnoughStats = total > 0;
        double personalPercentage = 0;
        double groupPercentage = 0;
        double byMePercentage = 0;
        if (hasEnoughStats) {
            personalPercentage = ((personal * HUNDRED_PERCENT_VALUE) / total);
            groupPercentage = HUNDRED_PERCENT_VALUE - personalPercentage;
            byMePercentage = ((byMe * HUNDRED_PERCENT_VALUE) / total);
        }
        return new Overview.OverviewFullStatsItem(
                status,
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage,
                byMe,
                byMePercentage
        );
    }

    private Overview.OverviewStatsItem getDevelopmentDays(List<Project> projects) {
        int total = 0;
        int personal = 0;
        int group = 0;
        for (Project project : projects) {
            int totalDevelopmentDays = project.getTotalDevelopmentDays();
            if (project.hasGroups())
                group += totalDevelopmentDays;
            else
                personal += totalDevelopmentDays;
            total += totalDevelopmentDays;
        }
        boolean hasEnoughStats = total > 0;
        double personalPercentage = 0;
        double groupPercentage = 0;
        if (hasEnoughStats) {
            personalPercentage = ((personal * HUNDRED_PERCENT_VALUE) / total);
            groupPercentage = HUNDRED_PERCENT_VALUE - personalPercentage;
        }
        return new Overview.OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    private Overview.OverviewStatsItem getAverageDevelopmentDays(List<Project> projects) {
        int total = 0;
        int personal = 0;
        int group = 0;
        for (Project project : projects) {
            int averageDevelopmentTime = project.getAverageDevelopmentTime();
            if (project.hasGroups())
                group += averageDevelopmentTime;
            else
                personal += averageDevelopmentTime;
            total += averageDevelopmentTime;
        }
        boolean hasEnoughStats = total > 0;
        double personalPercentage = 0;
        double groupPercentage = 0;
        if (hasEnoughStats) {
            personalPercentage = ((personal * HUNDRED_PERCENT_VALUE) / total);
            groupPercentage = HUNDRED_PERCENT_VALUE - personalPercentage;
        }
        return new Overview.OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    private static final class ProjectsAnalyzer {

        private final List<Project> projects;

        private Project bestPersonalProject;

        private Project bestGroupProject;

        public ProjectsAnalyzer(List<Project> projects) {
            this.projects = projects;
        }

        public ProjectPerformanceStats getBestPersonalProjectStats() {
            bestPersonalProject = getBestProject(false);
            return ProjectPerformanceStats.toStats(bestPersonalProject);
        }

        public ProjectPerformanceStats getWorstPersonalProjectStats() {
            Project worstPersonalProject = getWorstProject(false, bestPersonalProject);
            return ProjectPerformanceStats.toStats(worstPersonalProject);
        }

        public ProjectPerformanceStats getBestGroupProjectStats() {
            bestGroupProject = getBestProject(true);
            return ProjectPerformanceStats.toStats(bestGroupProject);
        }

        public ProjectPerformanceStats getWorstGroupProjectStats() {
            Project worstGroupProject = getWorstProject(true, bestGroupProject);
            return ProjectPerformanceStats.toStats(worstGroupProject);
        }

        private Project getBestProject(boolean isGroup) {
            Project bestProject = null;
            int maxUpdates = 0;
            int minDevelopmentDays = Integer.MAX_VALUE;
            int minAvgDevelopmentTime = Integer.MAX_VALUE;
            for (Project project : projects) {
                if (project.hasGroups() == isGroup) {
                    int updates = project.getUpdates().size();
                    int developmentDays = project.getTotalDevelopmentDays();
                    int avgDevTime = project.getAverageDevelopmentTime();
                    if (updates > maxUpdates || (updates == maxUpdates && developmentDays < minDevelopmentDays) ||
                            (updates == maxUpdates && developmentDays == minDevelopmentDays
                                    && avgDevTime < minAvgDevelopmentTime)) {
                        bestProject = project;
                        maxUpdates = updates;
                        minDevelopmentDays = developmentDays;
                        minAvgDevelopmentTime = avgDevTime;
                    }
                }
            }
            return bestProject;
        }

        private Project getWorstProject(boolean isGroup, Project bestProject) {
            Project worstProject = null;
            int minUpdates = Integer.MAX_VALUE;
            int maxDevelopmentDays = 0;
            int maxAvgDevelopmentTime = 0;
            for (Project project : projects) {
                if (project.hasGroups() == isGroup && (bestProject == null || !project.getId().equals(bestProject.getId()))) {
                    int updates = project.getUpdates().size();
                    int developmentDays = project.getTotalDevelopmentDays();
                    int avgDevTime = project.getAverageDevelopmentTime();
                    if (updates < minUpdates || (updates == minUpdates && developmentDays > maxDevelopmentDays) ||
                            (updates == minUpdates && developmentDays == maxDevelopmentDays &&
                                    avgDevTime > maxAvgDevelopmentTime)) {
                        worstProject = project;
                        minUpdates = updates;
                        maxDevelopmentDays = developmentDays;
                        maxAvgDevelopmentTime = avgDevTime;
                    }
                }
            }
            return worstProject;
        }

    }

}
