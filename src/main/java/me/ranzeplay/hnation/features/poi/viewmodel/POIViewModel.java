package me.ranzeplay.hnation.features.poi.viewmodel;

import me.ranzeplay.hnation.main.ServerMain;
import me.ranzeplay.hnation.features.poi.db.DbPOI;
import net.minecraft.nbt.NbtCompound;

import java.sql.SQLException;

public final class POIViewModel {
    int x;
    int y;
    int z;
    long createTime;
    String worldName;
    String name;
    String playerName;

    public POIViewModel(int x, int y, int z, long createTime, String worldName, String name, String playerName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.createTime = createTime;
        this.worldName = worldName;
        this.name = name;
        this.playerName = playerName;
    }

    public POIViewModel(DbPOI poi) {
        this.name = poi.name();
        this.x = poi.x();
        this.y = poi.y();
        this.z = poi.z();
        this.createTime = poi.createTime().getTime();
        this.worldName = poi.worldName();

        try {
            this.playerName = ServerMain.dbManager.getPlayerDao().queryForId(poi.playerUuid()).getName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getName() {
        return name;
    }

    public String getPlayerName() {
        return playerName;
    }

    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putInt("x", x);
        nbt.putInt("y", y);
        nbt.putInt("z", z);
        nbt.putString("worldName", worldName);
        nbt.putLong("createTime", createTime);
        nbt.putString("name", name);
        nbt.putString("playerName", playerName);

        return nbt;
    }

    public static POIViewModel fromNbt(NbtCompound nbt) {
        return new POIViewModel(nbt.getInt("x"),
                nbt.getInt("y"),
                nbt.getInt("z"),
                nbt.getLong("createTime"),
                nbt.getString("worldName"),
                nbt.getString("name"),
                nbt.getString("playerName"));
    }
}
