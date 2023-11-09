package com.tecknobit.pandoro.records.updates;

import com.tecknobit.pandoro.records.Project;

public class UpdateCompositeKey {

    private String id;

    private String targetVersion;

    private Project project;

    public UpdateCompositeKey() {
    }

    public UpdateCompositeKey(String id, String targetVersion, Project project) {
        this.id = id;
        this.targetVersion = targetVersion;
        this.project = project;
    }

}
