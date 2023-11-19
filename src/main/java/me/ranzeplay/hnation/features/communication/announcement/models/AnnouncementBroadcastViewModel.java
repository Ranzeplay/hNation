package me.ranzeplay.hnation.features.communication.announcement.models;

import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import net.minecraft.nbt.NbtCompound;

public class AnnouncementBroadcastViewModel extends AbstractNBTSerializable {
    @Override
    public NbtCompound toNbt() {
        return null;
    }

    @Override
    public void fromNbt(NbtCompound nbt) {

    }

    @Override
    public Class<?> getGenericClass() {
        return null;
    }
}
