package me.ranzeplay.hnation.server.db.communication;

import me.ranzeplay.hnation.server.db.DbPlayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class DbSquadMessage extends ChatMessageBase {
    public DbSquadMessage(String message, DbPlayer sender) {
        super(message, sender);
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(sender.getName()).append(" @ ").append("[SQUAD]").append("> ")
                .append(message);
    }

    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putString("message", message);
        nbt.put("player", sender.toNbt());

        return nbt;
    }

    public DbSquadMessage(NbtCompound nbt) {
        this.message = nbt.getString("message");
        this.sender = new DbPlayer(nbt.getCompound("player"));
    }
}
