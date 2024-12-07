package com.tecknobit.pandoro.services.groups.controller;

import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.groups.dto.GroupDTO;
import com.tecknobit.pandoro.services.groups.entity.Group;
import com.tecknobit.pandoro.services.groups.service.GroupsHelper;
import com.tecknobit.pandoro.services.projects.entities.Project;
import com.tecknobit.pandoro.services.users.entities.GroupMember;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandorocore.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL;
import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.InvitationStatus.JOINED;
import static com.tecknobit.pandorocore.enums.Role.ADMIN;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.*;
import static com.tecknobit.pandorocore.helpers.PandoroInputsValidator.INSTANCE;
import static com.tecknobit.pandorocore.helpers.PandoroInputsValidator.WRONG_GROUP_LOGO_MESSAGE;

/**
 * The {@code GroupsController} class is useful to manage all the group operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController
 * @see DefaultPandoroController
 */
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + GROUPS_KEY)
public class GroupsController extends DefaultPandoroController {

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
     * {@code groupsHelper} instance to manage the projects_groups database operations
     */
    @Autowired
    private GroupsHelper groupsHelper;

    /**
     * Method to get a projects_groups list
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param authoredGroups Whether retrieve only the groups authored by the requesting user
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups", method = GET)
    public <T> T getGroups(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize,
            @RequestParam(name = ONLY_AUTHORED_GROUPS, defaultValue = "false", required = false) boolean authoredGroups
    ) {
        if (isMe(id, token))
            return (T) successResponse(groupsHelper.getGroups(id, page, pageSize, authoredGroups));
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to create a new group
     *
     * @param id The identifier of the user
     * @param token The token of the user
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
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups", method = POST)
    public String createGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @ModelAttribute GroupDTO payload
    ) {
        if (!isMe(id, token))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        String groupName = payload.name();
        MultipartFile logo = payload.logo();
        if (logo == null || logo.isEmpty())
            return failedResponse(WRONG_GROUP_LOGO_MESSAGE);
        if (!INSTANCE.isGroupNameValid(groupName))
            return failedResponse("wrong_group_name_key");
        if (groupsHelper.groupExists(id, groupName))
            return failedResponse("group_name_already_exists_key");
        String groupDescription = payload.group_description();
        if (!INSTANCE.isGroupDescriptionValid(groupDescription))
            return failedResponse("wrong_group_description_key");
        List<String> members = payload.members();
        if (!INSTANCE.checkMembersValidity(members))
            return failedResponse("wrong_members_list_key");
        try {
            groupsHelper.createGroup(me, generateIdentifier(), payload);
        } catch (IOException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    /**
     * Method to get a single group
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group to fetch
     *
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}", method = GET)
    public <T> T getGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId
    ) {
        if (isMe(id, token)) {
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null)
                return (T) successResponse(group);
            else
                return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        } else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to add members to a group
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group where add the members
     * @param payload The payload with the list of the member to add
     *
     * @return the result of the request as {@link String}
     */
    @PutMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ADD_MEMBERS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}/addMembers", method = PUT)
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
                List<String> members = jsonHelper.fetchList(GROUP_MEMBERS_KEY);
                if (INSTANCE.checkMembersValidity(members)) {
                    groupsHelper.addMembers(group.getName(), members, groupId);
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
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group to accept the invitation
     * @param payload The payload with the changelog identifier to delete
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + ACCEPT_GROUP_INVITATION_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}/acceptGroupInvitation", method = PATCH)
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
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group to decline the invitation
     * @param payload The payload with the changelog identifier to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + DECLINE_GROUP_INVITATION_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}/declineGroupInvitation", method = DELETE)
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
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group where change the role of a member
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
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}/changeMemberRole", method = PATCH)
    public String changeMemberRole(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, String> payload
    ) {
        if (isMe(id, token)) {
            Group meGroup = groupsHelper.getGroup(id, groupId);
            loadJsonHelper(payload);
            String hisId = jsonHelper.getString(IDENTIFIER_KEY, "");
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
                            Role role = Role.valueOf(jsonHelper.getString(MEMBER_ROLE_KEY));
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
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group where remove the member
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
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}/removeMember", method = DELETE)
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
     * @param group The group of the operation
     * @param memberId The identifier of the member to check
     * @return whether the member of the operation is not the author of the group as boolean
     */
    private boolean isNotTheAuthor(Group group, String memberId) {
        return !group.getAuthor().getId().equals(memberId);
    }

    /**
     * Method to edit the projects of a group
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group where edit the projects list
     * @param payload The payload with the list of projects for the group
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + EDIT_PROJECTS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}/editProjects", method = PATCH)
    public String editProjects(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, Object> payload
    ) {
        if (isMe(id, token)) {
            PandoroUser me = super.me;
            Group group = groupsHelper.getGroup(id, groupId);
            if (group != null && group.isUserAdmin(me)) {
                loadJsonHelper(payload);
                List<String> projectsList = jsonHelper.fetchList(PROJECTS_KEY, new ArrayList<>());
                ArrayList<String> projectsIds = new ArrayList<>();
                for (Project project : me.getProjects())
                    projectsIds.add(project.getId());
                if (projectsIds.containsAll(projectsList)) {
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
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group from leave
     * @param payload The payload with the identifier of the admin, if required
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}" + LEAVE_GROUP_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}/leaveGroup", method = DELETE)
    public String leaveGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody(required = false) Map<String, String> payload
    ) {
        if (!isMe(id, token))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        Group group = groupsHelper.getGroup(id, groupId);
        if (group == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        GroupMember meMember = groupsHelper.getGroupMember(groupId, me);
        if (meMember == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        if (meMember.isAdmin()) {
            if (groupsHelper.hasOtherMembers(groupId)) {
                if (!groupsHelper.hasGroupAdmins(id, groupId)) {
                    loadJsonHelper(payload);
                    String nextAdminId = jsonHelper.getString(IDENTIFIER_KEY);
                    if (nextAdminId == null || groupsHelper.getGroup(nextAdminId, groupId) == null)
                        return failedResponse(WRONG_ADMIN_MESSAGE);
                    HashMap<String, String> changeRolePayload = new HashMap<>();
                    changeRolePayload.put(IDENTIFIER_KEY, nextAdminId);
                    changeRolePayload.put(MEMBER_ROLE_KEY, ADMIN.name());
                    String result = changeMemberRole(id, token, groupId, changeRolePayload);
                    if (roleChanged(result)) {
                        groupsHelper.leaveGroup(id, groupId);
                        return successResponse();
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
    }

    /**
     * Method to check if the role changed correctly
     *
     * @param result The response of the request
     * @return whether the request has been successful as boolean
     */
    private boolean roleChanged(String result) {
        return result.contains(SUCCESSFUL.name());
    }

    /**
     * Method to delete a group
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param groupId The identifier of the group to delete
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/projects_groups/{group_id}", method = DELETE)
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
