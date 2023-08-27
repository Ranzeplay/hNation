package me.ranzeplay.hnation.server.db.communication;

import com.j256.ormlite.field.DatabaseField;
import me.ranzeplay.hnation.server.db.DbPlayer;

import java.sql.Timestamp;
import java.util.UUID;

public abstract class ChatMessageBase {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;
    @DatabaseField(canBeNull = false)
    String message;
    @DatabaseField(foreign = true, canBeNull = false)
    DbPlayer sender;
    @DatabaseField(canBeNull = false)
    Timestamp time;

    public ChatMessageBase(String message, DbPlayer sender) {
        this.message = message;
        this.sender = sender;
    }

    public ChatMessageBase() {
    }
}
