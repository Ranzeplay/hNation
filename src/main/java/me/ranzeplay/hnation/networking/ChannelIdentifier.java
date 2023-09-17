package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class ChannelIdentifier {
    public static final Identifier CREATE_CHANNEL_REQUEST = new Identifier("hnation.networking.request", "channel/create");
    public static final Identifier CREATE_CHANNEL_REPLY = new Identifier("hnation.networking.reply", "channel/create");
    public static final Identifier INVITE_CHANNEL_REQUEST = new Identifier("hnation.networking.request", "channel/invite");
    public static final Identifier INVITE_CHANNEL_REPLY = new Identifier("hnation.networking.reply", "channel/invite");
}
