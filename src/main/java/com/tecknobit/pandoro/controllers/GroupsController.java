package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.pandoro.records.Group;
import com.tecknobit.pandoro.records.Project;
import com.tecknobit.pandoro.records.users.GroupMember;
import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.GroupsHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*;
import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.helpers.InputsValidatorKt.*;
import static com.tecknobit.pandoro.records.users.GroupMember.InvitationStatus.JOINED;
import static com.tecknobit.pandoro.records.users.GroupMember.Role.ADMIN;
import static com.tecknobit.pandoro.services.GroupsHelper.*;
import static com.tecknobit.pandoro.services.UsersHelper.NAME_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY;

/**
 * The {@code GroupsController} class is useful to manage all the group operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroController
 */
@RestController
@RequestMapping(path = BASE_ENDPOINT + GROUPS_KEY)
public class GroupsController extends PandoroController {

    /**
     * {@code GROUPS_KEY} groups key
     */
    public static final String GROUPS_KEY = "groups";

    /**
     * {@code CREATE_GROUP_ENDPOINT} endpoint to create a new group
     */
    public static final String CREATE_GROUP_ENDPOINT = "/createGroup";

    /**
     * {@code ADD_MEMBERS_ENDPOINT} endpoint to add member to group
     */
    public static final String ADD_MEMBERS_ENDPOINT = "/addMembers";

    /**
     * {@code ACCEPT_GROUP_INVITATION_ENDPOINT} endpoint to accept a group invitation
     */
    public static final String ACCEPT_GROUP_INVITATION_ENDPOINT = "/acceptGroupInvitation";

    /**
     * {@code DECLINE_GROUP_INVITATION_ENDPOINT} endpoint to decline a group invitation
     */
    public static final String DECLINE_GROUP_INVITATION_ENDPOINT = "/declineGroupInvitation";

    /**
     * {@code CHANGE_MEMBER_ROLE_ENDPOINT} endpoint to change the role of a member of a group
     */
    public static final String CHANGE_MEMBER_ROLE_ENDPOINT = "/changeMemberRole";

    /**
     * {@code REMOVE_MEMBER_ENDPOINT} endpoint to remove a member of a group
     */
    public static final String REMOVE_MEMBER_ENDPOINT = "/removeMember";

    /**
     * {@code EDIT_PROJECTS_ENDPOINT} endpoint to edit the projects of a group
     */
    public static final String EDIT_PROJECTS_ENDPOINT = "/editProjects";

    /**
     * {@code LEAVE_GROUP_ENDPOINT} endpoint to leave from a group
     */
    public static final String LEAVE_GROUP_ENDPOINT = "/leaveGroup";

    /**
     * {@code DELETE_GROUP_ENDPOINT} endpoint to delete a group
     */
    public static final String DELETE_GROUP_ENDPOINT = "/deleteGroup";

    /**
     * {@code WRONG_ADMIN_MESSAGE} message to use when a wrong admin has been inserted
     */
    public static final String WRONG_ADMIN_MESSAGE = "You need to insert a valid new admin";

    /**
     * {@code groupsHelper} instance to manage the groups database operations
     */
    private final GroupsHelper groupsHelper;

    /**
     * Constructor to init a {@link GroupsController} controller
     *
     * @param groupsHelper: instance to manage the groups database operations
     */
    @Autowired
    public GroupsController(GroupsHelper groupsHelper) {
        this.groupsHelper = groupsHelper;
    }

