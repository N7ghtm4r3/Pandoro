package com.tecknobit.pandoro.services.groups.service;

import com.tecknobit.equinoxbackend.annotations.TableColumns;
import com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper;
import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import com.tecknobit.pandoro.configuration.PandoroResourcesManager;
import com.tecknobit.pandoro.services.changelogs.helpers.ChangelogsNotifier;
import com.tecknobit.pandoro.services.changelogs.repository.ChangelogsRepository;
import com.tecknobit.pandoro.services.groups.dto.GroupDTO;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.groups.repositories.GroupMembersRepository;
import com.tecknobit.pandoro.services.groups.repositories.GroupsRepository;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.users.entities.GroupMember;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandoro.services.users.repository.PandoroUsersRepository;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper.InsertCommand.REPLACE_INTO;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.InvitationStatus.JOINED;
import static com.tecknobit.pandorocore.enums.InvitationStatus.PENDING;
import static com.tecknobit.pandorocore.enums.Role.ADMIN;
import static com.tecknobit.pandorocore.enums.Role.DEVELOPER;

/**
 * The {@code GroupsService} class is useful to manage all the groups database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxItemsHelper
 */
@Service
public class GroupsService extends EquinoxItemsHelper implements PandoroResourcesManager {

    /**
     * {@code usersRepository} instance for the users repository
     */
    private final PandoroUsersRepository usersRepository;

    /**
     * {@code groupsRepository} instance for the groups repository
     */
    private final GroupsRepository groupsRepository;

    /**
     * {@code membersRepository} instance for the members of a group repository
     */
    private final GroupMembersRepository membersRepository;

    /**
     * {@code changelogsRepository} instance for the changelogs repository
     */
    private final ChangelogsRepository changelogsRepository;

    /**
     * {@code changelogsNotifier} instance used to notify a changelog event
     */
    private final ChangelogsNotifier changelogsNotifier;

    /**
     * Constructor to init the service
     *
     * @param usersRepository      The instance for the users repository
     * @param groupsRepository     The instance for the groups repository
     * @param membersRepository    The instance for the members of a group repository
     * @param changelogsRepository The instance for the changelogs repository
     * @param changelogsNotifier The instance used to notify a changelog event
     */
    @Autowired
    public GroupsService(PandoroUsersRepository usersRepository, GroupsRepository groupsRepository,
                         GroupMembersRepository membersRepository, ChangelogsRepository changelogsRepository,
                         ChangelogsNotifier changelogsNotifier) {
        this.usersRepository = usersRepository;
        this.groupsRepository = groupsRepository;
        this.membersRepository = membersRepository;
        this.changelogsRepository = changelogsRepository;
        this.changelogsNotifier = changelogsNotifier;
    }

