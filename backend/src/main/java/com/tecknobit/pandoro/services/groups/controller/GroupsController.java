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
import java.util.List;
import java.util.Map;

import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.*;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.enums.InvitationStatus.JOINED;
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
     * {@code groupsHelper} instance to manage the groups database operations
     */
    @Autowired
    private GroupsHelper groupsHelper;

    /**
     * Method to get a groups list
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param authoredGroups Whether retrieve only the groups authored by the requesting user
     * @param groupName The name of the group to use as filter
     * @param roles The role values to use as filter
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups", method = GET)
    public <T> T getGroups(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize,
            @RequestParam(name = ONLY_AUTHORED_GROUPS, defaultValue = "false", required = false) boolean authoredGroups,
            @RequestParam(name = NAME_KEY, defaultValue = "", required = false) String groupName,
            @RequestParam(
                    name = ROLES_FILTER_KEY,
                    defaultValue = DEFAULT_ROLES_FILTER_VALUE,
                    required = false
            ) List<String> roles
    ) {
        if (isMe(id, token))
            return (T) successResponse(groupsHelper.getGroups(id, page, pageSize, authoredGroups, groupName, roles));
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
     *                                  "logo" : "logo of the group", -> [String]
     *                                  "name" : "name of the group", -> [String]
     *                                  "group_description": "description of the group", -> [String]
     *                                  "members" : [ -> [List of Strings or empty]
     *                                      // id of the group member -> [String]
     *                                  ],
     *                                  "projects" : [ -> [List of Strings or empty]
     *                                      // id of the projects -> [String]
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
    @RequestPath(path = "/api/v1/users/{id}/groups", method = POST)
    public String createGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @ModelAttribute GroupDTO payload
    ) {
        String isValidRequest = isValidRequest(id, token, payload, false);
        if (isValidRequest != null)
            return failedResponse(isValidRequest);
        try {
            groupsHelper.createGroup(me, generateIdentifier(), payload);
        } catch (IOException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    /**
     * Method to create edit an existing group
     *
     * @param id       The identifier of the user
     * @param token    The token of the user
     * @param payload: payload of the request
     *                 <pre>
     *                                      {@code
     *                                              {
     *                                                  "logo" : "logo of the group", -> [String]
     *                                                  "name" : "name of the group", -> [String]
     *                                                  "group_description": "description of the group", -> [String]
     *                                                  "members" : [ -> [List of Strings or empty]
     *                                                      // id of the group member -> [String]
     *                                                  ],
     *                                                  "projects" : [ -> [List of Strings or empty]
     *                                                      // id of the projects -> [String]
     *                                                  ]
     *                                              }
     *                                      }
     *                                 </pre>
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = "/{" + GROUP_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = POST)
    public String editGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestHeader(TOKEN_KEY) String token,
            @ModelAttribute GroupDTO payload
    ) {
        String isValidRequest = isValidRequest(id, token, payload, true);
        if (isValidRequest != null)
            return failedResponse(isValidRequest);
        if (groupsHelper.getGroup(id, groupId) == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        try {
            groupsHelper.editGroup(me, groupId, payload);
        } catch (IOException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    /**
     * Method to check the validity of a request between {@link #createGroup(String, String, GroupDTO)} and
     * {@link #editGroup(String, String, String, GroupDTO)}
     *
     * @param id       The identifier of the user
     * @param token    The token of the user
     * @param payload: payload of the request
     *                 <pre>
     *                                      {@code
     *                                              {
     *                                                  "logo" : "logo of the group", -> [String]
     *                                                  "name" : "name of the group", -> [String]
     *                                                  "group_description": "description of the group", -> [String]
     *                                                  "members" : [ -> [List of Strings or empty]
     *                                                      // id of the group member -> [String]
     *                                                  ],
     *                                                  "projects" : [ -> [List of Strings or empty]
     *                                                      // id of the projects -> [String]
     *                                                  ]
     *                                              }
     *                                      }
     *                                 </pre>
     * @return the result of the validation as {@link String}
     */
    private String isValidRequest(String id, String token, GroupDTO payload, boolean editingMode) {
        if (!isMe(id, token))
            return WRONG_PROCEDURE_MESSAGE;
        String groupName = payload.name();
        MultipartFile logo = payload.logo();
        if (!editingMode && (logo == null || logo.isEmpty()))
            return WRONG_GROUP_LOGO_MESSAGE;
        if (!INSTANCE.isGroupNameValid(groupName))
            return "wrong_group_name_key";
        if (!editingMode && groupsHelper.groupExists(id, groupName))
            return "group_name_already_exists_key";
        String groupDescription = payload.group_description();
        if (!INSTANCE.isGroupDescriptionValid(groupDescription))
            return "wrong_group_description_key";
        if (!me.getProjectsIds().containsAll(payload.projects()))
            return WRONG_PROCEDURE_MESSAGE;
        return null;
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = GET)
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
                List<String> members = jsonHelper.fetchList(GROUP_MEMBERS_KEY);
                groupsHelper.addMembers(group.getName(), members, groupId);
                return successResponse();
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/acceptGroupInvitation", method = PATCH)
    public String acceptInvitation(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody Map<String, String> payload
    ) {
        if (!isMe(id, token))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        Group group = groupsHelper.getGroup(id, groupId);
        if (group == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        loadJsonHelper(payload);
        try {
            groupsHelper.acceptGroupInvitation(groupId, jsonHelper.getString(CHANGELOG_IDENTIFIER_KEY), me);
            return successResponse();
        } catch (IllegalAccessException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/changeMemberRole", method = PATCH)
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/editProjects", method = PATCH)
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/leaveGroup", method = DELETE)
    public String leaveGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId,
            @RequestBody(required = false) Map<String, String> payload
    ) {
        if (!isMe(id, token))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        Group group = groupsHelper.getGroup(id, groupId);
        if (group == null || group.getAuthor().getId().equals(id))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        GroupMember meMember = groupsHelper.getGroupMember(groupId, me);
        if (meMember == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        groupsHelper.leaveGroup(id, group);
        return successResponse();
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = DELETE)
    public String deleteGroup(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(GROUP_IDENTIFIER_KEY) String groupId
    ) {
        if (!isMe(id, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        Group group = groupsHelper.getGroup(id, groupId);
        if (group != null && group.getAuthor().getId().equals(me.getId())) {
            groupsHelper.deleteGroup(id, groupId);
            return successResponse();
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
