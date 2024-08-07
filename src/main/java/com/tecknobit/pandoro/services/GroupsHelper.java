package com.tecknobit.pandoro.services;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.pandoro.helpers.ChangelogsCreator.ChangelogOperator;
import com.tecknobit.pandoro.services.repositories.ChangelogsRepository;
import com.tecknobit.pandoro.services.repositories.UsersRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupsRepository;
import com.tecknobit.pandorocore.records.Group;
import com.tecknobit.pandorocore.records.Project;
import com.tecknobit.pandorocore.records.users.GroupMember;
import com.tecknobit.pandorocore.records.users.GroupMember.Role;
import com.tecknobit.pandorocore.records.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandorocore.records.users.GroupMember.InvitationStatus.JOINED;
import static com.tecknobit.pandorocore.records.users.GroupMember.InvitationStatus.PENDING;
import static com.tecknobit.pandorocore.records.users.GroupMember.Role.ADMIN;
import static com.tecknobit.pandorocore.records.users.GroupMember.Role.DEVELOPER;

/**
 * The {@code GroupsHelper} class is useful to manage all the groups database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ChangelogOperator
 */
@Service
public class GroupsHelper extends ChangelogOperator {

    /**
     * {@code usersRepository} instance for the users repository
     */
    @Autowired
    private UsersRepository usersRepository;

    /**
     * {@code groupsRepository} instance for the groups repository
     */
    @Autowired
    private GroupsRepository groupsRepository;

    /**
     * {@code membersRepository} instance for the members of a group repository
     */
    @Autowired
    private GroupMembersRepository membersRepository;

    /**
     * {@code changelogsRepository} instance for the changelogs repository
     */
    @Autowired
    private ChangelogsRepository changelogsRepository;

    /**
     * Method to get the user's groups list
     *
     * @param userId: the user identifier
     * @return the groups list as {@link List} of {@link Group}
     */
    public List<Group> getGroups(String userId) {
        return groupsRepository.getGroups(userId);
    }

    /**
     * Method to check whether the user's group exists
     *
     * @param userId:    the user identifier
     * @param groupName: the name of the group
     * @return whether the user's group exists as boolean
     */
    public boolean groupExists(String userId, String groupName) {
        return groupsRepository.getGroupByName(userId, groupName) != null;
    }

