package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.users.GroupMember.Role;
import com.tecknobit.pandoro.records.users.PublicUser;
import com.tecknobit.pandoro.services.repositories.GroupMembersRepository;
import com.tecknobit.pandoro.services.repositories.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void createGroup(PublicUser user, String groupId, String groupName, String groupDescription) {
        String userId = user.getId();
        groupsRepository.createGroup(userId, groupId, groupName, groupDescription);
        membersRepository.insertMember(
                userId,
                user.getName(),
                user.getEmail(),
                user.getProfilePic(),
                user.getSurname(),
                Role.ADMIN,
                groupId
        );
    }

    public Group getGroup(String userId, String groupId) {
        return groupsRepository.getGroup(userId, groupId);
    }

    public void deleteGroup(String userId, String groupId) {
        groupsRepository.deleteGroup(userId, groupId);
    }

}
