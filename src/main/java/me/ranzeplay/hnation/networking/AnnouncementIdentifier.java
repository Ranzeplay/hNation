package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class AnnouncementIdentifier {
    public static final Identifier PUBLISH_ANNOUNCEMENT_REQUEST = new Identifier("hnation.networking.request", "announcement/publish");
    public static final Identifier PUBLISH_ANNOUNCEMENT_NOTIFY = new Identifier("hnation.networking.notify", "announcement/publish");
    public static final Identifier BROADCAST_ANNOUNCEMENT_NOTIFY = new Identifier("hnation.networking.notify", "announcement/broadcast");
    public static final Identifier PULL_ANNOUNCEMENT_REQUEST = new Identifier("hnation.networking.request", "announcement/pull");
    public static final Identifier PULL_ANNOUNCEMENT_REPLY = new Identifier("hnation.networking.reply", "announcement/pull");
}