    /**
     * Method to get the user's groups list
     *
     * @param userId The user identifier
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param authoredGroups Whether retrieve only the groups authored by the requesting user
     * @param groupName The name of the group to use as filter
     * @param roles The role values to use as filter
     * @return the groups list as {@link PaginatedResponse} of {@link Group}
     */
    public PaginatedResponse<Group> getGroups(String userId, int page, int pageSize, boolean authoredGroups,
                                              String groupName, List<String> roles) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Group> groups;
        long groupsCount;
        if (authoredGroups) {
            groups = groupsRepository.getAuthoredGroups(userId, groupName, pageable);
            groupsCount = groupsRepository.getAuthoredGroupsCount(userId, groupName);
        } else {
            groups = groupsRepository.getGroups(userId, groupName, roles, pageable);
            groupsCount = groupsRepository.getGroupsCount(userId, groupName, roles);
        }
        return new PaginatedResponse<>(groups, page, pageSize, groupsCount);
    }

    /**
     * Method to check whether the user's group exists
     *
     * @param userId The user identifier
     * @param groupName The name of the group
     * @return whether the user's group exists as boolean
     */
    public boolean groupExists(String userId, String groupName) {
        return groupsRepository.getGroupByName(userId, groupName) != null;
    }

    /**
     * Method to create a group
     *
     * @param author The author of the group
     * @param groupId The identifier of the new group
     * @param group The payload with the group details
     */
    public void createGroup(PandoroUser author, String groupId, GroupDTO group) throws IOException {
        String authorId = author.getId();
        String groupName = group.name();
        MultipartFile logo = group.logo();
        long operationDate = System.currentTimeMillis();
        String logoPath = createGroupLogoResource(logo, groupId + operationDate);
        groupsRepository.createGroup(
                authorId,
                groupId,
                groupName,
                logoPath,
                operationDate,
                group.group_description()
        );
        membersRepository.insertMember(
                authorId,
                author.getName(),
                author.getEmail(),
                author.getProfilePic(),
                author.getSurname(),
                ADMIN,
                JOINED,
                groupId
        );
        addMembers(groupName, group.members(), groupId);
        editProjects(groupId, group.projects());
        saveResource(logo, logoPath);
    }

    /**
     * Method to add a list of members to a group
     *
     * @param groupName The name of the group
     * @param members   The list of members to add
     * @param groupId   The group identifier where add the members
     */
    public void addMembers(String groupName, List<String> members, String groupId) {
        batchInsert(REPLACE_INTO, GROUP_MEMBERS_TABLE, new BatchQuery<PandoroUser>() {
            @Override
            public Collection<PandoroUser> getData() {
                ArrayList<PandoroUser> filteredMembers = new ArrayList<>();
                for (String member : members)
                    usersRepository.findById(member).ifPresent(filteredMembers::add);
                return filteredMembers;
            }

            @Override
            @TableColumns(columns = {GROUP_MEMBER_KEY, IDENTIFIER_KEY, EMAIL_KEY, INVITATION_STATUS_KEY, NAME_KEY,
                    PROFILE_PIC_KEY, MEMBER_ROLE_KEY, SURNAME_KEY})
            public void prepareQuery(Query query, int index, Collection<PandoroUser> members) {
                for (PandoroUser member : members) {
                    String memberId = member.getId();
                    query.setParameter(index++, groupId);
                    query.setParameter(index++, memberId);
                    query.setParameter(index++, member.getEmail());
                    query.setParameter(index++, PENDING.name());
                    query.setParameter(index++, member.getName());
                    query.setParameter(index++, member.getProfilePic());
                    query.setParameter(index++, DEVELOPER.name());
                    query.setParameter(index++, member.getSurname());
                    changelogsNotifier.sendGroupInvite(groupId, groupName, memberId);
                }
            }

            @Override
            public String[] getColumns() {
                return new String[]{GROUP_MEMBER_KEY, IDENTIFIER_KEY, EMAIL_KEY, INVITATION_STATUS_KEY, NAME_KEY,
                        PROFILE_PIC_KEY, MEMBER_ROLE_KEY, SURNAME_KEY};
            }
        });
    }

    /**
     * Method to create a group
     *
     * @param author  The author of the group
     * @param groupId The identifier of the new group
     * @param group   The payload with the group details
     */
    public void editGroup(PandoroUser author, String groupId, GroupDTO group) throws IOException {
        String requester = author.getId();
        String groupName = group.name();
        String groupDescription = group.group_description();
        MultipartFile logo = group.logo();
        if (logo != null && !logo.isEmpty()) {
            deleteGroupLogoResource(groupId);
            String logoPath = createGroupLogoResource(logo, groupId + System.currentTimeMillis());
            groupsRepository.editGroup(
                    groupId,
                    logoPath,
                    groupName,
                    groupDescription
            );
            saveResource(logo, logoPath);
        } else {
            groupsRepository.editGroup(
                    groupId,
                    groupName,
                    groupDescription
            );
        }
        editMembers(requester, groupId, groupName, group.members());
        editProjects(groupId, group.projects());
    }

    /**
     * Method to edit the members list of a group
     *
     * @param requester The user who request the operation
     * @param groupId The group identifier
     * @param groupName The name of the group
     * @param members The members list of a group to edit
     */
    public void editMembers(String requester, String groupId, String groupName, List<String> members) {
        List<GroupMember> groupMembers = membersRepository.getAllGroupMembers(groupId);
        SyncBatchModel model = new SyncBatchModel() {
            @Override
            public Collection<String> getCurrentData() {
                ArrayList<String> members = new ArrayList<>(groupMembers.stream().map(GroupMember::getId).toList());
                members.remove(requester);
                return members;
            }

            @Override
            public String[] getDeletingColumns() {
                return new String[]{IDENTIFIER_KEY};
            }

            @Override
            public void afterSync() {
                for (GroupMember member : groupMembers)
                    changelogsNotifier.newMemberJoined(groupId, member.getId());
            }
        };
        BatchQuery<String> batchQuery = new BatchQuery<>() {
            @Override
            public Collection<String> getData() {
                return members;
            }

            @Override
            @TableColumns(columns = {GROUP_MEMBER_KEY, IDENTIFIER_KEY, EMAIL_KEY, INVITATION_STATUS_KEY, NAME_KEY,
                    PROFILE_PIC_KEY, MEMBER_ROLE_KEY, SURNAME_KEY})
            public void prepareQuery(Query query, int index, Collection<String> members) {
                for (String member : members) {
                    PandoroUser pandoroUser = usersRepository.findById(member).orElse(null);
                    if (pandoroUser == null)
                        break;
                    query.setParameter(index++, groupId);
                    query.setParameter(index++, member);
                    query.setParameter(index++, pandoroUser.getEmail());
                    query.setParameter(index++, PENDING);
                    query.setParameter(index++, pandoroUser.getName());
                    query.setParameter(index++, pandoroUser.getProfilePic());
                    query.setParameter(index++, DEVELOPER);
                    query.setParameter(index++, pandoroUser.getSurname());
                    changelogsNotifier.sendGroupInvite(groupId, groupName, member);
                }
            }

            @Override
            public String[] getColumns() {
                return new String[]{GROUP_MEMBER_KEY, IDENTIFIER_KEY, EMAIL_KEY, INVITATION_STATUS_KEY, NAME_KEY,
                        PROFILE_PIC_KEY, MEMBER_ROLE_KEY, SURNAME_KEY};
            }
        };
        syncBatch(model, GROUP_MEMBERS_TABLE, batchQuery);
    }

    /**
     * Method to get the user's group by its id
     *
     * @param userId The user identifier
     * @param groupId The group identifier
     * @return the group as {@link Group}
     */
    public Group getGroup(String userId, String groupId) {
        return groupsRepository.getGroup(userId, groupId);
    }

    /**
     * Method to accept a group invitation
     *
     * @param groupId The group identifier
     * @param user The user who accepts the invitation
     */
    public void acceptGroupInvitation(String groupId, String changelogId, PandoroUser user) throws IllegalAccessException {
        String userId = user.getId();
        if (changelogsRepository.getChangelog(changelogId, userId) == null)
            throw new IllegalAccessException();
        membersRepository.acceptGroupInvitation(userId, groupId);
        List<GroupMember> members = membersRepository.getGroupMembers(groupId);
        changelogsRepository.deleteChangelog(userId, changelogId);
        for (GroupMember member : members)
            changelogsNotifier.newMemberJoined(groupId, member.getId());
    }

    /**
     * Method to decline a group invitation
     *
     * @param groupId The group identifier
     * @param user The user who declines the invitation
     */
    public void declineGroupInvitation(String groupId, String changelogId, PandoroUser user) throws IllegalAccessException {
        String userId = user.getId();
        if (changelogsRepository.getChangelog(changelogId, userId) == null)
            throw new IllegalAccessException();
        if (membersRepository.getGroupMemberByEmail(userId, groupId, user.getEmail()).getInvitationStatus() == PENDING) {
            membersRepository.leaveGroup(userId, groupId);
            changelogsRepository.deleteChangelog(userId, changelogId);
        } else
            throw new IllegalAccessException();
    }

    /**
     * Method to get a group member
     *
     * @param groupId The group identifier
     * @param user The user to fetch
     * @return the member of a group as {@link GroupMember}
     */
    public GroupMember getGroupMember(String groupId, PandoroUser user) {
        return membersRepository.getGroupMemberByEmail(user.getId(), groupId, user.getEmail());
    }

    /**
     * Method to get a group member
     *
     * @param groupId The group identifier
     * @param userId The user identifier
     * @return the member of a group as {@link GroupMember}
     */
    public GroupMember getGroupMember(String groupId, String userId) {
        return membersRepository.getGroupMember(userId, groupId);
    }

    /**
     * Method to change the role of a member
     *
     * @param memberId The member identifier
     * @param groupId The group identifier
     * @param role The new role for a member
     */
    public void changeMemberRole(String memberId, String groupId, com.tecknobit.pandorocore.enums.Role role) {
        membersRepository.changeMemberRole(memberId, groupId, role);
        changelogsNotifier.yourGroupRoleChanged(groupId, memberId, role);
    }

    /**
     * Method to remove a member from a group
     *
     * @param memberId The member identifier
     * @param groupId The group identifier
     */
    public void removeMember(String memberId, String groupId) {
        membersRepository.leaveGroup(memberId, groupId);
    }

    /**
     * Method to edit the projects list of a group
     *
     * @param groupId The group identifier
     * @param projects The projects list of a group to edit
     */
    public void editProjects(String groupId, ArrayList<String> projects) {
        List<String> currentProjects = groupsRepository.getGroupProjectsIds(groupId);
        List<GroupMember> groupMembers = membersRepository.getGroupMembers(groupId);
        currentProjects.removeAll(projects);
        for (String project : currentProjects) {
            groupsRepository.removeGroupProject(project, groupId);
            for (GroupMember member : groupMembers)
                changelogsNotifier.removedGroupProject(project, member.getId());
        }
        projects.removeAll(groupsRepository.getGroupProjectsIds(groupId));
        for (String project : projects) {
            groupsRepository.addGroupProject(project, groupId);
            for (GroupMember member : groupMembers)
                changelogsNotifier.addedGroupProject(project, member.getId());
        }
    }

    /**
     * Method to leave from a group
     *
     * @param memberId The identifier of the member
     * @param group The group from leave
     */
    public void leaveGroup(String memberId, Group group) {
        String groupId = group.getId();
        if (group.getMembers().size() - 1 == 0)
            deleteGroup(memberId, groupId);
        else {
            membersRepository.leaveGroup(memberId, groupId);
            for (Project project : group.getProjects())
                if (project.getAuthor().getId().equals(memberId))
                    groupsRepository.removeGroupProject(project.getId(), groupId);
            changelogsNotifier.memberLeftGroup(groupId, memberId);
        }
    }

    /**
     * Method to delete a group
     *
     * @param memberId The identifier of the member
     * @param groupId:  the group identifier
     */
    public void deleteGroup(String memberId, String groupId) {
        List<GroupMember> members = membersRepository.getGroupMembers(groupId);
        String groupName = groupsRepository.getGroup(memberId, groupId).getName();
        groupsRepository.deleteGroup(groupId);
        deleteGroupLogoResource(groupId);
        for (GroupMember member : members)
            changelogsNotifier.groupDeleted(groupName, member.getId());
    }

}
