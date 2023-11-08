package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.users.GroupMember;
import com.tecknobit.pandoro.records.users.GroupMember.Role;
import com.tecknobit.pandoro.records.users.PublicUser;
import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.repositories.UsersRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupMembersRepository;
import com.tecknobit.pandoro.services.repositories.groups.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.records.users.GroupMember.InvitationStatus.JOINED;
import static com.tecknobit.pandoro.records.users.GroupMember.InvitationStatus.PENDING;
import static com.tecknobit.pandoro.records.users.GroupMember.Role.ADMIN;
import static com.tecknobit.pandoro.records.users.GroupMember.Role.DEVELOPER;

@Service
public class GroupsHelper {

    public static final String GROUP_IDENTIFIER_KEY = "group_id";

    public static final String GROUP_KEY = "group_member";

    public static final String GROUP_DESCRIPTION_KEY = "group_description";

    public static final String GROUP_MEMBERS_KEY = "members";

    public static final String TOTAL_GROUP_MEMBERS_KEY = "total_members";

    public static final String GROUP_PROJECTS_KEY = "group_projects";

    public static final String TOTAL_GROUP_PROJECTS_KEY = "total_projects";

    public static final String MEMBER_ROLE_KEY = "role";

    public static final String INVITATION_STATUS_KEY = "invitation_status";

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private GroupMembersRepository membersRepository;

    public List<Group> getGroups(String userId) {
        return groupsRepository.getGroups(userId);
    }

    public boolean groupExists(String userId, String groupName) {
        return groupsRepository.getGroupByName(userId, groupName) != null;
    }

    public void createGroup(PublicUser user, String groupId, String groupName, String groupDescription,
                            ArrayList<String> members) {
        String userId = user.getId();
        groupsRepository.createGroup(userId, groupId, groupName, groupDescription);
        membersRepository.insertMember(
                userId,
                user.getName(),
                user.getEmail(),
                user.getProfilePic(),
                user.getSurname(),
                ADMIN,
                JOINED,
                groupId
        );
        addMembers(members, groupId);
    }

    public Group getGroup(String userId, String groupId) {
        return groupsRepository.getGroup(userId, groupId);
    }

    public void addMembers(List<String> members, String groupId) {
        for (String memberEmail : members) {
            // TODO: 08/11/2023 CREATE CHANGELOG FOR EACH INVITE
            PublicUser member = usersRepository.getUserByEmail(memberEmail);
            if (member != null) {
                String userId = member.getId();
                String email = member.getEmail();
                if (membersRepository.getGroupMemberByEmail(userId, groupId, email) == null) {
                    membersRepository.insertMember(
                            userId,
                            member.getName(),
                            email,
                            member.getProfilePic(),
                            member.getSurname(),
                            DEVELOPER,
                            PENDING,
                            groupId
                    );
                }
            }
        }
    }

    public void acceptGroupInvitation(String groupId, User user) {
        membersRepository.acceptGroupInvitation(user.getId(), groupId);
    }

    public void declineGroupInvitation(String groupId, User user) throws IllegalAccessException {
        String userId = user.getId();
        if (membersRepository.getGroupMemberByEmail(userId, groupId, user.getEmail()).getInvitationStatus() == PENDING)
            membersRepository.leaveGroup(userId, groupId);
        else
            throw new IllegalAccessException();
    }

    public GroupMember getGroupMember(String groupId, User user) {
        return membersRepository.getGroupMemberByEmail(user.getId(), groupId, user.getEmail());
    }

    public GroupMember getGroupMember(String groupId, String userId) {
        return membersRepository.getGroupMember(userId, groupId);
    }

    public void changeMemberRole(String memberId, String groupId, Role role) {
        membersRepository.changeMemberRole(memberId, groupId, role);
    }

    public void deleteGroup(String userId, String groupId) {
        groupsRepository.deleteGroup(userId, groupId);
    }

}
