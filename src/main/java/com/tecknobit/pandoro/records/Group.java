package com.tecknobit.pandoro.records;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;

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
     * Constructor to init a {@link Group} object
     *
     * @param id:          identifier of the group
     * @param name:        name of the group
     * @param description: the description of the group
     */
    // TODO: 19/08/2023 TO REMOVE
    public Group(String id, String name, String description) {
        this(id, name, new User("Manuel", "Maurizio"), description,
                new ArrayList<>(List.of(
                        new Member("manu0", "Manuel", "Maurizio", Role.ADMIN),
                        new Member("Gabriele", "Marengo", Role.MAINTAINER))
                ),
                new ArrayList<>(of(
                        new Project(
                                "id0",
                                "One",
                                new User("Manuel", "Maurizio"),
                                "First project",
                                "Lorem ipsum dolor sit amet. Ab veniam enim et aperiam perferendis aut veritatis corporis sit modi aperiam vel aperiam voluptate! Et voluptatem quia aut minus culpa aut nihil optio quo nihil illum ea velit voluptatibus. Et reiciendis voluptatibus ea fuga distinctio eum tenetur recusandae et rerum natus et quia aliquam. Id sint quod eum eligendi rerum sed porro asperiores. </p><p>Ut consequatur illum qui quia omnis et quam adipisci aut autem natus aut soluta sunt ut error veritatis. Et magni labore qui optio dolorem vel totam consequatur est quas iste. Est incidunt omnis sed odit unde nam fugit quia At quam unde sit veritatis asperiores et optio reiciendis. Qui earum eaque sit veritatis voluptate eos galisum harum est alias quia ut sint asperiores vel accusantium libero ea optio ipsam. </p><p>Nam dolor temporibus a molestias maiores est libero rerum et ullam repudiandae? Et repellendus earum a fuga magni est doloribus quia ad libero dicta a aperiam impedit. Ut quos odio a quisquam natus est dolores natus aut debitis cupiditate aut sunt corrupti. Sit quia exercitationem nam quos harum sit veniam accusamus eum corrupti rerum qui voluptas dolor sit officiis modi sit eius quia",
                                "1.0.0",
                                new ArrayList<>(List.of(
                                        new Group("id1",
                                                "Tecknobit",
                                                new User("Manuel", "Maurizio"),
                                                "Lorem ipsum dolor sit amet. Ab veniam enim et aperiam perferendis aut veritatis corporis sit modi aperiam vel aperiam voluptate! Et voluptatem quia aut minus culpa aut nihil optio quo nihil illum ea velit voluptatibus. Et reiciendis voluptatibus ea fuga distinctio eum tenetur recusandae et rerum natus et quia aliquam. Id sint quod eum eligendi rerum sed porro asperiores. </p><p>Ut consequatur illum qui quia omnis et quam adipisci aut autem natus aut soluta sunt ut error veritatis. Et magni labore qui optio dolorem vel totam consequatur est quas iste. Est incidunt omnis sed odit unde nam fugit quia At quam unde sit veritatis asperiores et optio reiciendis. Qui earum eaque sit veritatis voluptate eos galisum harum est alias quia ut sint asperiores vel accusantium libero ea optio ipsam. </p><p>Nam dolor temporibus a molestias maiores est libero rerum et ullam repudiandae? Et repellendus earum a fuga magni est doloribus quia ad libero dicta a aperiam impedit. Ut quos odio a quisquam natus est dolores natus aut debitis cupiditate aut sunt corrupti. Sit quia exercitationem nam qu" +
                                                        "os harum sit veniam accusamus eum corrupti rerum qui voluptas dolor sit officiis modi sit " +
                                                        "eius quia",
                                                new ArrayList<>(List.of(
                                                        new Member("manu0", "Manuel", "Maurizio", Role.ADMIN),
                                                        new Member("Gabriele", "Marengo", Role.MAINTAINER))
                                                ),
                                                new ArrayList<>()
                                        )
                                )),
                                new ArrayList<>(List.of(
                                        new Update(
                                                "gaga",
                                                new User("Manuel", "Maurizio"),
                                                System.currentTimeMillis(),
                                                "1.0.2",
                                                new ArrayList<>(List.of(new Note(
                                                                "gagaga111",
                                                                new User("Gabriele", "Marengo"),
                                                                "Fixed",
                                                                System.currentTimeMillis()),
                                                        new Note(
                                                                "gagaga1112",
                                                                "Remove console property windows [DK]",
                                                                System.currentTimeMillis()),
                                                        new Note(
                                                                "gagaga2222",
                                                                "Fixed24",
                                                                System.currentTimeMillis())))
                                        ),
                                        new Update(
                                                "gagagaga",
                                                new User("Manuel", "Maurizio"),
                                                System.currentTimeMillis(),
                                                "1.0.1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                new ArrayList<>(List.of(new Note(
                                                                "gagaga",
                                                                "Fixed",
                                                                System.currentTimeMillis()),
                                                        new Note(
                                                                "gagaga1",
                                                                "Remove console property windows [DK]",
                                                                System.currentTimeMillis()),
                                                        new Note(
                                                                "gagaga2",
                                                                "Fixed2",
                                                                System.currentTimeMillis())))
                                        ),
                                        new Update(
                                                "gagagaga1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                "1.0.0",
                                                new User("Gabriele", "Marengo"),
                                                1689854400000L,
                                                new User("Manuel", "Maurizio"),
                                                1690286400000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("Manuel", "Maurizio"),
                                                        "Fixed",
                                                        1691852915000L,
                                                        true,
                                                        new User("Gabriele", "Marengo"),
                                                        System.currentTimeMillis()
                                                ), new Note(
                                                        "gagaga2",
                                                        "Fixed2",
                                                        1691852915000L), new Note(
                                                        "gagaga3",
                                                        "Fixed3",
                                                        1691852915000L)))
                                        ),
                                        new Update(
                                                "gagagaga1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                "1.0.0",
                                                new User("Gabriele", "Marengo"),
                                                1690286400000L,
                                                new User("Manuel", "Maurizio"),
                                                1690459200000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("Manuel", "Maurizio"),
                                                        "Fixed",
                                                        1691852915000L,
                                                        true,
                                                        new User("Gabriele", "Marengo"),
                                                        System.currentTimeMillis()
                                                ), new Note(
                                                        "gagaga2",
                                                        "Fixed2",
                                                        1691852915000L), new Note(
                                                        "gagaga3",
                                                        "Fixed3",
                                                        1691852915000L)))
                                        ),
                                        new Update(
                                                "gagagaga1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                "1.0.0",
                                                new User("Gabriele", "Marengo"),
                                                1690891200000L,
                                                new User("Manuel", "Maurizio"),
                                                1691150400000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("Manuel", "Maurizio"),
                                                        "Fixed",
                                                        1691852915000L,
                                                        true,
                                                        new User("Gabriele", "Marengo"),
                                                        System.currentTimeMillis()
                                                ), new Note(
                                                        "gagaga2",
                                                        "Fixed2",
                                                        1691852915000L), new Note(
                                                        "gagaga3",
                                                        "Fixed3",
                                                        1691852915000L)))
                                        ),
                                        new Update(
                                                "gagagaga2",
                                                System.currentTimeMillis(),
                                                "0.0.0",
                                                1691323200000L,
                                                1691841600000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "gagaga2",
                                                        "Fixed",
                                                        1691852915000L)))
                                        ))),
                                "https://github.com/N7ghtm4r3/Pandoro-Desktop"
                        ))));
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
        this(id, name, new User("manu0", "Manuel", "Maurizio"), description, members, projects);
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
        this.author = author;
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
     * @see User
     * @see Serializable
     */
    public static class Member extends User {

        /**
         * {@code role} the role of the member
         */
        private final Role role;

        /**
         * Constructor to init a {@link Member} object
         *
         * @param id:      identifier of the member
         * @param name:    name of the member
         * @param surname: surname of the member
         * @param role:    the role of the member
         */
        public Member(String id, String name, String surname, Role role) {
            super(id, name, surname);
            this.role = role;
        }

        /**
         * Constructor to init a {@link Member} object
         *
         * @param name:    name of the member
         * @param surname: surname of the member
         * @param role:    the role of the member
         */
        public Member(String name, String surname, Role role) {
            super(name, surname);
            this.role = role;
        }

        /**
         * Constructor to init a {@link Member} object
         *
         * @param id:         identifier of the member
         * @param name:       name of the member
         * @param profilePic: the profile picture of the member
         * @param surname:    the surname of the member
         * @param email:      the email of the member
         * @param password:   the password of the member
         * @param changelogs: list of action messages for the member
         * @param groups:     list of the groups of the member
         * @param projects:   list of the projects of the member
         * @param role:       the role of the member
         */
        public Member(String id, String name, String profilePic, String surname, String email, String password,
                      ArrayList<Group> groups, ArrayList<Changelog> changelogs, ArrayList<Project> projects,
                      Role role) {
            super(id, name, profilePic, surname, email, password, changelogs, groups, projects, null);
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
        // TODO: 25/10/2023 REPLACE user.id != member.id WITH THIS METHOD
        public boolean isLoggedUser(User userLogged) {
            return userLogged.getId().equals(id);
        }

    }

}
