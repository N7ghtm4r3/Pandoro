package com.tecknobit.pandorocore.ui

import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.Project

/**
 * Function to populate the frequent projects list
 *
 * @param projectsList: the main projects list from get the frequent projects
 * @return frequent projects list as [List] of [Project]
 */
fun populateFrequentProjects(
    projectsList: List<Project>
): List<Project> {
    val frequentProjects = mutableListOf<Project>()
    val updatesNumber = ArrayList<Int>()
    for (project in projectsList)
        updatesNumber.add(project.updatesNumber)
    updatesNumber.sortDescending()
    for (updates in updatesNumber) {
        if (frequentProjects.size < 9) {
            for (project in projectsList) {
                if (project.updatesNumber == updates && !frequentProjects.contains(project)) {
                    frequentProjects.add(project)
                    break
                }
            }
        }
    }
    if (frequentProjects.size > 9)
        frequentProjects.subList(8, frequentProjects.size - 1).clear()
    return frequentProjects
}

/**
 * Function to filter the projects list
 *
 * @param query: the query to filter the projects list
 * @param list: the list of the [Project] to filter
 *
 * @return projects list filtered as [List] of [Project]
 */
fun filterProjects(
    query: String,
    list: List<Project>
): List<Project> {
    return if (query.isEmpty())
        list
    else {
        val checkQuery = query.uppercase()
        val filteredList = mutableListOf<Project>()
        for (project in list) {
            if (project.name.uppercase().contains(checkQuery) ||
                project.shortDescription.uppercase().contains(checkQuery) ||
                project.description.uppercase().contains(checkQuery) ||
                project.version.uppercase().contains(checkQuery) ||
                groupMatch(project.groups, checkQuery)
            ) {
                filteredList.add(project)
            }
        }
        return filteredList
    }
}

/**
 * Function to check whether name match with a group of the list
 *
 * @param groups: the groups of the project
 * @param name: the name to do the check
 *
 * @return whether name match with a group of the list as [Boolean]
 */
fun groupMatch(
    groups: ArrayList<Group>,
    name: String
): Boolean {
    groups.forEach { group: Group ->
        if (group.name.uppercase().contains(name))
            return true
    }
    return false
}