package com.tecknobit.pandoro.configuration;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.equinoxbackend.apis.resources.ResourcesManager;
import org.springframework.web.multipart.MultipartFile;

public interface PandoroResourcesManager extends ResourcesManager {

    /**
     * {@code PROJECT_ICONS_DIRECTORY} the folder where the project icons will be saved
     */
    String PROJECT_ICONS_DIRECTORY = "icons";

    /**
     * {@code GROUP_LOGOS_DIRECTORY} the folder where the group logos will be saved
     */
    String GROUP_LOGOS_DIRECTORY = "logos";

    /**
     * Method to create the pathname for a project icon
     *
     * @param resource   The resource from create its pathname
     * @param resourceId The resource identifier
     * @return the pathname created for a project icon
     */
    @Wrapper
    default String createProjectIconResource(MultipartFile resource, String resourceId) {
        return createResource(resource, PROJECT_ICONS_DIRECTORY, resourceId);
    }

    /**
     * Method to delete a project icon
     *
     * @param projectId The user identifier of the project icon to delete
     * @return whether the project icon has been deleted as boolean
     */
    @Wrapper
    default boolean deleteProjectIconResource(String projectId) {
        return deleteResource(PROJECT_ICONS_DIRECTORY, projectId);
    }

    /**
     * Method to create the pathname for a group logo
     *
     * @param resource   The resource from create its pathname
     * @param resourceId The resource identifier
     * @return the pathname created for a group logo
     */
    @Wrapper
    default String createGroupLogoResource(MultipartFile resource, String resourceId) {
        return createResource(resource, GROUP_LOGOS_DIRECTORY, resourceId);
    }

    /**
     * Method to delete a group logo
     *
     * @param groupId The user identifier of the project icon to delete
     * @return whether the project icon has been deleted as boolean
     */
    @Wrapper
    default boolean deleteGroupLogoResource(String groupId) {
        return deleteResource(GROUP_LOGOS_DIRECTORY, groupId);
    }

}
