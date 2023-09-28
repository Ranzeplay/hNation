package me.ranzeplay.hnation.features.communication.announcement.db;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.features.player.db.DbPlayer;

import java.sql.Timestamp;
import java.util.UUID;

@DatabaseTable(tableName = "announcement")
public class DbAnnouncement {
    @DatabaseField(canBeNull = false, id = true)
    UUID id;

    @DatabaseField(canBeNull = false)
    String title;
    @DatabaseField(canBeNull = false)
    String content;

    @DatabaseField(canBeNull = false)
    UUID announcerId;
    @DatabaseField(canBeNull = false)
    Timestamp createTime;

    public DbAnnouncement() {
    }

    public DbAnnouncement(String title, String content, DbPlayer announcer) {
        this.title = title;
        this.content = content;
        this.announcerId = announcer.getId();

        id = UUID.randomUUID();
        createTime = new Timestamp(System.currentTimeMillis());
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public DbPlayer getAnnouncer() {
        return PlayerManager.getInstance().getPlayer(announcerId);
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
}