    /**
     * Method to create a group
     *
     * @param author:           the author of the group
     * @param groupId:          the identifier of the new group
     * @param groupName:        the name of the group
     * @param groupDescription: the description of the group
     * @param members:          the list of the group members
     */
    public void createGroup(User author, String groupId, String groupName, String groupDescription,
                            ArrayList<String> members) {
        String authorId = author.getId();
        groupsRepository.createGroup(
                authorId,
                groupId,
                groupName,
                System.currentTimeMillis(),
                groupDescription
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
        addMembers(groupName, members, groupId);
    }

    /**
     * Method to get the user's group by its id
     *
     * @param userId: the user identifier
     * @param groupId: the group identifier
     * @return the group as {@link Group}
     */
    public Group getGroup(String userId, String groupId) {
        return groupsRepository.getGroup(userId, groupId);
    }

    /**
     * Method to add a list of members to a group
     *
     * @param groupName: the name of the group
     * @param members: the list of members to add
     * @param groupId: the group identifier where add the members
     */
    public void addMembers(String groupName, List<String> members, String groupId) {
        for (String memberEmail : members) {
            User member = usersRepository.getUserByEmail(memberEmail.toLowerCase());
            if (member != null) {
                String memberId = member.getId();
                String email = member.getEmail();
                if (membersRepository.getGroupMemberByEmail(memberId, groupId, email) == null) {
                    membersRepository.insertMember(
                            memberId,
                            member.getName(),
                            email,
                            member.getProfilePic(),
                            member.getSurname(),
                            DEVELOPER,
                            PENDING,
                            groupId
                    );
                    changelogsCreator.sendGroupInvite(groupId, groupName, memberId);
                }
            }
        }
    }

    /**
     * Method to accept a group invitation
     *
     * @param groupId: the group identifier
     * @param user: the user who accepts the invitation
     */
    public void acceptGroupInvitation(String groupId, String changelogId, User user) throws IllegalAccessException {
        String userId = user.getId();
        if (changelogsRepository.getChangelog(changelogId, userId) == null)
            throw new IllegalAccessException();
        membersRepository.acceptGroupInvitation(userId, groupId);
        List<GroupMember> members = membersRepository.getGroupMembers(groupId);
        changelogsRepository.deleteChangelog(userId, changelogId);
        for (GroupMember member : members)
            changelogsCreator.newMemberJoined(groupId, member.getId());
    }

    /**
     * Method to decline a group invitation
     *
     * @param groupId: the group identifier
     * @param user: the user who declines the invitation
     */
    public void declineGroupInvitation(String groupId, String changelogId, User user) throws IllegalAccessException {
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
     * @param groupId: the group identifier
     * @param user: the user to fetch
     * @return the member of a group as {@link GroupMember}
     */
    public GroupMember getGroupMember(String groupId, User user) {
        return membersRepository.getGroupMemberByEmail(user.getId(), groupId, user.getEmail());
    }

    /**
     * Method to get a group member
     *
     * @param groupId: the group identifier
     * @param userId: the user identifier
     * @return the member of a group as {@link GroupMember}
     */
    public GroupMember getGroupMember(String groupId, String userId) {
        return membersRepository.getGroupMember(userId, groupId);
    }

    /**
     * Method to change the role of a member
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     * @param role: the new role for a member
     */
    public void changeMemberRole(String memberId, String groupId, Role role) {
        membersRepository.changeMemberRole(memberId, groupId, role);
        changelogsCreator.yourGroupRoleChanged(groupId, memberId, role);
    }

    /**
     * Method to remove a member from a group
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     */
    public void removeMember(String memberId, String groupId) {
        membersRepository.leaveGroup(memberId, groupId);
    }

    /**
     * Method to edit the projects list of a group
     *
     * @param groupId: the group identifier
     * @param projects: the projects list of a group to edit
     */
    public void editProjects(String groupId, List<String> projects) {
        List<String> currentProjects = groupsRepository.getGroupProjectsIds(groupId);
        List<GroupMember> groupMembers = membersRepository.getGroupMembers(groupId);
        currentProjects.removeAll(projects);
        for (String project : currentProjects) {
            groupsRepository.removeGroupProject(project, groupId);
            for (GroupMember member : groupMembers)
                changelogsCreator.removedGroupProject(project, member.getId());
        }
        projects.removeAll(groupsRepository.getGroupProjectsIds(groupId));
        for (String project : projects) {
            groupsRepository.addGroupProject(project, groupId);
            for (GroupMember member : groupMembers)
                changelogsCreator.addedGroupProject(project, member.getId());
        }
    }

    /**
     * Method to check if the group has other admins
     *
     * @param memberId: the member identifier
     * @param groupId: the group identifier
     * @return whether the group has other admin
     */
    public boolean hasGroupAdmins(String memberId, String groupId) {
        return !membersRepository.getGroupAdmins(memberId, groupId).isEmpty();
    }

    /**
     * Method to check if the group has other members
     *
     * @param groupId: the group identifier
     * @return whether the group has other members
     */
    public boolean hasOtherMembers(String groupId) {
        return membersRepository.getGroupMembers(groupId).size() > 1;
    }

    /**
     * Method to leave from a group
     *
     * @param memberId: the identifier of the member
     * @param groupId: the group identifier
     */
    @Wrapper
    public void leaveGroup(String memberId, String groupId) {
        leaveGroup(memberId, groupId, false);
    }

    /**
     * Method to leave from a group
     *
     * @param memberId: the identifier of the member
     * @param groupId: the group identifier
     * @param deleteGroup: whether delete the group after the member left
     */
    public void leaveGroup(String memberId, String groupId, boolean deleteGroup) {
        if (deleteGroup)
            deleteGroup(memberId, groupId);
        else {
            Group group = getGroup(memberId, groupId);
            membersRepository.leaveGroup(memberId, groupId);
            for (Project project : group.getProjects())
                if (project.getAuthor().getId().equals(memberId))
                    groupsRepository.removeGroupProject(project.getId(), groupId);
            changelogsCreator.memberLeftGroup(groupId, memberId);
        }
    }

    /**
     * Method to delete a group
     *
     * @param memberId: the identifier of the member
     * @param groupId:  the group identifier
     */
    public void deleteGroup(String memberId, String groupId) {
        List<GroupMember> members = membersRepository.getGroupMembers(groupId);
        String groupName = groupsRepository.getGroup(memberId, groupId).getName();
        groupsRepository.deleteGroup(groupId);
        for (GroupMember member : members)
            changelogsCreator.groupDeleted(groupName, member.getId());
    }

}
