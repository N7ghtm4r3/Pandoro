package com.tecknobit.pandoro.helpers;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper;
import com.tecknobit.pandoro.services.changelogs.repository.ChangelogsRepository;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandorocore.enums.ChangelogEvent;
import com.tecknobit.pandorocore.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController.generateIdentifier;
import static com.tecknobit.pandorocore.enums.ChangelogEvent.*;
import static java.lang.System.currentTimeMillis;

/**
 * The {@code ChangelogsCreator} class is useful to manage the creation and the queries to the database for the changelogs
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Service
public class ChangelogsCreator {

    /**
     * {@code changelogsRepository} instance for the changelog repository
     */
    @Autowired
    private ChangelogsRepository changelogRepository;

    /**
     * Method to create a changelog when the user has been invited in a {@link Group}
     *
     * @param groupId:   the group identifier
     * @param groupName: the name of the group of the invite
     * @param memberId:  the member id, the changelog owner
     */
    @Wrapper
    public void sendGroupInvite(String groupId, String groupName, String memberId) {
        createGroupChangelog(INVITED_GROUP, groupName, groupId, memberId);
    }

    /**
     * Method to create a changelog when the user joined in a {@link Group}
     *
     * @param groupId:  the group identifier
     * @param memberId: the member id, the changelog owner
     */
    @Wrapper
    public void newMemberJoined(String groupId, String memberId) {
        createGroupChangelog(JOINED_GROUP, null, groupId, memberId);
    }

    /**
     * Method to create a changelog when the role of the user changed
     *
     * @param groupId: the group identifier
     * @param memberId: the member id, the changelog owner
     * @param role: the new role of the user
     */
    @Wrapper
    public void yourGroupRoleChanged(String groupId, String memberId, Role role) {
        createGroupChangelog(ROLE_CHANGED, role.toString(), groupId, memberId);
    }

    /**
     * Method to create a changelog when the user changed left a {@link Group}
     *
     * @param groupId: the group identifier
     * @param memberId: the member id, the changelog owner
     */
    @Wrapper
    public void memberLeftGroup(String groupId, String memberId) {
        createGroupChangelog(LEFT_GROUP, null, groupId, memberId);
    }

    /**
     * Method to create a changelog when a {@link Group} has been deleted
     *
     * @param groupName: the group name
     * @param memberId: the member id, the changelog owner
     */
    @Wrapper
    public void groupDeleted(String groupName, String memberId) {
        createGroupChangelog(GROUP_DELETED, groupName, null, memberId);
    }

    /**
     * Method to create a changelog for a {@link Group}
     *
     * @param event: the event of the changelog
     * @param extraContent: the extra content of the changelog
     * @param groupId: the group identifier
     * @param memberId: the member id, the changelog owner
     */
    private void createGroupChangelog(ChangelogEvent event, String extraContent, String groupId, String memberId) {
        changelogRepository.addGroupChangelog(generateIdentifier(), event, extraContent, currentTimeMillis(), groupId,
                memberId);
    }

    /**
     * Method to create a changelog when the project has been added to a {@link Group}
     *
     * @param projectId: the project identifier
     * @param userId: the user id, the changelog owner
     */
    @Wrapper
    public void addedGroupProject(String projectId, String userId) {
        createProjectChangelog(PROJECT_ADDED, null, projectId, userId);
    }

    /**
     * Method to create a changelog when the project has been removed from a {@link Group}
     *
     * @param projectId: the project identifier
     * @param userId: the user id, the changelog owner
     */
    @Wrapper
    public void removedGroupProject(String projectId, String userId) {
        createProjectChangelog(PROJECT_REMOVED, null, projectId, userId);
    }

    /**
     * Method to create a changelog when an update of project has been scheduled
     *
     * @param targetVersion: the target version of the project's update
     * @param projectId: the project identifier
     * @param userId: the user id, the changelog owner
     */
    @Wrapper
    public void scheduledNewUpdate(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_SCHEDULED, targetVersion, projectId, userId);
    }

    /**
     * Method to create a changelog when an update of project has been started
     *
     * @param targetVersion: the target version of the project's update
     * @param projectId: the project identifier
     * @param userId: the user id, the changelog owner
     */
    @Wrapper
    public void updateStarted(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_STARTED, targetVersion, projectId, userId);
    }

    /**
     * Method to create a changelog when an update of project has been published
     *
     * @param targetVersion: the target version of the project's update
     * @param projectId: the project identifier
     * @param userId: the user id, the changelog owner
     */
    @Wrapper
    public void updatePublished(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_PUBLISHED, targetVersion, projectId, userId);
    }

    /**
     * Method to create a changelog when an update of project has been deleted
     *
     * @param targetVersion: the target version of the project's update
     * @param projectId: the project identifier
     * @param userId: the user id, the changelog owner
     */
    @Wrapper
    public void updateDeleted(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_DELETED, targetVersion, projectId, userId);
    }

    /**
     * Method to create a changelog for a {@link Project}
     *
     * @param event: the event of the changelog
     * @param targetVersion: the target version of the project's update
     * @param projectId: the project identifier
     * @param userId: the user id, the changelog owner
     */
    private void createProjectChangelog(ChangelogEvent event, String targetVersion, String projectId, String userId) {
        changelogRepository.addProjectChangelog(generateIdentifier(), event, targetVersion, currentTimeMillis(),
                projectId, userId);
    }

    /**
     * The {@code ChangelogOperator} class is useful to manage the creation of the changelogs
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    @Service
    public static class ChangelogOperator extends EquinoxItemsHelper {

        /**
         * {@code changelogsCreator} the changelogs creator helper
         */
        @Autowired
        protected ChangelogsCreator changelogsCreator;

    }

}
