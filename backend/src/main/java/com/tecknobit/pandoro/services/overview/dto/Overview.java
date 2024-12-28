package com.tecknobit.pandoro.services.overview.dto;

import com.tecknobit.equinoxcore.annotations.DTO;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandorocore.enums.UpdateStatus;

// TODO: 28/12/2024 TO DOCU
@DTO
public class Overview {

    private OverviewStatsItem totalProjects;

    private OverviewStatsItem totalUpdates;

    private OverviewFullStatsItem updatesScheduled;

    private OverviewFullStatsItem updatesInDevelopment;

    private OverviewFullStatsItem updatesPublished;

    private OverviewStatsItem developmentDays;

    private OverviewStatsItem averageDevelopmentDays;

    private ProjectPerformanceStats bestPersonalPerformanceProject;

    private ProjectPerformanceStats worstPersonalPerformanceProject;

    private ProjectPerformanceStats bestGroupPerformanceProject;

    private ProjectPerformanceStats worstGroupPerformanceProject;

    public OverviewStatsItem getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(OverviewStatsItem totalProjects) {
        this.totalProjects = totalProjects;
    }

    public OverviewStatsItem getTotalUpdates() {
        return totalUpdates;
    }

    public void setTotalUpdates(OverviewStatsItem totalUpdates) {
        this.totalUpdates = totalUpdates;
    }

    public void setUpdatesStats(UpdateStatus status, OverviewFullStatsItem stats) {
        switch (status) {
            case SCHEDULED -> setUpdatesScheduled(stats);
            case IN_DEVELOPMENT -> setUpdatesInDevelopment(stats);
            case PUBLISHED -> setUpdatesPublished(stats);
        }
    }

    public OverviewFullStatsItem getUpdatesScheduled() {
        return updatesScheduled;
    }

    private void setUpdatesScheduled(OverviewFullStatsItem updatesScheduled) {
        this.updatesScheduled = updatesScheduled;
    }

    public OverviewFullStatsItem getUpdatesInDevelopment() {
        return updatesInDevelopment;
    }

    private void setUpdatesInDevelopment(OverviewFullStatsItem updatesInDevelopment) {
        this.updatesInDevelopment = updatesInDevelopment;
    }

    public OverviewFullStatsItem getUpdatesPublished() {
        return updatesPublished;
    }

    private void setUpdatesPublished(OverviewFullStatsItem updatesPublished) {
        this.updatesPublished = updatesPublished;
    }

    public OverviewStatsItem getDevelopmentDays() {
        return developmentDays;
    }

    public void setDevelopmentDays(OverviewStatsItem developmentDays) {
        this.developmentDays = developmentDays;
    }

    public OverviewStatsItem getAverageDevelopmentDays() {
        return averageDevelopmentDays;
    }

    public void setAverageDevelopmentDays(OverviewStatsItem averageDevelopmentDays) {
        this.averageDevelopmentDays = averageDevelopmentDays;
    }

    public ProjectPerformanceStats getBestPersonalPerformanceProject() {
        return bestPersonalPerformanceProject;
    }

    public void setBestPersonalPerformanceProject(ProjectPerformanceStats bestPersonalPerformanceProject) {
        this.bestPersonalPerformanceProject = bestPersonalPerformanceProject;
    }

    public ProjectPerformanceStats getWorstPersonalPerformanceProject() {
        return worstPersonalPerformanceProject;
    }

    public void setWorstPersonalPerformanceProject(ProjectPerformanceStats worstPersonalPerformanceProject) {
        this.worstPersonalPerformanceProject = worstPersonalPerformanceProject;
    }

    public ProjectPerformanceStats getBestGroupPerformanceProject() {
        return bestGroupPerformanceProject;
    }

    public void setBestGroupPerformanceProject(ProjectPerformanceStats bestGroupPerformanceProject) {
        this.bestGroupPerformanceProject = bestGroupPerformanceProject;
    }

    public ProjectPerformanceStats getWorstGroupPerformanceProject() {
        return worstGroupPerformanceProject;
    }

    public void setWorstGroupPerformanceProject(ProjectPerformanceStats worstGroupPerformanceProject) {
        this.worstGroupPerformanceProject = worstGroupPerformanceProject;
    }

    public record OverviewStatsItem(
            int total,
            int personal,
            double personalPercentage,
            int group,
            double groupPercentage
    ) {

    }

    public record OverviewFullStatsItem(
            UpdateStatus status,
            int total,
            int personal,
            double personalPercentage,
            int group,
            double groupPercentage,
            int byMe,
            double byMePercentage
    ) {

    }

    public record ProjectPerformanceStats(
            String id,
            String name,
            int updates,
            int totalDevelopmentDays,
            double averageDaysPerUpdate
    ) {

        public static ProjectPerformanceStats toStats(Project project) {
            if (project == null)
                return null;
            return new ProjectPerformanceStats(
                    project.getId(),
                    project.getName(),
                    project.getUpdates().size(),
                    project.getTotalDevelopmentDays(),
                    project.getAverageDevelopmentTime()
            );
        }

    }

}
