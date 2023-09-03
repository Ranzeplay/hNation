package me.ranzeplay.hnation.networking.squad;

import net.minecraft.nbt.NbtCompound;

public class SquadCreationReplyViewModel {
    boolean success;
    String message;

    public SquadCreationReplyViewModel(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putBoolean("success", success);
        nbt.putString("message", message);
        return nbt;
    }

    public SquadCreationReplyViewModel(NbtCompound nbt) {
        this.success = nbt.getBoolean("success");
        this.message = nbt.getString("message");
    }
}
