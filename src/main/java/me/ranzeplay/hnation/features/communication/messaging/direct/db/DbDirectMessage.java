package me.ranzeplay.hnation.features.communication.messaging.direct.db;

import me.ranzeplay.hnation.features.communication.ChatMessageBase;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.minecraft.text.Text;

public class DbDirectMessage extends ChatMessageBase {
    DbPlayer receiver;

    public DbDirectMessage(String message, DbPlayer sender, DbPlayer receiver) {
        super(message, sender);
        this.receiver = receiver;
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(getSender().getName()).append(" @ ").append("[PRIVATE]").append("> ")
                .append(getMessage());
    }
}
