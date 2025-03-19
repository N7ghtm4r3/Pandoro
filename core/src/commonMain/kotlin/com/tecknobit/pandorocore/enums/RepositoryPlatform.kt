package com.tecknobit.pandorocore.enums

import com.tecknobit.equinoxcore.annotations.Validator
import kotlinx.serialization.Serializable

/**
 * `RepositoryPlatform` list of available repository platforms
 */
@Serializable
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
                return if (url.contains(Github.name.lowercase()))
                    Github
                else
                    GitLab
            }
            return null
        }

        /**
         * Method to check a repository platform validity
         *
         * @param url: the url to check the platform
         * @return whether the repository platform is valid as boolean
         */
        @Validator
        fun isValidPlatform(
            url: String,
        ): Boolean {
            for (platform in entries)
                if (url.contains(platform.name.lowercase()))
                    return true
            return false
        }

    }

}