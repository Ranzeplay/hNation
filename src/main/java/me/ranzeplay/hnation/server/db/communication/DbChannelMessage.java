package me.ranzeplay.hnation.server.db.communication;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.ranzeplay.hnation.server.db.DbPlayer;
import net.minecraft.text.Text;

@DatabaseTable(tableName = "channelMessage")
public class DbChannelMessage extends ChatMessageBase {
    @DatabaseField(canBeNull = false, foreign = true)
    DbChannel channel;

    public DbChannelMessage(String message, DbPlayer sender) {
        super(message, sender);
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(sender.getName()).append(" @ ").append("[C]").append(channel.getName()).append("> ")
                .append(message);
    }
}
