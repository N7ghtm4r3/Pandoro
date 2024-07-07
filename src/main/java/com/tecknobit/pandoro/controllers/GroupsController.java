package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.equinox.environment.controllers.EquinoxController;
import com.tecknobit.pandoro.services.GroupsHelper;
import com.tecknobit.pandorocore.records.Group;
import com.tecknobit.pandorocore.records.Project;
import com.tecknobit.pandorocore.records.users.GroupMember;
import com.tecknobit.pandorocore.records.users.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*;
import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.FAILED;
import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL;
import static com.tecknobit.equinox.Requester.RESPONSE_STATUS_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.*;
import static com.tecknobit.pandorocore.Endpoints.*;
import static com.tecknobit.pandorocore.helpers.InputsValidator.Companion;
import static com.tecknobit.pandorocore.records.Changelog.CHANGELOG_IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.records.Group.*;
import static com.tecknobit.pandorocore.records.Project.PROJECTS_KEY;
import static com.tecknobit.pandorocore.records.structures.PandoroItem.IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.records.users.GroupMember.InvitationStatus.JOINED;
import static com.tecknobit.pandorocore.records.users.GroupMember.Role.ADMIN;

/**
 * The {@code GroupsController} class is useful to manage all the group operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxController
 * @see PandoroController
 */
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + GROUPS_KEY)
public class GroupsController extends PandoroController {

    /**
     * {@code CANNOT_EXECUTE_ACTION_ON_OWN_ACCOUNT_MESSAGE} message to use when the user tried to execute an action on its
     * account wrong
     */
    public static final String CANNOT_EXECUTE_ACTION_ON_OWN_ACCOUNT_MESSAGE = "action_executed_on_own_account_error_key";

