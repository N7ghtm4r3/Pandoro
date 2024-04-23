package com.tecknobit.pandorocore.records.users;

/**
 * The {@code GroupMemberCompositeKey} is useful for the {@link GroupMember} class to specify its ids
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class GroupMemberCompositeKey {

    /**
     * {@code id} the identifier of the member
     */
    private String id;

    /**
     * {@code group_member} the identifier of the group
     */
    private String group_member;

    /**
     * Default constructor
     *
     * @apiNote empty constructor required
     */
    public GroupMemberCompositeKey() {
    }

    /**
     * Constructor to init a {@link GroupMemberCompositeKey} object
     *
     * @param id:           the identifier of the member
     * @param group_member:{@code group_member} the identifier of the group
     */
    public GroupMemberCompositeKey(String id, String group_member) {
        this.id = id;
        this.group_member = group_member;
    }

}
