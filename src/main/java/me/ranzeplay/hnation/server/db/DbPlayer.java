package me.ranzeplay.hnation.server.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "player")
public class DbPlayer {
    @DatabaseField(id = true, canBeNull = false)
    UUID id;
    @DatabaseField(canBeNull = false)
    String name;

    public DbPlayer(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public DbPlayer() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
