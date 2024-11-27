package com.tecknobit.pandorocore.enums

import java.util.*

/**
 * `RepositoryPlatform` list of available repository platforms
 */
enum class RepositoryPlatform {

    /**
     * `Github` repository platform
     */
    Github,

    /**
     * `GitLab` repository platforms
     */
    GitLab;

    companion object {

        /**
         * Method to reach a platform value
         *
         * @param url: the url to fetch the platform
         * @return repository platform as [RepositoryPlatform]
         */
        fun reachPlatform(
            url: String,
        ): RepositoryPlatform? {
            if (isValidPlatform(url)) {
                return if (url.contains(Github.name.lowercase(Locale.getDefault()))) Github
                else GitLab
            }
            return null
        }

        /**
         * Method to check a repository platform validity
         *
         * @param url: the url to check the platform
         * @return whether the repository platform is valid as boolean
         */
        fun isValidPlatform(
            url: String,
        ): Boolean {
            for (platform in entries) if (url.contains(platform.name.lowercase(Locale.getDefault()))) return true
            return false
        }

    }

}