package me.ranzeplay.hnation.features.communication.announcement.models;

import me.ranzeplay.hnation.features.communication.announcement.db.DbAnnouncement;
import net.minecraft.nbt.NbtCompound;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AnnouncementViewModel {
    String senderName;
    long publishTime;
    String title;
    String content;

    public AnnouncementViewModel(String sender, long publishTime, String title, String content) {
        this.senderName = sender;
        this.publishTime = publishTime;
        this.title = title;
        this.content = content;
    }

    public AnnouncementViewModel(DbAnnouncement announcement) {
        this.senderName = announcement.getAnnouncer().getName();
        this.publishTime = announcement.getCreateTime().getTime();
        this.title = announcement.getTitle();
        this.content = announcement.getContent();
    }

    public String getSenderName() {
        return senderName;
    }

    public String getPublishTime() {
        return new Date(publishTime)
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public NbtCompound toNbt() {
        var comp = new NbtCompound();
        comp.putString("senderName", senderName);
        comp.putLong("publishTime", publishTime);
        comp.putString("title", title);
        comp.putString("content", content);

        return comp;
    }

    public AnnouncementViewModel(NbtCompound nbt) {
        this.senderName = nbt.getString("senderName");
        this.title = nbt.getString("title");
        this.content = nbt.getString("content");
        this.publishTime = nbt.getLong("publishTime");
    }

    @Override
    public String toString() {
        return String.format("%s @ %s\n%s\nby %s\n",
                this.getTitle(),
                this.getPublishTime(),
                this.getContent(),
                this.getSenderName());
    }
}
