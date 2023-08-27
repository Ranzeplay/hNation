package me.ranzeplay.hnation.server.db.communication;

import me.ranzeplay.hnation.server.db.DbPlayer;
import net.minecraft.text.Text;

public class DbPublicMessage extends ChatMessageBase {
    public DbPublicMessage(String message, DbPlayer sender) {
        super(message, sender);
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(sender.getName()).append(" @ ").append("[PUBLIC]").append("> ")
                .append(message);
    }

    public DbPublicMessage() {
    }
}
