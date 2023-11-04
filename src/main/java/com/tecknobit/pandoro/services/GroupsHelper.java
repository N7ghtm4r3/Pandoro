package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.services.repositories.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsHelper {

    public static final String GROUP_IDENTIFIER_KEY = "group_id";

    public static final String GROUP_KEY = "group";

    public static final String GROUP_DESCRIPTION_KEY = "group_description";

    public static final String GROUP_MEMBERS_KEY = "members";

    public static final String GROUP_PROJECTS_KEY = "group_projects";

    public static final String MEMBER_ROLE_KEY = "role";

    @Autowired
    private GroupsRepository groupsRepository;

}
