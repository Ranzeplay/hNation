package me.ranzeplay.hnation.features.communication.announcement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import net.minecraft.nbt.NbtCompound;

@AllArgsConstructor
@Getter
public class CreateAnnouncementRequestModel extends AbstractNBTSerializable {
    String title;
    String content;

    @Override
    public NbtCompound toNbt() {
        var comp = new NbtCompound();
        comp.putString("title", title);
        comp.putString("content", content);
        return comp;
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        title = nbt.getString("title");
        content = nbt.getString("content");
    }

    @Override
    public Class<?> getGenericClass() {
        return null;
    }
}
