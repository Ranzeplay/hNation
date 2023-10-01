package me.ranzeplay.hnation.features.communication.messaging.direct.viewmodel;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class DirectMessageView {
    String message;
    String senderName;
    String receiverName;

    public DirectMessageView(String message, String senderName, String receiverName) {
        this.message = message;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public NbtCompound toNbt() {
        var comp = new NbtCompound();
        comp.putString("sender", getSenderName());
        comp.putString("receiver", getReceiverName());
        comp.putString("message", getMessage());
        return comp;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public Text toMessageText() {
        return Text.empty()
                .append("<").append(getSenderName()).append(" @ ").append("[PRIVATE]").append("> ")
                .append(getMessage());
    }

    public DirectMessageView(NbtCompound nbt) {
        this(nbt.getString("message"), nbt.getString("sender"), nbt.getString("receiver"));
    }
}
