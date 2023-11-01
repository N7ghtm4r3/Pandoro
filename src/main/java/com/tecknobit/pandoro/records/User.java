package com.tecknobit.pandoro.records;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.pandoro.services.UsersHelper.*;
import static java.util.List.of;

/**
 * The {@code User} class is useful to create a <b>Pandoro's user</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItem
 * @see Serializable
 */
@Entity
@Table(name = USERS_TABLE)
public class User extends PandoroItem {

    /**
     * {@code USER_NAME_MAX_LENGTH} the max length of the name for a user
     */
    public static final int USER_NAME_MAX_LENGTH = 20;

    /**
     * {@code USER_SURNAME_MAX_LENGTH} the max length of the surname for a user
     */
    public static final int USER_SURNAME_MAX_LENGTH = 30;

    /**
     * {@code EMAIL_MAX_LENGTH} the max length of the email for a user
     */
    public static final int EMAIL_MAX_LENGTH = 50;

    /**
     * {@code PASSWORD_MIN_LENGTH} the min length of the password for a user
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * {@code PASSWORD_MAX_LENGTH} the max length of the password for a user
     */
    public static final int PASSWORD_MAX_LENGTH = 32;

    /**
     * {@code token} token of the user
     */
    @Column(name = TOKEN_KEY, unique = true)
    private final String token;

    /**
     * {@code surname} the surname of the user
     */
    @Column(name = SURNAME_KEY)
    private final String surname;

    /**
     * {@code profilePic} the profile picture of the user
     */
    @Column(
            name = PROFILE_PIC_KEY,
            columnDefinition = "text default " + DEFAULT_PROFILE_PIC
    )
    private final String profilePic;

    /**
     * {@code email} the email of the user
     */
    @Column(name = EMAIL_KEY, unique = true)
    private final String email;

    /**
     * {@code password} the password of the user
     */
    @Column(name = PASSWORD_KEY)
    private final String password;

    /**
     * {@code changelogs} list of action messages for the user
     */
    @Column(name = CHANGELOGS_KEY)
    private final ArrayList<Changelog> changelogs;

    /**
     * {@code groups} list of the groups of the user
     */
    @Column(name = GROUPS_KEY)
    private final ArrayList<Group> groups;

    /**
     * {@code projects} list of the projects of the user
     */
    @Column(name = PROJECTS_KEY)
    private final ArrayList<Project> projects;

    /**
     * {@code notes} list of the notes of the user
     */
    @Column(name = NOTES_KEY)
    private final ArrayList<Note> notes;