    /**
     * {@code WRONG_ADMIN_MESSAGE} message to use when a wrong admin has been inserted
     */
    public static final String WRONG_ADMIN_MESSAGE = "wrong_admin_inserted_key";

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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups", method = GET)
    public <T> T getGroupsList(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isMe(id, token))
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/createGroup", method = POST)
    public String createGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody String payload
    ) {
        if (isMe(id, token)) {
            JsonHelper hPayload = new JsonHelper(payload);
            String groupName = hPayload.getString(NAME_KEY);
            if (Companion.isGroupNameValid(groupName)) {
                if (!groupsHelper.groupExists(id, groupName)) {
                    String groupDescription = hPayload.getString(GROUP_DESCRIPTION_KEY);
                    if (Companion.isGroupDescriptionValid(groupDescription)) {
                        ArrayList<String> members = hPayload.fetchList(GROUP_MEMBERS_KEY);
                        if (Companion.checkMembersValidity(members)) {
                            groupsHelper.createGroup(me, generateIdentifier(), groupName, groupDescription, members);
                            return successResponse();
                        } else
                            return failedResponse("wrong_members_list_key");
                    } else
                        return failedResponse("wrong_group_description_key");
                } else
                    return failedResponse("group_name_already_exists_key");
            } else
                return failedResponse("wrong_group_name_key");
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = GET)
    public <T> T getGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId
    ) {
        if (isMe(id, token)) {
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
     * @param payload: the payload with the list of the member to add
     *
     * @return the result of the request as {@link String}
     */
    @PutMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ADD_MEMBERS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/addMembers", method = PUT)
    public String addMembers(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, Object> payload
    ) {
        if (isMe(id, token)) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null && group.isUserMaintainer(me)) {
                loadJsonHelper(payload);
                List<?> members = jsonHelper.getJSONArray(GROUP_MEMBERS_KEY, new JSONArray()).toList();
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
     * @param payload: the payload with the changelog identifier to delete
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ACCEPT_GROUP_INVITATION_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/acceptGroupInvitation", method = PATCH)
    public String acceptInvitation(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, String> payload
    ) {
        if (isMe(id, token)) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null) {
                loadJsonHelper(payload);
                try {
                    groupsHelper.acceptGroupInvitation(groupId, jsonHelper.getString(CHANGELOG_IDENTIFIER_KEY), me);
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
     * @param payload: the payload with the changelog identifier to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + DECLINE_GROUP_INVITATION_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/declineGroupInvitation", method = DELETE)
    public String declineInvitation(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, String> payload
    ) {
        if (isMe(id, token)) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null) {
                loadJsonHelper(payload);
                try {
                    groupsHelper.declineGroupInvitation(groupId, jsonHelper.getString(CHANGELOG_IDENTIFIER_KEY), me);
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
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/changeMemberRole", method = PATCH)
    public String changeMemberRole(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody String payload
    ) {
        if (isMe(id, token)) {
            Group meGroup = groupsHelper.getGroup(id, groupId);
            JsonHelper hPayload = new JsonHelper(payload);
            String hisId = hPayload.getString(IDENTIFIER_KEY, "");
            Group uGroup = groupsHelper.getGroup(hisId, groupId);
            if (!id.equals(hisId)) {
                if (meGroup != null && uGroup != null && isNotTheAuthor(uGroup, hisId)) {
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
     * @param payload: payload with the member identifier to remove
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + REMOVE_MEMBER_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/removeMember", method = DELETE)
    public String removeMember(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, String> payload
    ) {
        if (isMe(id, token)) {
            Group meGroup = groupsHelper.getGroup(id, groupId);
            loadJsonHelper(payload);
            String memberId = jsonHelper.getString(IDENTIFIER_KEY);
            Group uGroup = groupsHelper.getGroup(memberId, groupId);
            if (!id.equals(memberId)) {
                if (meGroup != null && uGroup != null && isNotTheAuthor(uGroup, memberId)) {
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
     * Method to check whether the member of the operation is not the author of the group
     *
     * @param group: the group of the operation
     * @param memberId: the identifier of the member to check
     * @return whether the member of the operation is not the author of the group as boolean
     */
    private boolean isNotTheAuthor(Group group, String memberId) {
        return !group.getAuthor().getId().equals(memberId);
    }

    /**
     * Method to edit the projects of a group
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param groupId: the identifier of the group where edit the projects list
     * @param payload: the payload with the list of projects for the group
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + EDIT_PROJECTS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/editProjects", method = PATCH)
    public String editProjects(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, Object> payload
    ) {
        if (isMe(id, token)) {
            User me = super.me;
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null && group.isUserAdmin(me)) {
                loadJsonHelper(payload);
                List<String> projectsList = jsonHelper.fetchList(PROJECTS_KEY, new ArrayList<>());
                ArrayList<String> projectsIds = new ArrayList<>();
                for (Project project : me.getProjects())
                    projectsIds.add(project.getId());
                if (projectsIds.containsAll(projectsList) && !projectsList.isEmpty()) {
                    groupsHelper.editProjects(groupId, projectsList);
                    return successResponse();
                } else
                    return failedResponse("wrong_projects_list_key");
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
     * @param payload: the payload with the identifier of the admin, if required
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + LEAVE_GROUP_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/leaveGroup", method = DELETE)
    public String leaveGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody(required = false) Map<String, String> payload
    ) {
        if (isMe(id, token)) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null) {
                GroupMember meMember = groupsHelper.getGroupMember(groupId, me);
                if (meMember != null) {
                    if (meMember.isAdmin()) {
                        if (groupsHelper.hasOtherMembers(groupId)) {
                            if (!groupsHelper.hasGroupAdmins(id, groupId)) {
                                loadJsonHelper(payload);
                                String nextAdminId = jsonHelper.getString(IDENTIFIER_KEY);
                                if (nextAdminId != null && groupsHelper.getGroup(nextAdminId, groupId) != null) {
                                    String result = changeMemberRole(id, token, groupId, new JSONObject()
                                            .put(IDENTIFIER_KEY, nextAdminId)
                                            .put(MEMBER_ROLE_KEY, ADMIN).toString());
                                    if (roleChanged(result)) {
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
     * Method to check if the role changed correctly
     *
     * @param result: the response of the request
     * @return whether the request has been successful as boolean
     */
    private boolean roleChanged(String result) {
        return StandardResponseCode.valueOf(JsonHelper.getString(new JSONObject(result),
                RESPONSE_STATUS_KEY, FAILED.name())) == SUCCESSFUL;
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
            path = "/{" + GROUP_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = DELETE)
    public String deleteGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId
    ) {
        if (isMe(id, token)) {
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
