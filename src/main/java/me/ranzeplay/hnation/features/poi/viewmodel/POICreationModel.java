package me.ranzeplay.hnation.features.poi.viewmodel;

import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public record POICreationModel(int x, int y, int z, String worldName, String name, UUID playerUuid) {
    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putInt("x", x);
        nbt.putInt("y", y);
        nbt.putInt("z", z);
        nbt.putString("worldName", worldName);
        nbt.putString("name", name);
        nbt.putUuid("playerUuid", playerUuid);

        return nbt;
    }

    public static POICreationModel fromNbt(NbtCompound nbt) {
        return new POICreationModel(nbt.getInt("x"),
                nbt.getInt("y"),
                nbt.getInt("z"),
                nbt.getString("worldName"),
                nbt.getString("name"),
                nbt.getUuid("playerUuid"));
    }
}