    /**
     * Constructor to init a {@link User} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param token:{@code token} token of the user
     * @param surname : the surname of the user
     * @param email   :   the email of the user
     * @param password   :   the password of the user
     *
     */
    public User(String id, String name, String token, String surname, String email, String password) {
        this(id, name, token, null, surname, email, password, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param surname : the surname of the user
     * @param token:token of the user
     */
    // TODO: 19/08/2023 TO REMOVE
    public User(String id, String name, String surname, String token) {
        this(id, name, token, "", surname, "maurizio.manuel2003@gmail.com", "pass", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param name:    name of the user
     * @param surname:{@code surname} the surname of the user
     */
    // TODO: 19/08/2023 TO REMOVE
    public User(String name, String surname) {
        this("", name, "token", "", surname, "maurizio.manuel2003@gmail.com", "pass", new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor to init a {@link User} object
     */
    // TODO: 19/08/2023 TO REMOVE
    public User() {
        this("manu0", "Manuel",
                "token", "https://www.oecd.org/media/oecdorg/directorates/directorateforsciencetechnologyandindustry/stp/space_astronaut_iStock-1353874144.jpg",
                "Maurizio", "maurizio.manuel2003@gmail.com", "pass",
                new ArrayList<>(of(
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.UPDATE_SCHEDULED,
                                System.currentTimeMillis(),
                                new Project("Prova"),
                                "1.0.2",
                                false),
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.UPDATE_STARTED,
                                System.currentTimeMillis(),
                                new Project("Prova"),
                                "1.0.2",
                                true),
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.UPDATE_PUBLISHED,
                                System.currentTimeMillis(),
                                new Project("Prova"),
                                "1.0.2",
                                false),
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.UPDATE_DELETED,
                                System.currentTimeMillis(),
                                new Project("Prova"),
                                "1.0.2",
                                false),
                        /* GROUP
                         */
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.JOINED_GROUP,
                                System.currentTimeMillis(),
                                new Group(
                                        "",
                                        "TecknobitJOIN",
                                        "ciao",
                                        new ArrayList<>(List.of(
                                                new Group.Member("manu0", "Manuel", "Maurizio", Group.Role.ADMIN),
                                                new Group.Member("Gabriele", "Marengo", Group.Role.MAINTAINER))
                                        ),
                                        new ArrayList<>()
                                ), false),
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.ROLE_CHANGED,
                                System.currentTimeMillis(),
                                new Group(
                                        "",
                                        "TecknobitJOIN",
                                        "ciao",
                                        new ArrayList<>(List.of(
                                                new Group.Member("manu0", "Manuel", "Maurizio", Group.Role.ADMIN),
                                                new Group.Member("Gabriele", "Marengo", Group.Role.MAINTAINER))
                                        ),
                                        new ArrayList<>()
                                ),
                                Group.Role.ADMIN.toString(),
                                true),
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.LEFT_GROUP,
                                System.currentTimeMillis(),
                                new Group(
                                        "",
                                        "TecknobitJOIN",
                                        "ciao",
                                        new ArrayList<>(List.of(
                                                new Group.Member("manu0", "Manuel", "Maurizio", Group.Role.ADMIN),
                                                new Group.Member("Gabriele", "Marengo", Group.Role.MAINTAINER))
                                        ),
                                        new ArrayList<>()
                                ), true),
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.GROUP_DELETED,
                                System.currentTimeMillis(),
                                new Group(
                                        "",
                                        "TecknobitJOIN",
                                        "ciao",
                                        new ArrayList<>(List.of(
                                                new Group.Member("manu0", "Manuel", "Maurizio", Group.Role.ADMIN),
                                                new Group.Member("Gabriele", "Marengo", Group.Role.MAINTAINER))
                                        ),
                                        new ArrayList<>()
                                ), true),
                        /*
                        PROJECT */
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.PROJECT_CREATED,
                                System.currentTimeMillis(),
                                new Project("Prova"),
                                true),
                        new Changelog(
                                "",
                                Changelog.ChangelogEvent.PROJECT_DELETED,
                                System.currentTimeMillis(),
                                new Project("Prova1"),
                                false)
                )),
                new ArrayList<>(of(
                        new Group(
                                "",
                                "Tecknobit",
                                "gagagagagagagwqgweagwegeggegrg",
                                new ArrayList<>(List.of(
                                        new Group.Member("manu0", "Manuel", "Maurizio", Group.Role.ADMIN),
                                        new Group.Member("Gabriele", "Marengo", Group.Role.MAINTAINER),
                                        new Group.Member("Gabriele", "Marengo", Group.Role.MAINTAINER))
                                ),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Tecknobit1",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito1",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito1",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito1",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito1",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito1",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito1",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ),
                        new Group(
                                "",
                                "Unito2",
                                "ciao", new ArrayList<>(),
                                new ArrayList<>()
                        ))),
                new ArrayList<>(of(
                        new Project(
                                "id0",
                                "One",
                                new User("manu0", "Manuel", "Maurizio", "token"),
                                "First project",
                                "Lorem ipsum dolor sit amet. Ab veniam enim et aperiam perferendis aut veritatis corporis sit modi aperiam vel aperiam voluptate! Et voluptatem quia aut minus culpa aut nihil optio quo nihil illum ea velit voluptatibus. Et reiciendis voluptatibus ea fuga distinctio eum tenetur recusandae et rerum natus et quia aliquam. Id sint quod eum eligendi rerum sed porro asperiores. </p><p>Ut consequatur illum qui quia omnis et quam adipisci aut autem natus aut soluta sunt ut error veritatis. Et magni labore qui optio dolorem vel totam consequatur est quas iste. Est incidunt omnis sed odit unde nam fugit quia At quam unde sit veritatis asperiores et optio reiciendis. Qui earum eaque sit veritatis voluptate eos galisum harum est alias quia ut sint asperiores vel accusantium libero ea optio ipsam. </p><p>Nam dolor temporibus a molestias maiores est libero rerum et ullam repudiandae? Et repellendus earum a fuga magni est doloribus quia ad libero dicta a aperiam impedit. Ut quos odio a quisquam natus est dolores natus aut debitis cupiditate aut sunt corrupti. Sit quia exercitationem nam quos harum sit veniam accusamus eum corrupti rerum qui voluptas dolor sit officiis modi sit eius quia AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                ,
                                "1.0.0",
                                new ArrayList<>(List.of(
                                        new Group("id1", "Tecknobit", "Lorem ipsum dolor sit amet. Ab veniam enim et aperiam perferendis aut veritatis corporis sit modi aperiam vel aperiam voluptate! Et voluptatem quia aut minus culpa aut nihil optio quo nihil illum ea velit voluptatibus. Et reiciendis voluptatibus ea fuga distinctio eum tenetur recusandae et rerum natus et quia aliquam. Id sint quod eum eligendi rerum sed porro asperiores. </p><p>Ut consequatur illum qui quia omnis et quam adipisci aut autem natus aut soluta sunt ut error veritatis. Et magni labore qui optio dolorem vel totam consequatur est quas iste. Est incidunt omnis sed odit unde nam fugit quia At quam unde sit veritatis asperiores et optio reiciendis. Qui earum eaque sit veritatis voluptate eos galisum harum est alias quia ut sint asperiores vel accusantium libero ea optio ipsam. </p><p>Nam dolor temporibus a molestias maiores est libero rerum et ullam repudiandae? Et repellendus earum a fuga magni est doloribus quia ad libero dicta a aperiam impedit. Ut quos odio a quisquam natus est dolores natus aut debitis cupiditate aut sunt corrupti. Sit quia exercitationem nam quos harum sit veniam accusamus eum corrupti rerum qui voluptas dolor sit officiis modi sit eius quia"),
                                        new Group("id11", "Prova1", "ciao"),
                                        new Group("id2", "Prova2", "ciao"),
                                        new Group("id3", "Prova3", "ciao")
                                )),
                                new ArrayList<>(List.of(
                                        new Update(
                                                "gaga",
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                System.currentTimeMillis(),
                                                "1.0.2",
                                                new ArrayList<>(List.of(new Note(
                                                                "gagaga111",
                                                                new User("Gabriele", "Marengo"),
                                                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                        "when an unknown printer took a galley of type and",
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                System.currentTimeMillis(),
                                                "1.0.1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                new ArrayList<>(List.of(new Note(
                                                                "gagaga",
                                                                new User("Gabriele", "Marengo"),
                                                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                        "when an unknown printer took a galley of type and",
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1690286400000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
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
                                                new User("manu10", "Manuel", "Maurizio", "token"),
                                                1694008593000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1691150400000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
                                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                "when an unknown printer took a galley of type and",
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
                        ),
                        new Project(
                                "id1",
                                "Pandorogag",
                                "Pandorogaga4444",
                                "The second project",
                                "1.0.1"
                        ),
                        new Project(
                                "id2",
                                "Third",
                                "Third project",
                                "The third project",
                                "1.0.2"
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourth project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id0",
                                "One",
                                "First project",
                                "The first project",
                                "1.0.0"
                        ),
                        new Project(
                                "id1",
                                "Second",
                                "project2",
                                "The second project",
                                "1.0.1",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"))),
                                new ArrayList<>(List.of(
                                        new Update(
                                                "gagagaga",
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                System.currentTimeMillis(),
                                                "1.0.1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                new ArrayList<>(List.of(new Note(
                                                                "gagaga",
                                                                new User("Gabriele", "Marengo"),
                                                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                        "when an unknown printer took a galley of type and",
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
                                                1690286400000L,
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1690459200000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1691150400000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
                                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                "when an unknown printer took a galley of type and",
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
                                                "gaga",
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                System.currentTimeMillis(),
                                                "1.0.2",
                                                new ArrayList<>(List.of(new Note(
                                                                "gagaga111",
                                                                new User("Gabriele", "Marengo"),
                                                                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                        "when an unknown printer took a galley of type and",
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
                                ""
                        ),
                        new Project(
                                "id2",
                                "Third",
                                "project2",
                                "The third project",
                                "1.0.2",
                                new ArrayList<>(List.of(
                                        new Update(
                                                "gagagaga1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                "1.0.0",
                                                new User("Gabriele", "Marengo"),
                                                1689854400000L,
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1690286400000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1690459200000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1691409600000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
                                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                "when an unknown printer took a galley of type and",
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
                                                1691512380000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "gagaga2",
                                                        "Fixed",
                                                        1691852915000L)))
                                        ),
                                        new Update(
                                                "gagagaga2",
                                                System.currentTimeMillis(),
                                                "0.0.0",
                                                1691323200000L,
                                                1691512380000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "gagaga2",
                                                        "Fixed",
                                                        1691852915000L)))
                                        ),
                                        new Update(
                                                "gagagaga2",
                                                System.currentTimeMillis(),
                                                "0.0.0",
                                                1691323200000L,
                                                1697976000000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "gagaga2",
                                                        "Fixed",
                                                        1691852915000L)))
                                        )))
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(List.of(
                                        new Update(
                                                "gagagaga1",
                                                new User("Gabriele", "Marengo"),
                                                System.currentTimeMillis(),
                                                "1.0.0",
                                                new User("Gabriele", "Marengo"),
                                                1689854400000L,
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1690286400000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1690459200000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1691409600000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
                                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                "when an unknown printer took a galley of type and",
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1691409600000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
                                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                "when an unknown printer took a galley of type and",
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
                                                new User("manu0", "Manuel", "Maurizio", "token"),
                                                1691409600000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "e484081840f511eebe560242ac120002",
                                                        new User("manu0", "Manuel", "Maurizio", "token"),
                                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                                "when an unknown printer took a galley of type and",
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
                                                1691512380000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "gagaga2",
                                                        "Fixed",
                                                        1691852915000L)))
                                        ),
                                        new Update(
                                                "gagagaga2",
                                                System.currentTimeMillis(),
                                                "0.0.0",
                                                1691323200000L,
                                                1691512380000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "gagaga2",
                                                        "Fixed",
                                                        1691852915000L)))
                                        ),
                                        new Update(
                                                "gagagaga2",
                                                System.currentTimeMillis(),
                                                "0.0.0",
                                                1691323200000L,
                                                1692705600000L,
                                                new ArrayList<>(List.of(new Note(
                                                        "gagaga2",
                                                        "Fixed",
                                                        1691852915000L)))
                                        ))),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                new User("manu0", "Manuel", "Maurizio", "token"),
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                "https://gitlab.com/eggwg/wegwg/-/learn_gitlab"
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ),
                        new Project(
                                "id19",
                                "Fourth",
                                "Fourt31 project",
                                "The fourth project",
                                "1.0.3",
                                new ArrayList<>(of(new Group("", "Tecknobit", "ciao"), new Group("", "Unito", "ciao"))),
                                new ArrayList<>(),
                                ""
                        ))),
                new ArrayList<>(
                        List.of(
                                new Note(
                                        "g",
                                        "ciao",
                                        System.currentTimeMillis()
                                ),
                                new Note(
                                        "g",
                                        "ciao",
                                        System.currentTimeMillis()
                                ),
                                new Note(
                                        "g",
                                        "ciao",
                                        System.currentTimeMillis()
                                ),
                                new Note(
                                        "g",
                                        "ciao",
                                        System.currentTimeMillis()
                                ),
                                new Note(
                                        "g",
                                        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo " +
                                                "ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis " +
                                                "parturient montes, nascetur ridiculus mus. Donec qu",
                                        System.currentTimeMillis(),
                                        true,
                                        System.currentTimeMillis() + 1000
                                ),
                                new Note(
                                        "g",
                                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                                                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                                                "when an unknown printer took a galley of type and",
                                        System.currentTimeMillis()
                                )
                        )
                )
        );
    }

    /**
     * Constructor to init a {@link User} object
     *
     * @param id         :         identifier of the user
     * @param name       :       name of the user
     * @param token:{@code token} token of the user
     * @param profilePic : the profile picture of the user
     * @param surname    :    the surname of the user
     * @param email      :      the email of the user
     * @param password   :   the password of the user
     * @param changelogs : list of action messages for the user
     * @param groups     :     list of the groups of the user
     * @param projects   :   list of the projects of the user
     * @param notes      :      list of the notes of the user
     */
    public User(String id, String name, String token, String profilePic, String surname, String email, String password,
                ArrayList<Changelog> changelogs, ArrayList<Group> groups, ArrayList<Project> projects,
                ArrayList<Note> notes) {
        super(id, name);
        this.token = token;
        this.profilePic = profilePic;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.groups = groups;
        this.changelogs = changelogs;
        this.projects = projects;
        this.notes = notes;
    }

    /**
     * Method to get {@link #token} instance <br>
     * No-any params required
     *
     * @return {@link #token} instance as {@link String}
     */
    public String getToken() {
        return token;
    }

    /**
     * Method to get {@link #profilePic} instance <br>
     * No-any params required
     *
     * @return {@link #profilePic} instance as {@link String}
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Method to get {@link #surname} instance <br>
     * No-any params required
     *
     * @return {@link #surname} instance as {@link String}
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Method to get the complete name of the user <br>
     * No-any params required
     *
     * @return complete name of the user as {@link String}
     * @apiNote {@link #name} (and) {@link #surname}
     */
    public String getCompleteName() {
        return name + " " + surname;
    }

    /**
     * Method to get {@link #email} instance <br>
     * No-any params required
     *
     * @return {@link #email} instance as {@link String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to get {@link #password} instance <br>
     * No-any params required
     *
     * @return {@link #password} instance as {@link String}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to get {@link #groups} instance <br>
     * No-any params required
     *
     * @return {@link #groups} instance as {@link ArrayList} of {@link Group}
     */
    public ArrayList<Group> getGroups() {
        return groups;
    }

    /**
     * Method to get {@link #changelogs} instance <br>
     * No-any params required
     *
     * @return {@link #changelogs} instance as {@link ArrayList} of {@link Changelog}
     */
    public ArrayList<Changelog> getChangelogs() {
        return changelogs;
    }

    /**
     * Method to get the number of {@link #changelogs} unread <br>
     * No-any params required
     *
     * @return the number of {@link #changelogs} unread as int
     */
    public int getUnreadChangelogsNumber() {
        int unread = 0;
        for (Changelog changelog : changelogs)
            if (!changelog.isRed())
                unread++;
        return unread;
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
     * Method to get {@link #notes} instance <br>
     * No-any params required
     *
     * @return {@link #notes} instance as {@link ArrayList} of {@link Note}
     */
    public ArrayList<Note> getNotes() {
        return notes;
    }

    /**
     * Method to get the groups where the user is the {@link Group.Role#ADMIN} <br>
     * No-any params required
     *
     * @return groups as {@link ArrayList} of {@link Group}
     */
    public ArrayList<Group> getAdminGroups() {
        ArrayList<Group> subGroups = new ArrayList<>();
        for (Group group : groups)
            if (group.isUserAdmin(this))
                subGroups.add(group);
        return subGroups;
    }

}

