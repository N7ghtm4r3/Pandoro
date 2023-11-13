package com.tecknobit.pandoro.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.apimanager.annotations.Returner;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.pandoro.records.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.tecknobit.pandoro.controllers.ChangelogsController.CHANGELOGS_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.records.users.GroupMember.Role.ADMIN;
import static com.tecknobit.pandoro.services.ChangelogsHelper.*;
import static com.tecknobit.pandoro.services.GroupsHelper.GROUP_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.PROJECT_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.ProjectsHelper.PROJECT_KEY;

/**
 * The {@code Changelog} class is useful to create a <b>Pandoro's changelog</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Serializable
 */
@Entity
@Table(name = CHANGELOGS_KEY)
public class Changelog extends PandoroItemStructure {

    /**
     * {@code ChangelogEvent} list of available event types
     */
    public enum ChangelogEvent {

        /**
         * {@code INVITED_GROUP} type of the changelogEvent when the user have been invited to join in a group
         */
        INVITED_GROUP("Invited into a group"),

        /**
         * {@code JOINED_GROUP} type of the changelogEvent when the user joins in a group
         */
        JOINED_GROUP("Joined in a group"),

        /**
         * {@code ROLE_CHANGED} type of the changelogEvent when the role of the user in a group has been changed
         */
        ROLE_CHANGED("Role changed"),

        /**
         * {@code LEFT_GROUP} type of the changelogEvent when a user left a group
         */
        LEFT_GROUP("Left a group"),

        /**
         * {@code GROUP_DELETED} type of the changelogEvent when a group has been deleted
         */
        GROUP_DELETED("Group deleted"),

        /**
         * {@code PROJECT_CREATED} type of the changelogEvent when a new project of a group has been created
         */
        PROJECT_CREATED("Project created"),

        /**
         * {@code PROJECT_DELETED} type of the changelogEvent when a project of a group has been deleted
         */
        PROJECT_DELETED("Project deleted"),

        /**
         * {@code UPDATE_SCHEDULED} type of the changelogEvent when a new update of project of a group has been scheduled
         */
        UPDATE_SCHEDULED("ProjectUpdate scheduled"),

        /**
         * {@code UPDATE_STARTED} type of the changelogEvent when an update of project of a group has been started
         */
        UPDATE_STARTED("ProjectUpdate started"),

        /**
         * {@code UPDATE_PUBLISHED} type of the changelogEvent when an update of project of a group has been published
         */
        UPDATE_PUBLISHED("ProjectUpdate published"),

        /**
         * {@code UPDATE_DELETED} type of the changelogEvent when an update of project of a group has been deleted
         */
        UPDATE_DELETED("ProjectUpdate deleted");

        /**
         * {@code changelogEvent} type
         */
        private final String event;

        /**
         * Constructor to init a {@link ChangelogEvent} object
         *
         * @param event:{@code changelogEvent} type
         */
        ChangelogEvent(String event) {
            this.event = event;
        }

        /**
         * Method to get {@link #event} instance <br>
         * No-any params required
         *
         * @return {@link #event} instance as {@link String}
         */
        public String getEvent() {
            return event;
        }

    }

    /**
     * {@code id} the identifier of the changelogEvent message
     */
    @Id
    @Column(name = CHANGELOG_IDENTIFIER_KEY)
    private final String id;

    /**
     * {@code changelogEvent} the value of the changelogEvent
     */
    @Column(name = CHANGELOG_EVENT_KEY)
    private final ChangelogEvent changelogEvent;

    /**
     * {@code timestamp} when the changelogEvent has been created
     */
    @Column(name = CHANGELOG_TIMESTAMP_KEY)
    private final long timestamp;

    /**
     * {@code project} the project of the changelogEvent
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = PROJECT_IDENTIFIER_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Project project;

    /**
     * {@code group} the group of the changelogEvent
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = CHANGELOG_GROUP_IDENTIFIER_KEY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Group group;

    /**
     * {@code extraContent} extra content data of the changelogEvent
     */
    @Column(name = CHANGELOG_EXTRA_CONTENT_KEY)
    private final String extraContent;

    /**
     * {@code red} whether the changelog has been red
     */
    @Column(name = CHANGELOG_RED_KEY)
    private final boolean red;

    /**
     * {@code owner} the changelog owner
     *
     * @apiNote usage in SQL scopes
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = CHANGELOG_OWNER_KEY)
    @JsonIgnore
    private User owner;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public Changelog() {
        this(null, null, -1, (Project) null, null, false);
    }

    public Changelog(JSONObject jChangelog) {
        super(jChangelog);
        id = hItem.getString(IDENTIFIER_KEY);
        changelogEvent = ChangelogEvent.valueOf(hItem.getString(CHANGELOG_EVENT_KEY));
        timestamp = hItem.getLong(CHANGELOG_TIMESTAMP_KEY, -1);
        project = Project.getInstance(hItem.getJSONObject(PROJECT_KEY));
        extraContent = hItem.getString(CHANGELOG_EXTRA_CONTENT_KEY);
        red = hItem.getBoolean(CHANGELOG_RED_KEY);
        group = Group.getInstance(hItem.getJSONObject(GROUP_KEY));
    }

    /**
     * Constructor to init a {@link Changelog} object
     *
     * @param id             :           the identifier of the changelogEvent message
     * @param changelogEvent :       the value of the changelogEvent
     * @param timestamp      :    when the changelogEvent has been created
     * @param project        :      the project of the changelogEvent project
     * @param extraContent   : extra content data of the changelogEvent
     * @param red:           whether the changelog has been red
     */
    public Changelog(String id, ChangelogEvent changelogEvent, long timestamp, Project project,
                     String extraContent, boolean red) {
        super(null);
        this.id = id;
        this.changelogEvent = changelogEvent;
        this.timestamp = timestamp;
        this.project = project;
        this.extraContent = extraContent;
        this.red = red;
        group = null;
    }

