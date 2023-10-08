package me.ranzeplay.hnation.features.communication.mail.db;

import com.j256.ormlite.field.DatabaseField;
import me.ranzeplay.hnation.features.player.db.DbPlayer;

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

    @DatabaseField
    MailStatus status;

    public DbMail(String content, DbPlayer sender, DbPlayer receiver, MailStatus status) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;

        id = UUID.randomUUID();
        sendTime = new Timestamp(System.currentTimeMillis());
    }

    public DbMail() {
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public DbPlayer getSender() {
        return sender;
    }

    public DbPlayer getReceiver() {
        return receiver;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public MailStatus getStatus() {
        return status;
    }

    public void setStatus(MailStatus status) {
        this.status = status;
    }
}
