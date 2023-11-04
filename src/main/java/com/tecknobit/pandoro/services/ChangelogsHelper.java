package com.tecknobit.pandoro.services;

import com.tecknobit.pandoro.records.Changelog;
import com.tecknobit.pandoro.records.Changelog.ChangelogEvent;
import com.tecknobit.pandoro.services.repositories.ChangelogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangelogsHelper {

    public static final String CHANGELOG_IDENTIFIER_KEY = "changelog_id";

    public static final String CHANGELOG_EVENT_KEY = "changelog_event";

    public static final String CHANGELOG_TIMESTAMP_KEY = "timestamp";

    public static final String CHANGELOG_PROJECT_IDENTIFIER_KEY = "project_id";

    public static final String CHANGELOG_GROUP_IDENTIFIER_KEY = "group_id";

    public static final String CHANGELOG_EXTRA_CONTENT_KEY = "extra_content";

    public static final String CHANGELOG_RED_KEY = "red";

    public static final String CHANGELOG_OWNER_KEY = "owner";

    @Autowired
    private ChangelogsRepository changelogsRepository;

    public List<Changelog> getChangelogs(String userId) {
        return changelogsRepository.getChangelogs(userId);
    }

    public boolean changelogExists(String changelogId) {
        return changelogsRepository.findById(changelogId).isPresent();
    }

    public void markAsRed(String changelogId, String userId) {
        changelogsRepository.markAsRed(userId, changelogId);
    }

    // TODO: 04/11/2023 IMPLEMENT CHANGELOG CREATOR
    //  INSERT INTO changelogs (changelog_id,changelog_event,extra_content,group_id,project_id,red,timestamp,owner)
    //  VALUES (1231,"LEFT_GROUP","gagga",NULL,NULL,true,1699102591,"f1fdab65e1494050ad5b29af5966f972")
    public void createChangelog(String userId, ChangelogEvent event) {

    }

}
