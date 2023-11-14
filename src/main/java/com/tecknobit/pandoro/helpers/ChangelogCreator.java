package com.tecknobit.pandoro.helpers;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.pandoro.records.Changelog.ChangelogEvent;
import com.tecknobit.pandoro.services.repositories.ChangelogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.tecknobit.pandoro.controllers.PandoroController.generateIdentifier;
import static com.tecknobit.pandoro.records.Changelog.ChangelogEvent.*;
import static com.tecknobit.pandoro.records.users.GroupMember.Role;
import static java.lang.System.currentTimeMillis;

@Service
public class ChangelogCreator {

    @Autowired
    private ChangelogsRepository changelogRepository;

    public void sendGroupInvite(String userId) {
        // TODO: 14/11/2023 SEND INVITATION
    }

    @Wrapper
    public void newMemberJoined(String groupId, String memberId) {
        createGroupChangelog(JOINED_GROUP, null, groupId, memberId);
    }

    @Wrapper
    public void yourGroupRoleChanged(String groupId, String memberId, Role role) {
        createGroupChangelog(ROLE_CHANGED, role.toString(), groupId, memberId);
    }

    @Wrapper
    public void memberLeftGroup(String groupId, String memberId) {
        createGroupChangelog(LEFT_GROUP, null, groupId, memberId);
    }

    @Wrapper
    public void groupDeleted(String groupName, String memberId) {
        createGroupChangelog(GROUP_DELETED, groupName, null, memberId);
    }

    private void createGroupChangelog(ChangelogEvent event, String extraContent, String groupId, String memberId) {
        changelogRepository.addGroupChangelog(generateIdentifier(), event, extraContent, currentTimeMillis(), groupId,
                memberId);
    }

    @Wrapper
    public void addedGroupProject(String projectId, String userId) {
        createProjectChangelog(PROJECT_ADDED, null, projectId, userId);
    }

    @Wrapper
    public void removedGroupProject(String projectId, String userId) {
        createProjectChangelog(PROJECT_REMOVED, null, projectId, userId);
    }

    @Wrapper
    public void scheduledNewUpdate(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_SCHEDULED, targetVersion, projectId, userId);
    }

    @Wrapper
    public void updateStarted(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_STARTED, targetVersion, projectId, userId);
    }

    @Wrapper
    public void updatePublished(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_PUBLISHED, targetVersion, projectId, userId);
    }

    @Wrapper
    public void updateDeleted(String targetVersion, String projectId, String userId) {
        createProjectChangelog(UPDATE_DELETED, targetVersion, projectId, userId);
    }

    private void createProjectChangelog(ChangelogEvent event, String targetVersion, String projectId, String userId) {
        changelogRepository.addProjectChangelog(generateIdentifier(), event, targetVersion, currentTimeMillis(),
                projectId, userId);
    }

    @Service
    public static class ChangelogOperator {

        @Autowired
        protected ChangelogCreator changelogCreator;

    }

}
