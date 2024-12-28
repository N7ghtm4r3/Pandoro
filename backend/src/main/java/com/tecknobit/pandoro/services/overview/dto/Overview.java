package com.tecknobit.pandoro.services.overview.dto;

import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.equinoxcore.annotations.DTO;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandorocore.enums.UpdateStatus;

/**
 * The {@code Overview} class is useful to transfer the data of an overview
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see DTO
 */
@DTO
public class Overview {

    /**
     * {@code totalProjects} the total number of the projects user currently has
     */
    private OverviewStatsItem totalProjects;

    /**
     * {@code totalUpdates} the total number of the updates user currently has
     */
    private OverviewStatsItem totalUpdates;

    /**
     * {@code updatesScheduled} stats of the current {@link UpdateStatus#SCHEDULED} updates user currently has
     */
    private OverviewFullStatsItem updatesScheduled;

    /**
     * {@code updatesInDevelopment} stats of the current {@link UpdateStatus#IN_DEVELOPMENT} updates user currently has
     */
    private OverviewFullStatsItem updatesInDevelopment;

    /**
     * {@code updatesPublished} stats of the current {@link UpdateStatus#PUBLISHED} updates user currently has
     */
    private OverviewFullStatsItem updatesPublished;

    /**
     * {@code developmentDays} stats of the total amount of development days user spent
     */
    private OverviewStatsItem developmentDays;

    /**
     * {@code averageDevelopmentDays} stats of the average amount of development days user spent
     */
    private OverviewStatsItem averageDevelopmentDays;

    /**
     * {@code bestPersonalPerformanceProject} the best personal user project
     */
    private ProjectPerformanceStats bestPersonalPerformanceProject;

    /**
     * {@code worstPersonalPerformanceProject} the worst personal user project
     */
    private ProjectPerformanceStats worstPersonalPerformanceProject;

    /**
     * {@code bestGroupPerformanceProject} the best group user project
     */
    private ProjectPerformanceStats bestGroupPerformanceProject;

    /**
     * {@code worstGroupPerformanceProject} the worst group user project
     */
    private ProjectPerformanceStats worstGroupPerformanceProject;

    /**
     * Method to get the {@link #totalProjects} instance
     *
     * @return the {@link #totalProjects} instance as {@link OverviewStatsItem}
     */
    public OverviewStatsItem getTotalProjects() {
        return totalProjects;
    }

    /**
     * Method to set the {@link #totalProjects} instance
     *
     * @param totalProjects The total number of the projects user currently has
     */
    public void setTotalProjects(OverviewStatsItem totalProjects) {
        this.totalProjects = totalProjects;
    }

    /**
     * Method to get the {@link #totalUpdates} instance
     *
     * @return the {@link #totalUpdates} instance as {@link OverviewStatsItem}
     */
    public OverviewStatsItem getTotalUpdates() {
        return totalUpdates;
    }

    /**
     * Method to set the {@link #totalUpdates} instance
     *
     * @param totalUpdates The total number of the updates user currently has
     */
    public void setTotalUpdates(OverviewStatsItem totalUpdates) {
        this.totalUpdates = totalUpdates;
    }

    /**
     * Wrapper method to set the correct stats based on the status
     *
     * @param status The status of the update
     * @param stats  The stats data to set
     */
    public void setUpdatesStats(UpdateStatus status, OverviewFullStatsItem stats) {
        switch (status) {
            case SCHEDULED -> setUpdatesScheduled(stats);
            case IN_DEVELOPMENT -> setUpdatesInDevelopment(stats);
            case PUBLISHED -> setUpdatesPublished(stats);
        }
    }

    /**
     * Method to get the {@link #updatesScheduled} instance
     *
     * @return the {@link #updatesScheduled} instance as {@link OverviewFullStatsItem}
     */
    public OverviewFullStatsItem getUpdatesScheduled() {
        return updatesScheduled;
    }

    /**
     * Method to set the {@link #updatesScheduled} instance
     *
     * @param updatesScheduled The tats of the current {@link UpdateStatus#SCHEDULED} updates user currently has
     */
    private void setUpdatesScheduled(OverviewFullStatsItem updatesScheduled) {
        this.updatesScheduled = updatesScheduled;
    }

    /**
     * Method to get the {@link #updatesInDevelopment} instance
     *
     * @return the {@link #updatesInDevelopment} instance as {@link OverviewFullStatsItem}
     */
    public OverviewFullStatsItem getUpdatesInDevelopment() {
        return updatesInDevelopment;
    }

    /**
     * Method to set the {@link #updatesInDevelopment} instance
     *
     * @param updatesInDevelopment The tats of the current {@link UpdateStatus#IN_DEVELOPMENT} updates user currently has
     */
    private void setUpdatesInDevelopment(OverviewFullStatsItem updatesInDevelopment) {
        this.updatesInDevelopment = updatesInDevelopment;
    }

    /**
     * Method to get the {@link #updatesPublished} instance
     *
     * @return the {@link #updatesPublished} instance as {@link OverviewFullStatsItem}
     */
    public OverviewFullStatsItem getUpdatesPublished() {
        return updatesPublished;
    }

    /**
     * Method to set the {@link #updatesPublished} instance
     *
     * @param updatesPublished The stats of the current {@link UpdateStatus#PUBLISHED} updates user currently has
     */
    private void setUpdatesPublished(OverviewFullStatsItem updatesPublished) {
        this.updatesPublished = updatesPublished;
    }

