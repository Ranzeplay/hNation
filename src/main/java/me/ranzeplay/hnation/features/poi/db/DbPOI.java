package me.ranzeplay.hnation.features.poi.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.util.UUID;

@DatabaseTable(tableName = "poi")
public final class DbPOI {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;
    @DatabaseField(canBeNull = false)
    String name;
    @DatabaseField(canBeNull = false)
    Timestamp createTime;
    @DatabaseField(canBeNull = false)
    UUID playerUuid;
    @DatabaseField(canBeNull = false)
    String worldName;
    @DatabaseField(canBeNull = false)
    int x;
    @DatabaseField(canBeNull = false)
    int y;
    @DatabaseField(canBeNull = false)
    int z;

    public DbPOI(
            UUID id,
            String name,
            Timestamp createTime,
            UUID playerUuid,
            String worldName,
            int x,
            int y,
            int z) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.playerUuid = playerUuid;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DbPOI() {
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Timestamp createTime() {
        return createTime;
    }

    public UUID playerUuid() {
        return playerUuid;
    }

    public String worldName() {
        return worldName;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }
}
