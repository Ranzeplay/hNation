package me.ranzeplay.hnation.server.db.communication;

import me.ranzeplay.hnation.server.db.DbPlayer;
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
}
