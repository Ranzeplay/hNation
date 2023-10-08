package me.ranzeplay.hnation.features.communication.mail.viewmodel;

import net.minecraft.nbt.NbtCompound;

public class SendMailViewModel {
    String receiverName;
    String title;
    String content;

    public SendMailViewModel(String receiverName, String title, String content) {
        this.receiverName = receiverName;
        this.title = title;
        this.content = content;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public NbtCompound toNbt() {
        var comp = new NbtCompound();
        comp.putString("receiver", receiverName);
        comp.putString("title", title);
        comp.putString("content", content);

        return comp;
    }

    public SendMailViewModel(NbtCompound nbt) {
        this.receiverName = nbt.getString("receiver");
        this.title = nbt.getString("title");
        this.content = nbt.getString("content");
    }
}
