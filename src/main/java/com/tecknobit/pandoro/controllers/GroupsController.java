package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.GroupsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.services.GroupsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.NAME_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY;
import static helpers.InputsValidatorKt.*;

@RestController
@RequestMapping(path = BASE_ENDPOINT + GROUPS_KEY)
public class GroupsController extends PandoroController {

    public static final String GROUPS_KEY = "groups";

    public static final String CREATE_GROUP_ENDPOINT = "/createGroup";

    public static final String ADD_MEMBERS_ENDPOINT = "/addMembers";

    public static final String ACCEPT_GROUP_INVITATION_ENDPOINT = "/acceptGroupInvitation";

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

    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public <T> T getGroupsList(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token))
            return (T) groupsHelper.getGroups(id);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PostMapping(
            path = CREATE_GROUP_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String createGroup(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody String payload
    ) {
        User me = getMe(id, token);
        if (me != null) {
            JsonHelper hPayload = new JsonHelper(payload);
            String groupName = hPayload.getString(NAME_KEY);
            if (isGroupNameValid(groupName)) {
                if (!groupsHelper.groupExists(id, groupName)) {
                    String groupDescription = hPayload.getString(GROUP_DESCRIPTION_KEY);
                    if (isGroupDescriptionValid(groupDescription)) {
                        ArrayList<String> members = hPayload.fetchList(GROUP_MEMBERS_KEY);
                        if (checkMembersValidity(members)) {
                            groupsHelper.createGroup(me, generateIdentifier(), groupName, groupDescription, members);
                            return successResponse();
                        } else
                            return failedResponse("Wrong members list");
                    } else
                        return failedResponse("Wrong group description");
                } else
                    return failedResponse("A group with this name already exists");
            } else
                return failedResponse("Wrong group name");
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @GetMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}",
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public <T> T getGroup(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId
    ) {
        if (isAuthenticatedUser(id, token)) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null)
                return (T) group;
            else
                return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PutMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ADD_MEMBERS_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String addMembers(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String payload
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null && group.isUserMaintainer(me)) {
                groupsHelper.addMembers(new JsonHelper(payload).toList(), groupId);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ACCEPT_GROUP_INVITATION_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String acceptInvitation(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            System.out.println(groupId);
            if (group != null) {
                groupsHelper.acceptGroupInvitation(groupId, me);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + DELETE_GROUP_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String deleteGroup(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId
    ) {
        User me = usersRepository.getAuthorizedUser(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null && group.isUserAdmin(me)) {
                groupsHelper.deleteGroup(id, groupId);
                return successResponse();
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
