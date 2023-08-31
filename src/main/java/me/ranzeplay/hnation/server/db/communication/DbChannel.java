package me.ranzeplay.hnation.server.db.communication;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import me.ranzeplay.hnation.server.db.DbPlayer;

import java.sql.Timestamp;
import java.util.UUID;

@DatabaseTable(tableName = "channel")
public class DbChannel {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;
    @DatabaseField(canBeNull = false)
    String name;
    // @ForeignCollectionField
    // ForeignCollection<DbPlayer> members;
    @ForeignCollectionField
    ForeignCollection<DbChannelMessage> messages;
    @DatabaseField(canBeNull = false)
    Timestamp createTime;

    @DatabaseField(canBeNull = false, foreign = true)
    DbPlayer owner;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public ForeignCollection<DbPlayer> getMembers() {
//        return members;
//    }

    public ForeignCollection<DbChannelMessage> getMessages() {
        return messages;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public DbChannel() {
    }
}