    /**
     * Constructor to init a {@link Changelog} object
     *
     * @param id             :           the identifier of the changelogEvent message
     * @param changelogEvent :       the value of the changelogEvent
     * @param timestamp      :    when the changelogEvent has been created
     * @param group          :        the group of the changelogEvent
     * @param extraContent   : extra content data of the changelogEvent
     * @param red:           whether the changelog has been red
     */
    public Changelog(String id, ChangelogEvent changelogEvent, long timestamp, Group group,
                     String extraContent, boolean red) {
        super(null);
        this.id = id;
        this.changelogEvent = changelogEvent;
        this.timestamp = timestamp;
        this.group = group;
        this.extraContent = extraContent;
        this.red = red;
        project = null;
    }

    /**
     * Method to get {@link #id} instance <br>
     * No-any params required
     *
     * @return {@link #id} instance as {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get {@link #changelogEvent} instance <br>
     * No-any params required
     *
     * @return {@link #changelogEvent} instance as {@link ChangelogEvent}
     */
    @Enumerated(EnumType.STRING)
    public ChangelogEvent getChangelogEvent() {
        return changelogEvent;
    }

    /**
     * Method to get the title for the changelogEvent message <br>
     * No-any params required
     *
     * @return the title for the changelogEvent message as {@link String}
     */
    public String getTitle() {
        return changelogEvent.getEvent() + " at " + getDate();
    }

    /**
     * Method to get {@link #timestamp} instance <br>
     * No-any params required
     *
     * @return {@link #timestamp} instance as long
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Method to get {@link #timestamp} instance <br>
     * No-any params required
     *
     * @return {@link #timestamp} instance as {@link String}
     */
    public String getDate() {
        return TimeFormatter.getStringDate(timestamp);
    }

    /**
     * Method to get the content message<br>
     * No-any params required
     *
     * @return the content message in base of the {@link #changelogEvent} type as {@link String}
     */
    @JsonIgnore
    public String getContent() {
        String entityName;
        if (group != null)
            entityName = group.getName();
        else if (project != null)
            entityName = project.getName();
        else
            entityName = null;
        return switch (changelogEvent) {
            case INVITED_GROUP -> "You have been invited to join in the " + entityName + " group";
            case JOINED_GROUP -> "You joined in the " + entityName + " group";
            case ROLE_CHANGED -> {
                String article = "a";
                if (extraContent.equals(ADMIN.toString()))
                    article = "an";
                yield "You became " + article + " " + extraContent + " in the " + entityName + " group";
            }
            case LEFT_GROUP -> "You left from the " + entityName + " group";
            case GROUP_DELETED -> "The " + entityName + " group has been deleted";
            case PROJECT_CREATED -> "The project " + entityName + " has been created";
            case PROJECT_DELETED -> "The project " + entityName + " has been deleted";
            case UPDATE_SCHEDULED ->
                    "A new update for " + entityName + "'s project has been scheduled [v. " + extraContent + "]";
            case UPDATE_STARTED ->
                    "The [v. " + extraContent + "] update of " + entityName + "'s project has been started";
            case UPDATE_PUBLISHED ->
                    "The [v. " + extraContent + "] update of " + entityName + "'s project has been published";
            case UPDATE_DELETED ->
                    "The [v. " + extraContent + "] update of " + entityName + "'s project has been deleted";
        };
    }

    /**
     * Method to get {@link #project} instance <br>
     * No-any params required
     *
     * @return {@link #project} instance as {@link Project}
     */
    public Project getProject() {
        return project;
    }

    /**
     * Method to get {@link #group} instance <br>
     * No-any params required
     *
     * @return {@link #group} instance as {@link Group}
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Method to get {@link #extraContent} instance <br>
     * No-any params required
     *
     * @return {@link #extraContent} instance as {@link String}
     */
    public String getExtraContent() {
        return extraContent;
    }

    /**
     * Method to get {@link #red} instance <br>
     * No-any params required
     *
     * @return {@link #red} instance as boolean
     */
    public boolean isRed() {
        return red;
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItems: items details as {@link JSONArray}
     * @return instance as {@link ArrayList} of {@link Changelog}
     */
    @Returner
    public static ArrayList<Changelog> getInstances(JSONArray jItems) {
        ArrayList<Changelog> notes = new ArrayList<>();
        if (jItems != null) {
            for (int j = 0; j < jItems.length(); j++)
                notes.add(new Changelog(jItems.getJSONObject(j)));
        }
        return notes;
    }

    /**
     * Method to get an instance of this Telegram's type
     *
     * @param jItem: item details as {@link JSONObject}
     * @return instance as {@link Changelog}
     */
    @Returner
    public static Changelog getInstance(JSONObject jItem) {
        if (jItem == null)
            return null;
        else
            return new Changelog(jItem);
    }

}
