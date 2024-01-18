package com.tecknobit.pandoro.helpers.ui

import com.tecknobit.pandoro.records.Project

/**
 * The **OverviewUIHelper** class is useful to serve the UI with the performance of personal and group projects
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class OverviewUIHelper(var userProjects: List<Project>) {

    /**
     * **bestPersonalVProject** -> the best personal project
     */
    var bestPersonalVProject: Project? = null

    /**
     * **bestGroupVProject** -> the best group project
     */
    var bestGroupVProject: Project? = null

    /**
     * Function to get the personal best project in terms of performance
     *
     * No-any params required
     *
     * @return the best personal project in terms of performance as [Project]
     */
    fun getBestPersonalProject(): Project? {
        var bestProject: Project? = null
        var updatesNumber = 0
        var developmentDays = 0
        var averageDevelopmentTime = 0
        userProjects.forEach { project ->
            if (!project.hasGroups()) {
                val pAverageDevelopmentTime = project.averageDevelopmentTime
                if (pAverageDevelopmentTime > averageDevelopmentTime) {
                    averageDevelopmentTime = pAverageDevelopmentTime
                    developmentDays = project.totalDevelopmentDays
                }
            }
        }
        userProjects.forEach { project ->
            val pUpdatesNumber = project.updatesNumber
            if (!project.hasGroups() && pUpdatesNumber > 0) {
                val pDevelopmentDays = project.totalDevelopmentDays
                if (pDevelopmentDays > 0) {
                    val pAverageDevelopmentTime = project.averageDevelopmentTime
                    if (pUpdatesNumber >= updatesNumber && pDevelopmentDays <= developmentDays) {
                        if (pAverageDevelopmentTime < averageDevelopmentTime || bestProject == null) {
                            bestProject = project
                            updatesNumber = pUpdatesNumber
                            developmentDays = pDevelopmentDays
                            averageDevelopmentTime = pAverageDevelopmentTime
                        }
                    }
                }
            }
        }
        return bestProject
    }

    /**
     * Function to get the worst personal project in terms of performance
     *
     * No-any params required
     *
     * @return the worst personal project in terms of performance as [Project]
     */
    fun getWorstPersonalProject(): Project? {
        var worstProject: Project? = null
        var updatesNumber = 0
        var developmentDays = 0
        var averageDevelopmentTime = 0
        if (bestPersonalVProject != null) {
            userProjects.forEach { project ->
                if (!project.hasGroups() && !project.id.equals(bestPersonalVProject!!.id)) {
                    val pUpdatesNumber = project.updatesNumber
                    if (pUpdatesNumber > updatesNumber) {
                        updatesNumber = pUpdatesNumber
                        developmentDays = project.totalDevelopmentDays
                    }
                }
            }
            userProjects.forEach { project ->
                val pUpdatesNumber = project.updatesNumber
                if (!project.hasGroups() && pUpdatesNumber > 0 && !project.id.equals(
                        bestPersonalVProject!!.id
                    )
                ) {
                    val pDevelopmentDays = project.totalDevelopmentDays
                    val pAverageDevelopmentTime = project.averageDevelopmentTime
                    if (pUpdatesNumber <= updatesNumber && pDevelopmentDays >= developmentDays) {
                        if (pAverageDevelopmentTime > averageDevelopmentTime || worstProject == null) {
                            worstProject = project
                            updatesNumber = pUpdatesNumber
                            developmentDays = pDevelopmentDays
                            averageDevelopmentTime = pAverageDevelopmentTime
                        }
                    }
                }
            }
        }
        return worstProject
    }

    /**
     * Function to get the best group project in terms of performance
     *
     * No-any params required
     *
     * @return the best group project in terms of performance as [Project]
     */
    fun getBestGroupProject(): Project? {
        var bestProject: Project? = null
        var updatesNumber = 0
        var developmentDays = 0
        var averageDevelopmentTime = 0
        userProjects.forEach { project ->
            if (project.hasGroups()) {
                val pAverageDevelopmentTime = project.averageDevelopmentTime
                if (pAverageDevelopmentTime > averageDevelopmentTime) {
                    averageDevelopmentTime = pAverageDevelopmentTime
                    developmentDays = project.totalDevelopmentDays
                }
            }
        }
        userProjects.forEach { project ->
            val pUpdatesNumber = project.updatesNumber
            if (project.hasGroups() && pUpdatesNumber > 0) {
                val pDevelopmentDays = project.totalDevelopmentDays
                if (pDevelopmentDays > 0) {
                    val pAverageDevelopmentTime = project.averageDevelopmentTime
                    if (pUpdatesNumber >= updatesNumber && pDevelopmentDays <= developmentDays) {
                        if (pAverageDevelopmentTime < averageDevelopmentTime || bestProject == null) {
                            bestProject = project
                            updatesNumber = pUpdatesNumber
                            developmentDays = pDevelopmentDays
                            averageDevelopmentTime = pAverageDevelopmentTime
                        }
                    }
                }
            }
        }
        return bestProject
    }

    /**
     * Function to get the worst group project in terms of performance
     *
     * No-any params required
     *
     * @return the worst group project in terms of performance as [Project]
     */
    fun getWorstGroupProject(): Project? {
        var worstProject: Project? = null
        var updatesNumber = 0
        var developmentDays = 0
        var averageDevelopmentTime = 0
        if (bestGroupVProject != null) {
            userProjects.forEach { project ->
                if (project.hasGroups() && !project.id.equals(bestGroupVProject!!.id)) {
                    val pUpdatesNumber = project.updatesNumber
                    if (pUpdatesNumber > updatesNumber) {
                        updatesNumber = pUpdatesNumber
                        developmentDays = project.totalDevelopmentDays
                    }
                }
            }
            userProjects.forEach { project ->
                val pUpdatesNumber = project.updatesNumber
                if (project.hasGroups() && pUpdatesNumber > 0 && !project.id.equals(bestGroupVProject!!.id)) {
                    val pDevelopmentDays = project.totalDevelopmentDays
                    val pAverageDevelopmentTime = project.averageDevelopmentTime
                    if (pUpdatesNumber <= updatesNumber && pDevelopmentDays >= developmentDays) {
                        if (pAverageDevelopmentTime > averageDevelopmentTime || worstProject == null) {
                            worstProject = project
                            updatesNumber = pUpdatesNumber
                            developmentDays = pDevelopmentDays
                            averageDevelopmentTime = pAverageDevelopmentTime
                        }
                    }
                }
            }
        }
        return worstProject
    }

}