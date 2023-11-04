package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.GroupsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_ENDPOINT;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;

@RestController
@RequestMapping(path = BASE_ENDPOINT + GROUPS_ENDPOINT)
public class GroupsController extends PandoroController {

    public static final String GROUPS_ENDPOINT = "groups";

    public static final String CREATE_GROUP_ENDPOINT = "/createGroup";

    public static final String ADD_MEMBERS_ENDPOINT = "/addMembers";

    public static final String CHANGE_MEMBER_ROLE_ENDPOINT = "/changeMemberRole";

    public static final String REMOVE_MEMBER_ENDPOINT = "/removeMember";

    public static final String EDIT_PROJECTS_ENDPOINT = "/editProjects";

    public static final String LEAVE_GROUP_ENDPOINT = "/leaveGroup";

    public static final String DELETE_GROUP_ENDPOINT = "/deleteGroup";

    private final GroupsHelper groupsHelper;

    @Autowired
    public GroupsController(GroupsHelper groupsHelper) {
        this.groupsHelper = groupsHelper;
    }


}