    /**
     * Method to get a groups list
     *
     * @param id:    the identifier of the user
     * @param token: the token of the user
     * @return the result of the request as {@link String} if fails or {@link JSONArray} if is successfully
     */
    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups", method = GET)
    public <T> T getGroupsList(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token))
            return (T) groupsHelper.getGroups(id);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to create a new group
     *
     * @param id:      the identifier of the user
     * @param token:   the token of the user
     * @param payload: payload of the request
     *                 <pre>
     *                      {@code
     *                              {
     *                                  "name" : "name of the group", -> [String]
     *                                  "group_description": "description of the group", -> [String]
     *                                  "members" : [ -> [List of Strings or empty]
     *                                      // id of the group member -> [String]
     *                                  ]
     *                              }
     *                      }
     *                 </pre>
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = CREATE_GROUP_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/createGroup", method = POST)
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

    /**
     * Method to get a single group
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group to fetch
     *
     * @return the result of the request as {@link String} if fails or {@link JSONObject} if is successfully
     */
    @GetMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}",
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}", method = GET)
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

    /**
     * Method to add members to a group
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group where add the members
     * @param membersList: the list of the member to add
     *
     * @return the result of the request as {@link String}
     */
    @PutMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ADD_MEMBERS_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/addMembers", method = PUT)
    public String addMembers(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String membersList
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null && group.isUserMaintainer(me)) {
                List<?> members = new JsonHelper(membersList).toList();
                if (!members.isEmpty()) {
                    groupsHelper.addMembers(group.getName(), (List<String>) members, groupId);
                    return successResponse();
                } else
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to accept a group invitation
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group to accept the invitation
     * @param changelogId: the changelog identifier to delete
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ACCEPT_GROUP_INVITATION_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/acceptGroupInvitation", method = PATCH)
    public String acceptInvitation(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String changelogId
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null) {
                try {
                    groupsHelper.acceptGroupInvitation(groupId, changelogId, me);
                    return successResponse();
                } catch (IllegalAccessException e) {
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
                }
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to decline a group invitation
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group to decline the invitation
     * @param changelogId: the changelog identifier to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + DECLINE_GROUP_INVITATION_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/declineGroupInvitation", method = DELETE)
    public String declineInvitation(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String changelogId
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null) {
                try {
                    groupsHelper.declineGroupInvitation(groupId, changelogId, me);
                    return successResponse();
                } catch (IllegalAccessException e) {
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
                }
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to change the role of a group member
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group where change the role of a member
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "id" : "identifier of the member", -> [String]
     *                  "role": "new role of the member" -> [InvitationStatus]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + CHANGE_MEMBER_ROLE_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/changeMemberRole", method = PATCH)
    public String changeMemberRole(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String payload
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group meGroup = groupsHelper.getGroup(id, groupId);
            JsonHelper hPayload = new JsonHelper(payload);
            String hisId = hPayload.getString(IDENTIFIER_KEY, "");
            Group uGroup = groupsHelper.getGroup(hisId, groupId);
            if (!id.equals(hisId)) {
                if (meGroup != null && uGroup != null) {
                    GroupMember iMember = groupsHelper.getGroupMember(groupId, me);
                    GroupMember heMember = groupsHelper.getGroupMember(groupId, hisId);
                    if (iMember != null && heMember != null && heMember.getInvitationStatus() == JOINED) {
                        boolean isMeAdmin = iMember.isAdmin();
                        boolean isMeMaintainer = iMember.isMaintainer();
                        boolean isHeAdmin = heMember.isAdmin();
                        boolean isHeMaintainer = heMember.isMaintainer();
                        try {
                            GroupMember.Role role = GroupMember.Role.valueOf(hPayload.getString(MEMBER_ROLE_KEY));
                            if (isMeAdmin) {
                                groupsHelper.changeMemberRole(heMember.getId(), groupId, role);
                                return successResponse();
                            } else if (isMeMaintainer) {
                                if (!isHeMaintainer || !isHeAdmin) {
                                    groupsHelper.changeMemberRole(heMember.getId(), groupId, role);
                                    return successResponse();
                                } else
                                    return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                            } else
                                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                        } catch (IllegalArgumentException e) {
                            return failedResponse(WRONG_PROCEDURE_MESSAGE);
                        }
                    } else
                        return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                } else
                    return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
            } else
                return failedResponse(CANNOT_EXECUTE_ACTION_ON_OWN_ACCOUNT_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to remove a group member
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group where remove the member
     * @param memberId: the member identifier to remove
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + REMOVE_MEMBER_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/removeMember", method = DELETE)
    public String removeMember(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String memberId
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group meGroup = groupsHelper.getGroup(id, groupId);
            Group uGroup = groupsHelper.getGroup(memberId, groupId);
            if (!id.equals(memberId)) {
                if (meGroup != null && uGroup != null) {
                    GroupMember iMember = groupsHelper.getGroupMember(groupId, me);
                    GroupMember heMember = groupsHelper.getGroupMember(groupId, memberId);
                    if (iMember != null && heMember != null) {
                        boolean isMeAdmin = iMember.isAdmin();
                        boolean isMeMaintainer = iMember.isMaintainer();
                        boolean isHeAdmin = heMember.isAdmin();
                        boolean isHeMaintainer = heMember.isMaintainer();
                        if (isMeAdmin) {
                            groupsHelper.removeMember(heMember.getId(), groupId);
                            return successResponse();
                        } else if (isMeMaintainer) {
                            if (!isHeMaintainer || !isHeAdmin) {
                                groupsHelper.removeMember(heMember.getId(), groupId);
                                return successResponse();
                            } else
                                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                        } else
                            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                    } else
                        return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
                } else
                    return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
            } else
                return failedResponse(CANNOT_EXECUTE_ACTION_ON_OWN_ACCOUNT_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to edit the projects of a group
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group where edit the projects list
     * @param projects: the list of projects for the group
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + EDIT_PROJECTS_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/editProjects", method = PATCH)
    public String editProjects(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String projects
    ) {
        User me = usersRepository.getAuthorizedUser(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null && group.isUserAdmin(me)) {
                List<String> projectsList = JsonHelper.toList(new JSONArray(projects));
                ArrayList<String> projectsIds = new ArrayList<>();
                for (Project project : me.getProjects())
                    projectsIds.add(project.getId());
                if (projectsIds.containsAll(projectsList)) {
                    groupsHelper.editProjects(groupId, projectsList);
                    return successResponse();
                } else
                    return failedResponse("Wrong projects list");
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to leave from a group
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group from leave
     * @param nextAdminId: the identifier of the admin, if required
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + LEAVE_GROUP_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/leaveGroup", method = DELETE)
    public String leaveGroup(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody(required = false) String nextAdminId
    ) {
        User me = getMe(id, token);
        if (me != null) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null) {
                GroupMember meMember = groupsHelper.getGroupMember(groupId, me);
                if (meMember != null) {
                    if (meMember.isAdmin()) {
                        if (groupsHelper.hasOtherMembers(groupId)) {
                            if (!groupsHelper.hasGroupAdmins(id, groupId)) {
                                if (nextAdminId != null && groupsHelper.getGroup(nextAdminId, groupId) != null) {
                                    String result = changeMemberRole(id, token, groupId, new JSONObject()
                                            .put(IDENTIFIER_KEY, nextAdminId)
                                            .put(MEMBER_ROLE_KEY, ADMIN).toString());
                                    if (JsonHelper.getBoolean(new JSONObject(result), SUCCESS_KEY, true)) {
                                        groupsHelper.leaveGroup(id, groupId);
                                        return successResponse();
                                    } else
                                        return failedResponse(WRONG_ADMIN_MESSAGE);
                                } else
                                    return failedResponse(WRONG_ADMIN_MESSAGE);
                            } else {
                                groupsHelper.leaveGroup(id, groupId, false);
                                return successResponse();
                            }
                        } else {
                            groupsHelper.leaveGroup(id, groupId, true);
                            return successResponse();
                        }
                    } else {
                        groupsHelper.leaveGroup(id, groupId);
                        return successResponse();
                    }
                } else
                    return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
            } else
                return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to delete a group
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + DELETE_GROUP_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/groups/{group_id}/deleteGroup", method = DELETE)
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
