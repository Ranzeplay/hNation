package me.ranzeplay.hnation.server.db.communication;

import me.ranzeplay.hnation.server.db.DbPlayer;
import net.minecraft.text.Text;

public class DbPrivateMessage extends ChatMessageBase {
    DbPlayer receiver;

    public DbPrivateMessage(String message, DbPlayer sender, DbPlayer receiver) {
        super(message, sender);
        this.receiver = receiver;
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(sender.getName()).append(" @ ").append("[PRIVATE]").append("> ")
                .append(message);
    }
}
