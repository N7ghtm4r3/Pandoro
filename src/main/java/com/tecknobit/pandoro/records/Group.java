package com.tecknobit.pandoro.records;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code Group} class is useful to create a <b>Pandoro's Group</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItem
 * @see Serializable
 */
public class Group extends PandoroItem {

    /**
     * {@code GROUP_NAME_MAX_LENGTH} the max length of the name for a group
     */
    public static final int GROUP_NAME_MAX_LENGTH = 15;

    /**
     * {@code GROUP_DESCRIPTION_MAX_LENGTH} the max description of the name for a group
     */
    public static final int GROUP_DESCRIPTION_MAX_LENGTH = 30;

    /**
     * {@code Role} list of available roles for a group's member
     */
    public enum Role {

        /**
         * {@code ADMIN} role
         *
         * @apiNote this role allows to manage the members of the group, so add or remove them, and also manage projects,
         * so add or remove them
         */
        ADMIN,

        /**
         * {@code MAINTAINER} role
         *
         * @apiNote this role allows to manage the members of the group, so add or remove them
         */
        MAINTAINER,

        /**
         * {@code DEVELOPER} role
         *
         * @apiNote this role allows see the members of the group and the projects managed by the group
         */
        DEVELOPER

    }

    /**
     * {@code author} the author of the group
     */
    private final User author;

    /**
     * {@code description} the description of the group
     */
    private final String description;

    /**
     * {@code members} the list of the members of the group
     */
    private final ArrayList<Member> members;

    /**
     * {@code totalMembers} how many members the group has
     */
    private final int totalMembers;

    /**
     * {@code projects} the list of the projects managed by the group
     */
    private final ArrayList<Project> projects;

    /**
     * {@code totalProjects} the number of the projects managed by the group
     */
    private final int totalProjects;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public Group() {
        this(null, null, null, null, null, null);
    }

    /**
     * Constructor to init a {@link Group} object
     *
     * @param id:          identifier of the group
     * @param name:        name of the group
     * @param description: the description of the group
     * @param members:     the list of the members of the group
     * @param projects:    the list of the projects managed by the group
     */
    // TODO: 19/08/2023 TO REMOVE
    public Group(String id, String name, String description, ArrayList<Member> members, ArrayList<Project> projects) {
        this(id, name, new User("manu0", "Manuel", "Maurizio", null), description, members, projects);
    }

    /**
     * Constructor to init a {@link Group} object
     *
     * @param id:          identifier of the group
     * @param name:        name of the group
     * @param author:      the author of the group
     * @param description: the description of the group
     * @param members:     the list of the members of the group
     * @param projects:    the list of the projects managed by the group
     */
    public Group(String id, String name, User author, String description, ArrayList<Member> members,
                 ArrayList<Project> projects) {
        super(id, name);
        this.author = new User(author.getId(), author.getName(), null, author.getProfilePic(), author.getSurname(),
                author.getEmail(), null, null, null, null, null);
        this.description = description;
        this.members = members;
        totalMembers = members.size();
        this.projects = projects;
        totalProjects = projects.size();
    }

    /**
     * Method to get {@link #author} instance <br>
     * No-any params required
     *
     * @return {@link #author} instance as {@link User}
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Method to get {@link #description} instance <br>
     * No-any params required
     *
     * @return {@link #description} instance as {@link String}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get {@link #members} instance <br>
     * No-any params required
     *
     * @return {@link #members} instance as {@link ArrayList} of {@link Member}
     */
    public ArrayList<Member> getMembers() {
        return members;
    }

    /**
     * Method to get {@link #totalMembers} instance <br>
     * No-any params required
     *
     * @return {@link #totalMembers} instance as int
     */
    public int getTotalMembers() {
        return totalMembers;
    }

    /**
     * Method to get {@link #projects} instance <br>
     * No-any params required
     *
     * @return {@link #projects} instance as {@link ArrayList} of {@link Project}
     */
    public ArrayList<Project> getProjects() {
        return projects;
    }

    /**
     * Method to get {@link #totalProjects} instance <br>
     * No-any params required
     *
     * @return {@link #totalProjects} instance as int
     */
    public int getTotalProjects() {
        return totalProjects;
    }

    /**
     * Method to check if a user is a {@link Role#MAINTAINER} of the group
     *
     * @param user: the user to check the role
     * @return whether the user is a {@link Role#MAINTAINER} as boolean
     */
    public boolean isUserMaintainer(User user) {
        for (Member member : members)
            if (user.getId().equals(member.getId()))
                return member.isMaintainer();
        return false;
    }

    /**
     * Method to check if a user is an {@link Role#ADMIN} of the group
     *
     * @param user: the user to check the role
     * @return whether the user is an {@link Role#ADMIN} as boolean
     */
    public boolean isUserAdmin(User user) {
        for (Member member : members)
            if (user.getId().equals(member.getId()))
                return member.isAdmin();
        return false;
    }

    /**
     * The {@code Member} class is useful to create a <b>Pandoro's group member</b>
     *
     * @author N7ghtm4r3 - Tecknobit
     * @see PandoroItem
     * @see PublicUser
     * @see Serializable
     */
    public static class Member extends PublicUser {

        /**
         * {@code role} the role of the member
         */
        private final Role role;

        /**
         * Constructor to init a {@link Member} object
         *
         * @param id:         identifier of the member
         * @param name:       name of the member
         * @param profilePic: the profile picture of the member
         * @param surname:    the surname of the member
         * @param email:      the email of the member
         * @param role:       the role of the member
         */
        public Member(String id, String name, String profilePic, String surname, String email, Role role) {
            super(id, name, surname, profilePic, email);
            this.role = role;
        }

        /**
         * Method to get {@link #role} instance <br>
         * No-any params required
         *
         * @return {@link #role} instance as {@link Role}
         */
        public Role getRole() {
            return role;
        }

        /**
         * Method to check if the member is a {@link Role#MAINTAINER} <br>
         * No-any params required
         *
         * @return whether the member is a {@link Role#MAINTAINER} as boolean
         */
        public boolean isMaintainer() {
            return isAdmin() || role == Role.MAINTAINER;
        }

        /**
         * Method to check if the member is a {@link Role#ADMIN} <br>
         * No-any params required
         *
         * @return whether the member is a {@link Role#ADMIN} as boolean
         */
        public boolean isAdmin() {
            return role == Role.ADMIN;
        }

        /**
         * Method to check if the user logged in the session is the current member iterated <br>
         *
         * @param userLogged: the user logged in the session
         * @return whether the user logged in the session is the current member iterated as boolean
         */
        public boolean isLoggedUser(User userLogged) {
            return userLogged.getId().equals(id);
        }

    }

}
