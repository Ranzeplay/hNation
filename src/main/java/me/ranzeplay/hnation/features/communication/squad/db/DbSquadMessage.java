package me.ranzeplay.hnation.features.communication.squad.db;

import me.ranzeplay.hnation.features.communication.ChatMessageBase;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class DbSquadMessage extends ChatMessageBase {
    public DbSquadMessage(String message, DbPlayer sender) {
        super(message, sender);
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(getSender().getName()).append(" @ ").append("[SQUAD]").append("> ")
                .append(getMessage());
    }

    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putString("message", getMessage());
        nbt.put("player", getSender().toNbt());

        return nbt;
    }

    public DbSquadMessage(NbtCompound nbt) {
        super(nbt.getString("message"), new DbPlayer(nbt.getCompound("player")));
    }
}
