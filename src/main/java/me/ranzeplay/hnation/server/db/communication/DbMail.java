package me.ranzeplay.hnation.server.db.communication;

import com.j256.ormlite.field.DatabaseField;
import me.ranzeplay.hnation.server.db.DbPlayer;

import java.sql.Timestamp;
import java.util.UUID;

public class DbMail {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;
    @DatabaseField(canBeNull = false)
    String content;
    @DatabaseField(foreign = true, canBeNull = false)
    DbPlayer sender;
    @DatabaseField(foreign = true, canBeNull = false)
    DbPlayer receiver;
    @DatabaseField(canBeNull = false)
    Timestamp sendTime;

    public DbMail(String content, DbPlayer sender, DbPlayer receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;

        id = UUID.randomUUID();
        sendTime = new Timestamp(System.currentTimeMillis());
    }

    public DbMail() {
    }
}
