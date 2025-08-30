package com.tecknobit.pandoro.services.overview.service;

import com.tecknobit.pandoro.services.overview.dto.Overview;
import com.tecknobit.pandoro.services.overview.dto.Overview.OverviewFullStatsItem;
import com.tecknobit.pandoro.services.overview.dto.Overview.OverviewStatsItem;
import com.tecknobit.pandoro.services.overview.dto.Overview.ProjectPerformanceStats;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.projects.entities.Update;
import com.tecknobit.pandoro.services.projects.repositories.ProjectsRepository;
import com.tecknobit.pandorocore.enums.UpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * The {@code OverviewHelper} class is useful to manage all the overview database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Service
public class OverviewService {

    /**
     * {@code HUNDRED_PERCENT_VALUE} constant for 100.0 value
     */
    private static final double HUNDRED_PERCENT_VALUE = 100.0;

    /**
     * {@code projectsRepository} instance for the projects repository
     */
    private final ProjectsRepository projectsRepository;

    /**
     * Constructor used to init the service
     *
     * @param projectsRepository The instance for the projects repository
     */
    @Autowired
    public OverviewService(ProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    /**
     * Method to get the overview analysis for the requested user
     *
     * @param userId The user who requests the overview
     * @return overview analysis as {@link Overview} DTO
     */
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

    /**
     * Method to calculate the stats about the projects of the user
     *
     * @param projects The list of the user's projects
     *
     * @return the stats as {@link OverviewStatsItem}
     */
    private OverviewStatsItem getTotalProjectsStats(List<Project> projects) {
        int total = projects.size();
        int personal = 0;
        int group = 0;
        for (Project project : projects) {
            if (project.hasGroups())
                group++;
            else
                personal++;
        }
        boolean hasEnoughStats = total > 0;
        double personalPercentage = 0;
        double groupPercentage = 0;
        if (hasEnoughStats) {
            personalPercentage = ((personal * HUNDRED_PERCENT_VALUE) / total);
            groupPercentage = HUNDRED_PERCENT_VALUE - personalPercentage;
        }
        return new OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    /**
     * Method to calculate the stats about the number of the updates in the projects of the user
     *
     * @param projects The list of the user's projects
     *
     * @return the stats as {@link OverviewStatsItem}
     */
    private OverviewStatsItem getTotalUpdatesStats(List<Project> projects) {
        int total = 0;
        int personal = 0;
        int group = 0;
        for (Project project : projects) {
            List<Update> updates = project.getUpdates();
            int updatesNumber = updates.size();
            if (!updates.isEmpty()) {
                total += updatesNumber;
                if (project.hasGroups())
                    group += updatesNumber;
                else
                    personal += updatesNumber;
            }
        }
        boolean hasEnoughStats = total > 0;
        double personalPercentage = 0;
        double groupPercentage = 0;
        if (hasEnoughStats) {
            personalPercentage = ((personal * HUNDRED_PERCENT_VALUE) / total);
            groupPercentage = HUNDRED_PERCENT_VALUE - personalPercentage;
        }
        return new OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    /**
     * Method to calculate the stats about the updates in the projects of the user
     *
     * @param userId The user who requests the overview
     * @param projects The list of the user's projects
     * @param status The status of the update to calculate its stats
     *
     * @return the stats as {@link OverviewFullStatsItem}
     */
    private OverviewFullStatsItem getUpdatesStats(String userId, List<Project> projects, UpdateStatus status) {
        int total = 0;
        int personal = 0;
        int group = 0;
        int byMe = 0;
        for (Project project : projects) {
            for (Update update : project.getUpdates()) {
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
        return new OverviewFullStatsItem(
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

    /**
     * Method to calculate the stats about the total number of the development days
     *
     * @param projects The list of the user's projects
     *
     * @return the stats as {@link OverviewStatsItem}
     */
    private OverviewStatsItem getDevelopmentDays(List<Project> projects) {
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
        return new OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    /**
     * Method to calculate the stats about the average number of the development days
     *
     * @param projects The list of the user's projects
     *
     * @return the stats as {@link OverviewStatsItem}
     */
    private OverviewStatsItem getAverageDevelopmentDays(List<Project> projects) {
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
        return new OverviewStatsItem(
                total,
                personal,
                personalPercentage,
                group,
                groupPercentage
        );
    }

    /**
     * The {@code ProjectsAnalyzer} class is useful to analyze the projects data and discover the best and to-improve
     * project both for personal and group side
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    private static final class ProjectsAnalyzer {

        /**
         * {@code projects} the list of the user's projects
         */
        private final List<Project> projects;

        /**
         * {@code bestPersonalProject} the best personal project
         */
        private Project bestPersonalProject;

        /**
         * {@code bestGroupProject} the best group project
         */
        private Project bestGroupProject;

        /**
         * Constructor to instantiate the analyzer
         *
         * @param projects The list of the user's projects
         */
        public ProjectsAnalyzer(List<Project> projects) {
            this.projects = projects;
        }

        /**
         * Method to get the best performance project for the personal side
         *
         * @return get the best performance project as {@link ProjectPerformanceStats}
         */
        public ProjectPerformanceStats getBestPersonalProjectStats() {
            bestPersonalProject = getBestProject(false);
            return ProjectPerformanceStats.toStats(bestPersonalProject);
        }

        /**
         * Method to get the worst performance project for the personal side
         *
         * @return get the worst performance project as {@link ProjectPerformanceStats}
         */
        public ProjectPerformanceStats getWorstPersonalProjectStats() {
            Project worstPersonalProject = getWorstProject(false, bestPersonalProject);
            return ProjectPerformanceStats.toStats(worstPersonalProject);
        }

        /**
         * Method to get the best performance project for the group side
         *
         * @return get the best performance project as {@link ProjectPerformanceStats}
         */
        public ProjectPerformanceStats getBestGroupProjectStats() {
            bestGroupProject = getBestProject(true);
            return ProjectPerformanceStats.toStats(bestGroupProject);
        }

        /**
         * Method to get the worst performance project for the group side
         *
         * @return get the worst performance project as {@link ProjectPerformanceStats}
         */
        public ProjectPerformanceStats getWorstGroupProjectStats() {
            Project worstGroupProject = getWorstProject(true, bestGroupProject);
            return ProjectPerformanceStats.toStats(worstGroupProject);
        }

        /**
         * Method to get the best performance project
         *
         * @param isGroup Whether search for the group or personal side
         * @return get the best performance project as {@link Project}
         */
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

        /**
         * Method to get the worst performance project
         *
         * @param isGroup Whether search for the group or personal side
         * @param bestProject The best performance project related to the side where is searching
         *
         * @return get the worst performance project as {@link Project}
         */
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
