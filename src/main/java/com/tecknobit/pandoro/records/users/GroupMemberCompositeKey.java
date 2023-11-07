package com.tecknobit.pandoro.records.users;

public class GroupMemberCompositeKey {

    private String id;

    private String group_member;

    public GroupMemberCompositeKey() {
    }

    public GroupMemberCompositeKey(String id, String group_member) {
        this.id = id;
        this.group_member = group_member;
    }

}