    /**
     * Method to get the {@link #developmentDays} instance
     *
     * @return the {@link #developmentDays} instance as {@link OverviewStatsItem}
     */
    public OverviewStatsItem getDevelopmentDays() {
        return developmentDays;
    }

    /**
     * Method to set the {@link #developmentDays} instance
     *
     * @param developmentDays The stats of the total amount of development days user spent
     */
    public void setDevelopmentDays(OverviewStatsItem developmentDays) {
        this.developmentDays = developmentDays;
    }

    /**
     * Method to get the {@link #averageDevelopmentDays} instance
     *
     * @return the {@link #averageDevelopmentDays} instance as {@link OverviewStatsItem}
     */
    public OverviewStatsItem getAverageDevelopmentDays() {
        return averageDevelopmentDays;
    }

    /**
     * Method to set the {@link #averageDevelopmentDays} instance
     *
     * @param averageDevelopmentDays The stats of the average amount of development days user spent
     */
    public void setAverageDevelopmentDays(OverviewStatsItem averageDevelopmentDays) {
        this.averageDevelopmentDays = averageDevelopmentDays;
    }

    /**
     * Method to get the {@link #bestPersonalPerformanceProject} instance
     *
     * @return the {@link #bestPersonalPerformanceProject} instance as {@link ProjectPerformanceStats}
     */
    public ProjectPerformanceStats getBestPersonalPerformanceProject() {
        return bestPersonalPerformanceProject;
    }

    /**
     * Method to set the {@link #bestPersonalPerformanceProject} instance
     *
     * @param bestPersonalPerformanceProject The best personal user project
     */
    public void setBestPersonalPerformanceProject(ProjectPerformanceStats bestPersonalPerformanceProject) {
        this.bestPersonalPerformanceProject = bestPersonalPerformanceProject;
    }

    /**
     * Method to get the {@link #worstPersonalPerformanceProject} instance
     *
     * @return the {@link #worstPersonalPerformanceProject} instance as {@link ProjectPerformanceStats}
     */
    public ProjectPerformanceStats getWorstPersonalPerformanceProject() {
        return worstPersonalPerformanceProject;
    }

    /**
     * Method to set the {@link #worstPersonalPerformanceProject} instance
     *
     * @param worstPersonalPerformanceProject The worst personal user project
     */
    public void setWorstPersonalPerformanceProject(ProjectPerformanceStats worstPersonalPerformanceProject) {
        this.worstPersonalPerformanceProject = worstPersonalPerformanceProject;
    }

    /**
     * Method to get the {@link #bestGroupPerformanceProject} instance
     *
     * @return the {@link #bestGroupPerformanceProject} instance as {@link ProjectPerformanceStats}
     */
    public ProjectPerformanceStats getBestGroupPerformanceProject() {
        return bestGroupPerformanceProject;
    }

    /**
     * Method to set the {@link #bestGroupPerformanceProject} instance
     *
     * @param bestGroupPerformanceProject The best group user project
     */
    public void setBestGroupPerformanceProject(ProjectPerformanceStats bestGroupPerformanceProject) {
        this.bestGroupPerformanceProject = bestGroupPerformanceProject;
    }

    /**
     * Method to get the {@link #worstGroupPerformanceProject} instance
     *
     * @return the {@link #worstGroupPerformanceProject} instance as {@link ProjectPerformanceStats}
     */
    public ProjectPerformanceStats getWorstGroupPerformanceProject() {
        return worstGroupPerformanceProject;
    }

    /**
     * Method to set the {@link #worstGroupPerformanceProject} instance
     *
     * @param worstGroupPerformanceProject The worst group user project
     */
    public void setWorstGroupPerformanceProject(ProjectPerformanceStats worstGroupPerformanceProject) {
        this.worstGroupPerformanceProject = worstGroupPerformanceProject;
    }

    /**
     * The {@code OverviewStatsItem} record class contains the statistics about the projects data
     *
     * @param total The number of the projects
     * @param personal The number of the personal
     * @param personalPercentage The percentage related to the {@link #personal} value
     * @param group The number of the group projects
     * @param groupPercentage The percentage related to the {@link #group} value
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    public record OverviewStatsItem(
            int total,
            int personal,
            double personalPercentage,
            int group,
            double groupPercentage
    ) {

    }

    /**
     * The {@code OverviewFullStatsItem} record class contains the stats about the update performance
     *
     * @param status The status of the update
     * @param total The number of the updates currently in this {@link #status}
     * @param personal The number of the personal updates currently in this {@link #status}
     * @param personalPercentage The percentage related to the {@link #personal} value
     * @param group The number of the group updates currently in this {@link #status}
     * @param groupPercentage The percentage related to the {@link #group} value
     * @param byMe The number of the group updates currently in this {@link #status} scheduled/started/published by me
     * @param byMePercentage The percentage related to the {@link #byMe} value
     *
     * @author N7ghtm4r3 - Tecknobit
     */
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

    /**
     * The {@code ProjectPerformanceStats} record class contains the stats about the project performance
     *
     * @param id The identifier of the project
     * @param name The name of the project
     * @param updates The number of the updates
     * @param totalDevelopmentDays The total amount of the days spent to publish the updates
     * @param averageDaysPerUpdate The average days spent to publish an update
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    public record ProjectPerformanceStats(
            String id,
            String name,
            int updates,
            int totalDevelopmentDays,
            double averageDaysPerUpdate
    ) {

        /**
         * Method to convert the project data to the related stats
         *
         * @param project The project to convert
         * @return the stats of the project as {@link ProjectPerformanceStats}
         */
        @Returner
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
