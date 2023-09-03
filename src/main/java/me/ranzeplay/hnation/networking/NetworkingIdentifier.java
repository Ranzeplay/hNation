package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class NetworkingIdentifier {
    public static final Identifier CREATE_POI_REQUEST = new Identifier("hnation.networking.request", "poi/create");
    public static final Identifier CREATE_POI_REPLY = new Identifier("hnation.networking.reply", "poi/create");

    public static final Identifier QUERY_POI_REQUEST = new Identifier("hnation.networking.request", "poi/query");
    public static final Identifier QUERY_POI_REPLY = new Identifier("hnation.networking.reply", "poi/query");

    public static final Identifier CREATE_REGION_REQUEST = new Identifier("hnation.networking.request", "region/create");
    public static final Identifier CREATE_REGION_REPLY = new Identifier("hnation.networking.reply", "region/create");

    public static final Identifier SEND_CHAT_PUBLIC = new Identifier("hnation.networking.chat", "send/public");
    public static final Identifier SEND_CHAT_PRIVATE = new Identifier("hnation.networking.chat", "send/private");
    public static final Identifier SEND_CHAT_CHANNEL = new Identifier("hnation.networking.chat", "send/channel");
    public static final Identifier SEND_CHAT_SQUAD = new Identifier("hnation.networking.chat", "send/squad");

    public static final Identifier CREATE_CHANNEL_REQUEST = new Identifier("hnation.networking.request", "channel/create");
    public static final Identifier CREATE_CHANNEL_REPLY = new Identifier("hnation.networking.reply", "channel/create");
    public static final Identifier INVITE_CHANNEL_REQUEST = new Identifier("hnation.networking.request", "channel/invite");
    public static final Identifier INVITE_CHANNEL_REPLY = new Identifier("hnation.networking.reply", "channel/invite");

    public static final Identifier SQUAD_CREATE_REQUEST = new Identifier("hnation.networking.request", "squad/create");
    public static final Identifier SQUAD_CREATE_REPLY = new Identifier("hnation.networking.reply", "squad/create");
    public static final Identifier SQUAD_JOIN_REQUEST = new Identifier("hnation.networking.request", "squad/join");
    public static final Identifier SQUAD_JOIN_REQUEST_NOTIFY = new Identifier("hnation.networking.notify", "squad/join");
    public static final Identifier SQUAD_JOIN_REACTION = new Identifier("hnation.networking.request", "squad/join");
    public static final Identifier SQUAD_KICK_REQUEST = new Identifier("hnation.networking.request", "squad/kick");
    public static final Identifier SQUAD_KICK_NOTIFY = new Identifier("hnation.networking.notify", "squad/kick");
    public static final Identifier SQUAD_INVITE_REQUEST = new Identifier("hnation.networking.request", "squad/kick");
    public static final Identifier SQUAD_INVITE_NOTIFY = new Identifier("hnation.networking.request", "squad/kick");
    public static final Identifier SQUAD_LEAVE_REQUEST = new Identifier("hnation.networking.request", "squad/leave");
    public static final Identifier SQUAD_LEAVE_NOTIFY = new Identifier("hnation.networking.notify", "squad/leave");
    public static final Identifier SQUAD_TRANSFER_REQUEST = new Identifier("hnation.networking.request", "squad/transfer");
    public static final Identifier SQUAD_TRANSFER_NOTIFY = new Identifier("hnation.networking.notify", "squad/transfer");
    public static final Identifier SQUAD_DISMISS_REQUEST = new Identifier("hnation.networking.request", "squad/dismiss");
    public static final Identifier SQUAD_DISMISS_NOTIFY = new Identifier("hnation.networking.notify", "squad/dismiss");
    public static final Identifier SQUAD_WARN_REQUEST = new Identifier("hnation.networking.request", "squad/warn");
    public static final Identifier SQUAD_WARN_NOTIFY = new Identifier("hnation.networking.notify", "squad/warn");
    public static final Identifier SQUAD_STATUS_SYNC = new Identifier("hnation.networking.sync", "squad/status");

    public static final Identifier CREATE_TRANSIT_LINE_REQUEST = new Identifier("hnation.networking.request", "transit/line/create");
    public static final Identifier CREATE_TRANSIT_LINE_REPLY = new Identifier("hnation.networking.reply", "transit/line/create");
}
