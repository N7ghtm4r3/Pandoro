package com.tecknobit.pandorocore;

/**
 * The {@code Endpoints} class is a container with all the Pandoro's endpoints
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class Endpoints {

    /**
     * {@code BASE_ENDPOINT} the base endpoint for the backend service
     */
    public static final String BASE_ENDPOINT = "/api/v1/";

    /**
     * {@code USERS_ENDPOINT} base endpoint for the users
     */
    public static final String USERS_ENDPOINT = "users";

    /**
     * {@code SIGN_UP_ENDPOINT} endpoint to sign up in the <b>Pandoro's system</b>
     */
    public static final String SIGN_UP_ENDPOINT = "/signUp";

    /**
     * {@code SIGN_IN_ENDPOINT} endpoint to sign in the <b>Pandoro's system</b>
     */
    public static final String SIGN_IN_ENDPOINT = "/signIn";

    /**
     * {@code CHANGE_PROFILE_PIC_ENDPOINT} endpoint to change the profile pic of the user
     */
    public static final String CHANGE_PROFILE_PIC_ENDPOINT = "/changeProfilePic";

    /**
     * {@code CHANGE_EMAIL_ENDPOINT} endpoint to change the email of the user
     */
    public static final String CHANGE_EMAIL_ENDPOINT = "/changeEmail";

    /**
     * {@code CHANGE_PASSWORD_ENDPOINT} endpoint to change the password of the user
     */
    public static final String CHANGE_PASSWORD_ENDPOINT = "/changePassword";

    /**
     * {@code CHANGE_LANGUAGE_ENDPOINT} endpoint to change the language of the user
     */
    public static final String CHANGE_LANGUAGE_ENDPOINT = "/changeLanguage";

    /**
     * {@code DELETE_ACCOUNT_ENDPOINT} endpoint to delete the account of the user
     */
    public static final String DELETE_ACCOUNT_ENDPOINT = "/deleteAccount";

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
     * {@code ADD_PROJECT_ENDPOINT} endpoint to add a new project
     */
    public static final String ADD_PROJECT_ENDPOINT = "/addProject";

    /**
     * {@code EDIT_PROJECT_ENDPOINT} endpoint to edit an existing project
     */
    public static final String EDIT_PROJECT_ENDPOINT = "/editProject";

    /**
     * {@code DELETE_PROJECT_ENDPOINT} endpoint to delete an existing project
     */
    public static final String DELETE_PROJECT_ENDPOINT = "/deleteProject";

    /**
     * {@code UPDATES_PATH} path for the updates operations
     */
    public static final String UPDATES_PATH = "/updates/";

    /**
     * {@code SCHEDULE_UPDATE_ENDPOINT} endpoint to schedule a new update for a project
     */
    public static final String SCHEDULE_UPDATE_ENDPOINT = "schedule";

    /**
     * {@code START_UPDATE_ENDPOINT} endpoint to start an existing update for a project
     */
    public static final String START_UPDATE_ENDPOINT = "/start";

    /**
     * {@code PUBLISH_UPDATE_ENDPOINT} endpoint to publish an existing update for a project
     */
    public static final String PUBLISH_UPDATE_ENDPOINT = "/publish";

    /**
     * {@code ADD_CHANGE_NOTE_ENDPOINT} endpoint to add a new change note for an update
     */
    public static final String ADD_CHANGE_NOTE_ENDPOINT = "/addChangeNote";

    /**
     * {@code MARK_CHANGE_NOTE_AS_DONE_ENDPOINT} endpoint to mark as done a change note of an update
     */
    public static final String MARK_CHANGE_NOTE_AS_DONE_ENDPOINT = "/markChangeNoteAsDone";

    /**
     * {@code MARK_CHANGE_NOTE_AS_TODO_ENDPOINT} endpoint to mark as todo a change note of an update
     */
    public static final String MARK_CHANGE_NOTE_AS_TODO_ENDPOINT = "/markChangeNoteAsToDo";

    /**
     * {@code DELETE_CHANGE_NOTE_ENDPOINT} endpoint to delete a change note of an update
     */
    public static final String DELETE_CHANGE_NOTE_ENDPOINT = "/deleteChangeNote";

    /**
     * {@code DELETE_UPDATE_ENDPOINT} endpoint to delete a project
     */
    public static final String DELETE_UPDATE_ENDPOINT = "/delete";

    /**
     * {@code CREATE_NOTE_ENDPOINT} endpoint to create a new note
     */
    public static final String CREATE_NOTE_ENDPOINT = "/create";

    /**
     * {@code MARK_AS_DONE_ENDPOINT} endpoint to mark as done an existing note
     */
    public static final String MARK_AS_DONE_ENDPOINT = "/markAsDone";

    /**
     * {@code MARK_AS_TO_DO_ENDPOINT} endpoint to mark as todo an existing note
     */
    public static final String MARK_AS_TO_DO_ENDPOINT = "/markAsToDo";

    /**
     * {@code DELETE_NOTE_ENDPOINT} endpoint to delete an existing note
     */
    public static final String DELETE_NOTE_ENDPOINT = "/deleteNote";

    /**
     * {@code READ_CHANGELOG_ENDPOINT} endpoint to read a changelog
     */
    public static final String READ_CHANGELOG_ENDPOINT = "/readChangelog";

    /**
     * {@code DELETE_CHANGELOG_ENDPOINT} endpoint to delete a changelog
     */
    public static final String DELETE_CHANGELOG_ENDPOINT = "/deleteChangelog";

    /**
     * Constructor to init the {@link Endpoints} class <br>
     * No-any params required
     */
    public Endpoints() {
    }

}