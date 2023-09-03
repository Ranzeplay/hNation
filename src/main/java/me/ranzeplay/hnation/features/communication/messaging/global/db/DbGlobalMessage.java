package me.ranzeplay.hnation.features.communication.messaging.global.db;

import me.ranzeplay.hnation.features.communication.ChatMessageBase;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import net.minecraft.text.Text;

public class DbGlobalMessage extends ChatMessageBase {
    public DbGlobalMessage(String message, DbPlayer sender) {
        super(message, sender);
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(getSender().getName()).append(" @ ").append("[PUBLIC]").append("> ")
                .append(getMessage());
    }

    public DbGlobalMessage() {
    }
}
